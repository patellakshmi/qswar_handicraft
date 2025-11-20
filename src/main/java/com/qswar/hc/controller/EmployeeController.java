package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.CloserReport;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
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

    @GetMapping(APIConstant.PRIVATE+"/api/v1/employee")
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


    @GetMapping(APIConstant.PRIVATE+"/api/v1/employees")
    public ResponseEntity<GenericResponse> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();

        for (Employee employee : employees) {
            employee.setVisits(null);
        }

        if (employees.isEmpty()) {
            // Return 204 No Content if the list is empty
            return ResponseEntity.noContent().build();
        }

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, employees),
                HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // Assuming the client provides a unique emp_id as per the schema, or the database handles it.
        // In a production app, more thorough validation and security (like password hashing) would be added here.

        Employee savedEmployee = employeeService.saveEmployee(employee);

        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }


    @PutMapping(APIConstant.PRIVATE+"/api/v1/employees/{id}")
    public ResponseEntity<GenericResponse> updateEmployee(@PathVariable Integer id, @RequestBody Employee employeeDetails) {
        Optional<Employee> employeeOptional = employeeService.findById(id);

        if (employeeOptional == null) {
            return ResponseEntity.notFound().build();
        }

        // Get existing employee and update fields from the payload
        Employee existingEmployee = employeeOptional.get();

        // Update fields (excluding collections which are handled separately in JPA)
        existingEmployee.setGovEmpId(employeeDetails.getGovEmpId());
        existingEmployee.setName(employeeDetails.getName());
        existingEmployee.setPhone(employeeDetails.getPhone());
        existingEmployee.setEmail(employeeDetails.getEmail());
        existingEmployee.setPosition(employeeDetails.getPosition());
        existingEmployee.setAuthorised(employeeDetails.getAuthorised());
        existingEmployee.setDepartment(employeeDetails.getDepartment());
        // WARNING: Updating password via PUT is generally bad practice; a separate endpoint is recommended.
        existingEmployee.setPassword(employeeDetails.getPassword());

        // Update manager reference
        existingEmployee.setManager(employeeDetails.getManager());

        // Save the updated entity
        Employee updatedEmployee = employeeService.saveEmployee(existingEmployee);
        updatedEmployee.setVisits(null);

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, updatedEmployee),
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
}