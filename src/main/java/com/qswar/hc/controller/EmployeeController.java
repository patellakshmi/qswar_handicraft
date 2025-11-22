package com.qswar.hc.controller;

import com.google.gson.Gson;
import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.enumurator.Authorize;
import com.qswar.hc.enumurator.EmpAuthorized;
import com.qswar.hc.model.CloserReport;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.EmployeeRequest;
import com.qswar.hc.pojos.responses.CloserReportResponse;
import com.qswar.hc.pojos.responses.EmployeeResponse;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import com.qswar.hc.repository.CloserReportRepository;
import com.qswar.hc.service.EmployeeService;
import com.qswar.hc.service.VisitService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class EmployeeController {
    private final Gson gson = new Gson();
    private static final Log log = LogFactory.getLog(EmployeeController.class);
    private final EmployeeService employeeService;
    private final VisitService visitService;
    private final CloserReportRepository closerReportRepository;

    @Autowired
    public EmployeeController(EmployeeService employeeService, VisitService visitService, CloserReportRepository closerReportRepository) {
        this.employeeService = employeeService;
        this.visitService = visitService;
        this.closerReportRepository = closerReportRepository;
    }

    @GetMapping(APIConstant.PRIVATE+"/v1/employee")
    public ResponseEntity<GenericResponse> getEmployee(@RequestHeader("hc_auth") String authToken,@RequestHeader("identity") String identity, @RequestHeader("tree_height") int height){

        Employee employee = employeeService.getEmployee(identity);
        List<Visit> visitList = visitService.findByEmployeeId(employee);

        List<VisitResponse> visitResponseList = new ArrayList<>();

        if( visitList != null && !visitList.isEmpty()) {
            for (Visit visit : visitList) {
                VisitResponse visitResponse = convert(visit);

                List<CloserReport> closerReportList = (List<CloserReport>) closerReportRepository.findByVisitVisitId(visit.getVisitId());
                List<CloserReportResponse> closerReportResponseList = new ArrayList<>();

                if( closerReportList != null && !closerReportList.isEmpty()) {
                    for (CloserReport closerReport : closerReportList) {
                        CloserReportResponse closerReportResponse = covert(closerReport);
                        closerReportResponseList.add(closerReportResponse);
                    }
                }

                visitResponseList.add(visitResponse);
            }
        }
        EmployeeResponse employeeResponse = convert(employee);
        employeeResponse.setVisits(visitResponseList);

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, employeeResponse),
                HttpStatus.OK);
    }


    @GetMapping(APIConstant.PUBLIC+"/v1/registered/employee")
    public ResponseEntity<GenericResponse> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();

        if(employees != null && !employees.isEmpty()) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.SUCCESS.name(),
                            Constant.NO_EMPLOYEE_FOUND, null),
                    HttpStatus.OK);
        }

        List<EmployeeResponse>  employeeResponseList = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeResponse employeeResponse = convert(employee);
            employeeResponseList.add(employeeResponse);
        }


        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, employeeResponseList),
                HttpStatus.OK);

    }

    @PostMapping(APIConstant.PRIVATE+"/v1/employee")
    public ResponseEntity<GenericResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest , @RequestHeader("hc_auth") String hcAuth, @RequestHeader("identity") String identity) {

        Employee superAdmin = employeeService.getEmployee(identity);
        EmpAuthorized empAuthorized = this.gson.fromJson(superAdmin.getAuthorised(), EmpAuthorized.class);

        if( !empAuthorized.isAuthorized(Authorize.SUPER_ADMIN)){
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse( Constant.STATUS.SUCCESS.name(),
                            Constant.UNAUTHORIZED_FOR_CREATE_EMP, null),
                    HttpStatus.OK);
        }

        Employee empHasToCreate = covert(employeeRequest);

        Employee savedEmployee = employeeService.saveEmployee(empHasToCreate);

        if( savedEmployee != null) {
            EmployeeResponse employeeResponse = convert(savedEmployee);
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse( Constant.STATUS.SUCCESS.name(),
                            Constant.EMPLOYEE_CREATED, employeeResponse),
                    HttpStatus.OK);
        }

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.FAIL_TO_CREATE_EMPLOYEE, null),
                HttpStatus.OK);
    }


    @PutMapping(APIConstant.PRIVATE+"/v1/employee")
    public ResponseEntity<GenericResponse> updateEmployee( @RequestBody EmployeeRequest employeeRequest, @RequestHeader("hc_auth") String hcAuth, @RequestHeader("identity") String identity) {
        Employee employee = employeeService.getEmployee(identity);

        if (employee == null) {
            return ResponseEntity.notFound().build();
        }

        if( !StringUtils.isBlank(employeeRequest.getGovEmpId()))
            employee.setGovEmpId(employeeRequest.getGovEmpId());
        if( !StringUtils.isBlank(employeeRequest.getName()))
            employee.setName(employeeRequest.getName());
        if( !StringUtils.isBlank(employeeRequest.getPhone()))
            employee.setPhone(employeeRequest.getPhone());
        if( !StringUtils.isBlank(employeeRequest.getEmail()))
            employee.setEmail(employeeRequest.getEmail());
        if( !StringUtils.isBlank(employeeRequest.getPosition()))
            employee.setPosition(employeeRequest.getPosition());
        if( !StringUtils.isBlank(employeeRequest.getAuthorised()))
            employee.setAuthorised(employeeRequest.getAuthorised());
        if( !StringUtils.isBlank(employeeRequest.getDepartment()))
            employee.setDepartment(employeeRequest.getDepartment());

        Employee updatedEmployee = employeeService.saveEmployee(employee);
        updatedEmployee.setVisits(null);
        EmployeeResponse employeeResponse = convert(updatedEmployee);


        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.EMPLOYEE_UPDATED, employeeResponse),
                HttpStatus.OK);

    }



    private EmployeeResponse convert(Employee employee){
        if( employee == null){ return null; }
        EmployeeResponse employeeResponse = new EmployeeResponse();
        employeeResponse.setGovEmpId(employee.getGovEmpId());
        employeeResponse.setName(employee.getName());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPosition(employee.getPosition());
        employeeResponse.setAuthorised(employee.getAuthorised());
        employeeResponse.setDepartment(employee.getDepartment());
        employeeResponse.setUsername(employee.getUsername());
        employeeResponse.setQSwarId(employee.getQSwarId());
        employeeResponse.setAuthorised(employee.getAuthorised());
        employeeResponse.setDepartment(employee.getDepartment());

        if( employee.getManager() != null ) {
            Employee manager = employeeService.findManager(employee.getManager().getId());
            if (manager != null) {
                EmployeeResponse managerResponse = new EmployeeResponse();
                managerResponse.setGovEmpId(manager.getGovEmpId());
                managerResponse.setName(manager.getName());
                managerResponse.setPhone(manager.getPhone());
                managerResponse.setEmail(manager.getEmail());
                managerResponse.setPosition(manager.getPosition());
                managerResponse.setAuthorised(manager.getAuthorised());
                managerResponse.setDepartment(manager.getDepartment());
                managerResponse.setUsername(manager.getUsername());
                managerResponse.setQSwarId(manager.getQSwarId());
                managerResponse.setAuthorised(manager.getAuthorised());
                managerResponse.setDepartment(manager.getDepartment());

                employeeResponse.setManager(managerResponse);
            }
        }

        List<Employee> subOrdinates = employeeService.findSubordinate(employee.getId());
        List<EmployeeResponse> subOrdinateResponsesList = new ArrayList<>();

        if( subOrdinates != null && subOrdinates.size() > 0 ){
            for (Employee subOrdinate : subOrdinates) {
                EmployeeResponse subOrdinateResponse = new EmployeeResponse();
                subOrdinateResponse.setGovEmpId(subOrdinate.getGovEmpId());
                subOrdinateResponse.setName(subOrdinate.getName());
                subOrdinateResponse.setPhone(subOrdinate.getPhone());
                subOrdinateResponse.setEmail(subOrdinate.getEmail());
                subOrdinateResponse.setPosition(subOrdinate.getPosition());
                subOrdinateResponse.setAuthorised(subOrdinate.getAuthorised());
                subOrdinateResponse.setDepartment(subOrdinate.getDepartment());
                subOrdinateResponse.setUsername(subOrdinate.getUsername());
                subOrdinateResponse.setQSwarId(subOrdinate.getQSwarId());
                subOrdinateResponse.setAuthorised(subOrdinate.getAuthorised());
                subOrdinateResponse.setDepartment(subOrdinate.getDepartment());
                subOrdinateResponsesList.add(subOrdinateResponse);
            }
            employeeResponse.setSubordinates(subOrdinateResponsesList);
        }

        return employeeResponse;
    }

    private VisitResponse convert(Visit visit){
        if ( visit == null){ return null; }
        VisitResponse visitResponse = new VisitResponse();

        if (visit.getVisitId() != null)
            visitResponse.setVisitId(visit.getVisitId());

        if ( visit.getStartDate() != null )
            visitResponse.setStartDate(visit.getStartDate());

        if ( visit.getEndDate() != null )
            visitResponse.setEndDate(visit.getEndDate());

        if ( !StringUtils.isBlank(visit.getVisitIconLink()))
            visitResponse.setVisitIconLink(visit.getVisitIconLink());

        if( !StringUtils.isBlank(visit.getLocation()))
            visitResponse.setLocation(visit.getLocation());

        if( !StringUtils.isBlank(visit.getPurpose()))
            visitResponse.setPurpose(visit.getPurpose());

        if( !StringUtils.isBlank(visit.getTitle()))
            visitResponse.setTitle(visit.getTitle());

        if( !StringUtils.isBlank(visit.getPlace()))
            visitResponse.setPlace(visit.getPlace());

        return visitResponse;

    }

    private CloserReportResponse covert(CloserReport closerReport){
            if( null == closerReport){ return null; }
            CloserReportResponse closerReportResponse = new CloserReportResponse();

            if( closerReport.getCrId() == null)
                closerReportResponse.setCrId(closerReport.getCrId());

            if( !StringUtils.isBlank(closerReport.getDocType()))
                closerReportResponse.setDocType(closerReport.getDocType());

            if( !StringUtils.isBlank(closerReport.getDocSubType()))
                closerReportResponse.setDocSubType(closerReport.getDocSubType());

            if( !StringUtils.isBlank(closerReport.getTitle()))
                closerReportResponse.setTitle(closerReport.getTitle());

            if( !StringUtils.isBlank(closerReport.getDetails()))
                closerReportResponse.setDetails(closerReport.getDescription());

            if( !StringUtils.isBlank(closerReport.getDocLink()))
                closerReportResponse.setDocLink(closerReport.getDocLink());

            if( !StringUtils.isBlank(closerReport.getDescription()))
                closerReportResponse.setDescription(closerReport.getDescription());

            if( closerReport.getCreatedAt() != null)
                closerReportResponse.setCreatedAt(closerReport.getCreatedAt());

            if( closerReport.getUpdatedAt() != null)
                closerReportResponse.setUpdatedAt(closerReport.getUpdatedAt());

            if( !StringUtils.isBlank(closerReport.getManagerFeedback()))
                closerReportResponse.setManagerFeedback(closerReport.getManagerFeedback());

            if( !StringUtils.isBlank(closerReport.getManagerApproval()))
                closerReportResponse.setManagerApproval(closerReport.getManagerApproval());

            return closerReportResponse;
    }

    private Employee covert(EmployeeRequest employeeRequest){
        if( null == employeeRequest){ return null; }
        Employee employee = new Employee();
        employee.setGovEmpId(employeeRequest.getGovEmpId());
        employee.setName(employeeRequest.getName());
        employee.setPhone(employeeRequest.getPhone());
        employee.setEmail(employeeRequest.getEmail());
        employee.setPosition(employeeRequest.getPosition());
        employee.setAuthorised(employeeRequest.getAuthorised());
        employee.setDepartment(employeeRequest.getDepartment());
        employee.setUsername(employeeRequest.getUsername());
        return employee;
    }
}