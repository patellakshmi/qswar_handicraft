package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.UpdateVisitRequest;
import com.qswar.hc.pojos.requests.VisitRequest;
import com.qswar.hc.pojos.responses.EmployeeResponse;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import com.qswar.hc.repository.VisitRepository;
import com.qswar.hc.service.EmployeeService;
import com.qswar.hc.service.VisitService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private EmployeeService employeeService;



    @PostMapping(APIConstant.PRIVATE+APIConstant.VERSION_1+"/visit")
    public ResponseEntity<GenericResponse> createVisit(@RequestBody VisitRequest visitRequest, @RequestHeader("hc_auth") String authToken,  @RequestHeader("identity") String identity, @RequestHeader("force") Boolean force) {
        if(StringUtils.isBlank(identity)) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                           null, null),
                    HttpStatus.BAD_REQUEST);
        }

        if( visitRequest.getStartDate() == null ||  visitRequest.getEndDate() == null || visitRequest.getStartDate().before(new Date()) ) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            "Visit date", null),
                    HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeService.getEmployee(identity);

        if(employee == null) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }



        if( force != null && force ){
            List<Visit> earlierExitingVisit = visitService.findByEmployeeId(employee);
            if( earlierExitingVisit != null ){
                for( Visit visit : earlierExitingVisit ){
                    if( !( visit.getStartDate().before(visitRequest.getStartDate()) && visit.getEndDate().before( visitRequest.getStartDate()) || visit.getEndDate().after(visitRequest.getEndDate()) &&  visit.getStartDate().after(visitRequest.getEndDate()))  ) {
                        // Overlap
                        return new ResponseEntity<GenericResponse>(
                                new GenericResponse(Constant.STATUS.FAIL.name(),
                                        "Some of visit has overlapping date", null),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        Visit visit = convert(visitRequest);
        visit.setEmployee(employee);
        visit = visitService.save(visit);
        VisitResponse visitResponse = convert(visit, false);

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.COURSE_CREATED, visitResponse),
                HttpStatus.OK);
    }

    @PutMapping(APIConstant.PRIVATE+APIConstant.VERSION_1+"/visit")
    public ResponseEntity<GenericResponse> updateVisit(@RequestBody UpdateVisitRequest visitRequest, @RequestHeader("hc_auth") String authToken, @RequestHeader("identity") String identity, @RequestHeader("force") Boolean force) {

        if(StringUtils.isBlank(identity)) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }

        Long visitId = visitRequest.getVisitId();
        Visit needToBeUpdatedVisit = visitService.getVisitById(visitId);

        if(needToBeUpdatedVisit == null) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            "Visit does not exit", null),
                    HttpStatus.BAD_REQUEST);
        }
        Employee employee = employeeService.getEmployee(identity);
        Employee visitEmployee = needToBeUpdatedVisit.getEmployee();
        if(employee == null || visitEmployee.getId() != employee.getId() ) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            "You can't update this visit", null),
                    HttpStatus.BAD_REQUEST);
        }

        if( visitRequest.getStartDate() == null ||  visitRequest.getEndDate() == null || visitRequest.getStartDate().before(new Date()) ) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            "Visit date", null),
                    HttpStatus.BAD_REQUEST);
        }

        if(employee == null) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse(Constant.STATUS.FAIL.name(),
                            null, null),
                    HttpStatus.BAD_REQUEST);
        }

        if( force != null && force ){
            List<Visit> earlierExitingVisit = visitService.findByEmployeeId(employee);
            if( earlierExitingVisit != null ){
                for( Visit visit : earlierExitingVisit ){
                    if( !( visit.getStartDate().before(visitRequest.getStartDate()) && visit.getEndDate().before( visitRequest.getStartDate()) || visit.getEndDate().after(visitRequest.getEndDate()) &&  visit.getStartDate().after(visitRequest.getEndDate()))  ) {
                        // Overlap
                        return new ResponseEntity<GenericResponse>(
                                new GenericResponse(Constant.STATUS.FAIL.name(),
                                        "Some of visit has overlapping date", null),
                                HttpStatus.BAD_REQUEST);
                    }
                }
            }
        }

        updateVisit(needToBeUpdatedVisit, visitRequest);
        needToBeUpdatedVisit = visitService.save(needToBeUpdatedVisit);
        VisitResponse visitResponse = convert(needToBeUpdatedVisit, false);

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

    private void updateVisit(Visit needToBeUpdatedVisit, UpdateVisitRequest updateVisitRequest){
        if( updateVisitRequest.getStartDate() != null  &&
                !DateUtils.isSameDay(updateVisitRequest.getStartDate(), needToBeUpdatedVisit.getStartDate())) {
            needToBeUpdatedVisit.setStartDate(updateVisitRequest.getStartDate());
        }
        if( updateVisitRequest.getEndDate() != null  &&
                !DateUtils.isSameDay(updateVisitRequest.getEndDate(), needToBeUpdatedVisit.getEndDate())) {
            needToBeUpdatedVisit.setEndDate(updateVisitRequest.getEndDate());
        }

        if( updateVisitRequest.getTitle() != null && !StringUtils.isEmpty(updateVisitRequest.getTitle()) ) {
            needToBeUpdatedVisit.setTitle(updateVisitRequest.getTitle());
        }

        if( updateVisitRequest.getPurpose() != null && !StringUtils.isEmpty(updateVisitRequest.getPurpose()) ) {
            needToBeUpdatedVisit.setPurpose(updateVisitRequest.getPurpose());
        }

        if ( updateVisitRequest.getLocation() != null && !StringUtils.isEmpty(updateVisitRequest.getLocation()) ) {
            needToBeUpdatedVisit.setLocation(updateVisitRequest.getLocation());
        }

        if( updateVisitRequest.getPlace() != null && !StringUtils.isEmpty(updateVisitRequest.getPlace()) ) {
            needToBeUpdatedVisit.setPlace(updateVisitRequest.getPlace());
        }

        if( updateVisitRequest.getVisitIconLink() != null && !StringUtils.isEmpty(updateVisitRequest.getVisitIconLink()) ) {
            needToBeUpdatedVisit.setVisitIconLink(updateVisitRequest.getVisitIconLink());
        }

        if( updateVisitRequest.getVisitStatus() != null && !StringUtils.isEmpty(updateVisitRequest.getVisitStatus()) ) {
            needToBeUpdatedVisit.setVisitStatus(updateVisitRequest.getVisitStatus());
        }

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

        if(  visitRequest.getStartDate()!= null )
            visit.setStartDate(visitRequest.getStartDate());

        if(  visitRequest.getEndDate() != null )
            visit.setEndDate(visitRequest.getEndDate());

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
