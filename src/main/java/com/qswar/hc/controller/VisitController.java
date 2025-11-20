package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.VisitRequest;
import com.qswar.hc.pojos.responses.EmployeeResponse;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import com.qswar.hc.repository.VisitRepository;
import com.qswar.hc.service.EmployeeService;
import com.qswar.hc.service.VisitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private EmployeeService employeeService;



    @PostMapping(APIConstant.PRIVATE+APIConstant.VERSION_1+"/visit")
    public ResponseEntity<GenericResponse> createVisit(@RequestBody VisitRequest visitRequest, @RequestHeader("hc_auth") String authToken,  @RequestHeader("identity") String identity) {
        if(StringUtils.isBlank(identity)) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                           null, null),
                    HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeService.getEmployee(identity);
        if(employee == null) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }

        Visit visit = convert(visitRequest);
        visit.setEmployee(employee);
        visit = visitService.save(visit);
        VisitResponse visitResponse = convert(visit);

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, visitResponse),
                HttpStatus.OK);
    }

    @GetMapping(APIConstant.PRIVATE+APIConstant.VERSION_1+"/visit")
    public ResponseEntity<GenericResponse> getVisit(@RequestHeader("hc_auth") String authToken,  @RequestHeader("identity") String identity) {
        if(StringUtils.isBlank(identity)) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeService.getEmployee(identity);
        if(employee == null) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }


        List<Visit> visitList= visitService.findByEmployeeId( employee);
        List<VisitResponse> visitResponseList= new ArrayList<>();
        if( visitList != null && !visitList.isEmpty()) {
            for(Visit visit:visitList) {
                VisitResponse visitResponse = convert(visit, false);
                visitResponseList.add(visitResponse);
            }

        }

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, visitResponseList),
                HttpStatus.OK);
    }




    private Visit convert(VisitRequest visitRequest) {

        if ( visitRequest == null) return null;

        Visit visit = new Visit();

        if( !StringUtils.isBlank(visitRequest.getTitle()))
            visit.setTitle(visitRequest.getTitle());

        if( !StringUtils.isBlank(visitRequest.getPurpose()))
            visit.setPurpose(visitRequest.getPurpose());

        if( !StringUtils.isBlank(visitRequest.getPlace()))
            visit.setPlace(visitRequest.getPlace());

        if( !StringUtils.isBlank(visitRequest.getLocation()))
            visit.setLocation(visitRequest.getLocation());

        /*if(  visitRequest.getFromDate() != null )
            visit.setFromDate(visitRequest.getFromDate());

        if(  visitRequest.getToDate() != null )
            visit.setFromDate(visitRequest.getToDate());*/

        if( !StringUtils.isBlank(visitRequest.getVisitIconLink()))
            visit.setVisitIconLink(visitRequest.getVisitIconLink());

        return visit;
    }


    private VisitResponse convert(Visit visit){
        return convert(visit, true);
    }

    private VisitResponse convert(Visit visit, boolean isEmpReq) {

        if ( visit == null) return null;

        VisitResponse visitResponse = new VisitResponse();

        if( visit.getVisitId() != null)
            visitResponse.setVisitId(visit.getVisitId());

        if( !StringUtils.isBlank(visit.getTitle()))
            visitResponse.setTitle(visit.getTitle());

        if( !StringUtils.isBlank(visit.getPurpose()))
            visitResponse.setPurpose(visit.getPurpose());

        if( !StringUtils.isBlank(visit.getPlace()))
            visitResponse.setPlace(visit.getPlace());

        if( !StringUtils.isBlank(visit.getLocation()))
            visitResponse.setLocation(visit.getLocation());

        if(  visit.getStartDate() != null )
            visitResponse.setStartDate(visit.getStartDate());

        if(  visit.getEndDate() != null )
            visitResponse.setEndDate(visit.getEndDate());

        if( !StringUtils.isBlank(visit.getVisitIconLink()))
            visitResponse.setVisitIconLink(visit.getVisitIconLink());

        if( isEmpReq ) {
            if (visit.getEmployee() != null)
                visitResponse.setEmployee(convert(visit.getEmployee()));
        }

        return visitResponse;
    }

    EmployeeResponse convert(Employee employee) {
        if ( employee == null) return null;
        EmployeeResponse employeeResponse = new EmployeeResponse();

        if( !StringUtils.isBlank(employee.getEmail()))
            employeeResponse.setEmail(employeeResponse.getEmail());

        if( !StringUtils.isBlank(employee.getQSwarId()))
            employeeResponse.setQSwarId(employee.getQSwarId());

        if( !StringUtils.isBlank(employee.getGovEmpId()))
            employeeResponse.setGovEmpId(employee.getGovEmpId());

        if( !StringUtils.isBlank(employee.getUsername()))
            employeeResponse.setUsername(employee.getUsername());

        if( !StringUtils.isBlank(employee.getPhone()))
            employeeResponse.setPhone(employee.getPhone());

        if( !StringUtils.isBlank(employee.getEmail()))
            employeeResponse.setEmail(employee.getEmail());

        employeeResponse.setSubordinates(null);
        employeeResponse.setManager(null);
        employeeResponse.setVisits(null);
        return employeeResponse;
    }


}
