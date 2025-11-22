package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.mapper.VisitMapper;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.UpdateVisitRequest;
import com.qswar.hc.pojos.requests.VisitRequest;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import com.qswar.hc.service.EmployeeService;
import com.qswar.hc.service.VisitServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(APIConstant.PRIVATE + APIConstant.VERSION_1 + "/visit") // Class-level mapping
public class VisitController {

    private static final Logger log = LoggerFactory.getLogger(VisitController.class);

    private final VisitServiceImpl visitService;
    private final EmployeeService employeeService;
    private final VisitMapper visitMapper; // Inject the dedicated mapper

    // 1. Constructor Injection (preferred over field injection)
    public VisitController(VisitServiceImpl visitService, EmployeeService employeeService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.employeeService = employeeService;
        this.visitMapper = visitMapper;
    }

    // --- Helper Methods ---

    /**
     * Checks if a new visit request overlaps with any existing visits.
     * The original overlap logic was complex and slightly incorrect.
     * Correct Overlap Logic: (StartA < EndB) AND (EndA > StartB)
     * The original code checked for *no* overlap: (EndA <= StartB) OR (StartA >= EndB)
     */
    private boolean hasOverlap(List<Visit> existingVisits, Date newStart, Date newEnd) {
        if (existingVisits == null) return false;

        return existingVisits.stream().anyMatch(existing ->
                existing.getStartDate().before(newEnd) && existing.getEndDate().after(newStart)
        );
    }

    // --- API Endpoints ---

    /**
     * Creates a new Visit for an employee. Includes date validation and optional overlap check.
     */
    @PostMapping
    public ResponseEntity<GenericResponse> createVisit(
            @RequestBody VisitRequest visitRequest,
            @RequestHeader("identity") String identity,
            @RequestHeader(value = "force", required = false) Boolean force) { // force header made optional

        // 2. Input Validation (Combined and using specific error messages/status)
        if (StringUtils.isBlank(identity)) {
            return buildErrorResponse("Identity header is missing.", HttpStatus.UNAUTHORIZED); // 401
        }
        if (visitRequest.getStartDate() == null || visitRequest.getEndDate() == null) {
            return buildErrorResponse("Start date and End date are required.", HttpStatus.BAD_REQUEST);
        }
        if (visitRequest.getStartDate().before(new Date())) {
            // Note: Use a comparison that ignores time if only day matters, or use ZonedDateTime.
            // Assuming time matters:
            return buildErrorResponse("Visit start date cannot be in the past.", HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeService.getEmployeeByIdentity(identity);
        if (employee == null) {
            log.warn("Attempt to create visit failed: Employee not found for identity: {}", identity);
            return buildErrorResponse("Employee not found.", HttpStatus.NOT_FOUND); // 404
        }

        // 3. Overlap Check Logic (Simplified and corrected)
        if (Boolean.TRUE.equals(force)) {
            List<Visit> existingVisits = visitService.findByEmployeeId(employee);
            if (hasOverlap(existingVisits, visitRequest.getStartDate(), visitRequest.getEndDate())) {
                return buildErrorResponse("Visit dates overlap with an existing visit.", HttpStatus.CONFLICT); // 409 Conflict
            }
        }

        Visit newVisit = visitMapper.convert(visitRequest);
        newVisit.setEmployee(employee);

        Visit savedVisit = visitService.save(newVisit);
        VisitResponse visitResponse = visitMapper.convert(savedVisit, false);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), Constant.VISIT_CREATED, visitResponse),
                HttpStatus.CREATED); // 201 CREATED for POST
    }

    /**
     * Updates an existing Visit.
     */
    @PutMapping
    public ResponseEntity<GenericResponse> updateVisit(
            @RequestBody UpdateVisitRequest visitRequest,
            @RequestHeader("identity") String identity,
            @RequestHeader(value = "force", required = false) Boolean force) {

        if (StringUtils.isBlank(identity) || visitRequest.getVisitId() == null) {
            return buildErrorResponse("Identity or Visit ID is missing.", HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeService.getEmployeeByIdentity(identity);
        if (employee == null) {
            return buildErrorResponse("Employee not found.", HttpStatus.NOT_FOUND); // 404
        }

        Visit needToBeUpdatedVisit = visitService.getVisitById(visitRequest.getVisitId());

        if (needToBeUpdatedVisit == null) {
            return buildErrorResponse("Visit does not exist.", HttpStatus.NOT_FOUND); // 404
        }

        // 4. Authorization Check (Use Optional and proper comparison)
        if (needToBeUpdatedVisit.getEmployee() == null ||
                !needToBeUpdatedVisit.getEmployee().getId().equals(employee.getId())) {
            return buildErrorResponse("You are not authorized to update this visit.", HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        // Date validation for update
        if (visitRequest.getStartDate() != null && visitRequest.getStartDate().before(new Date())) {
            return buildErrorResponse("Updated start date cannot be in the past.", HttpStatus.BAD_REQUEST);
        }
        if (visitRequest.getStartDate() == null && visitRequest.getEndDate() != null && needToBeUpdatedVisit.getStartDate().before(new Date())) {
            // Edge case: If only end date is updated, check if the original start date is still valid
            // Simplified check: rely on business logic to prevent updating past visits unless status is 'completed'
        }

        // 5. Overlap Check (Corrected and ensuring we exclude the visit being updated)
        if (Boolean.TRUE.equals(force)) {
            List<Visit> existingVisits = visitService.findByEmployeeId(employee).stream()
                    .filter(v -> !v.getVisitId().equals(visitRequest.getVisitId())) // Exclude current visit
                    .collect(Collectors.toList());

            // Use the dates from the request, falling back to existing dates if not provided
            Date newStart = visitRequest.getStartDate() != null ? visitRequest.getStartDate() : needToBeUpdatedVisit.getStartDate();
            Date newEnd = visitRequest.getEndDate() != null ? visitRequest.getEndDate() : needToBeUpdatedVisit.getEndDate();

            if (hasOverlap(existingVisits, newStart, newEnd)) {
                return buildErrorResponse("Updated dates overlap with another existing visit.", HttpStatus.CONFLICT); // 409 Conflict
            }
        }

        // Apply updates
        visitMapper.updateVisit(needToBeUpdatedVisit, visitRequest);

        Visit updatedVisit = visitService.save(needToBeUpdatedVisit);
        VisitResponse visitResponse = visitMapper.convert(updatedVisit, false);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), Constant.VISIT_UPDATED, visitResponse),
                HttpStatus.OK);
    }

    /**
     * Gets all visits for the authenticated employee.
     */
    @GetMapping
    public ResponseEntity<GenericResponse> getVisit(@RequestHeader("identity") String identity) {

        if (StringUtils.isBlank(identity)) {
            return buildErrorResponse("Identity header is missing.", HttpStatus.UNAUTHORIZED); // 401
        }

        Employee employee = employeeService.getEmployeeByIdentity(identity);
        if (employee == null) {
            return buildErrorResponse("Employee not found.", HttpStatus.NOT_FOUND); // 404
        }

        List<Visit> visitList = visitService.findByEmployeeId(employee);

        List<VisitResponse> visitResponseList = visitList.stream()
                .map(visit -> visitMapper.convert(visit, false))
                .collect(Collectors.toList());

        // 6. Use a more appropriate success message
        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), Constant.VISITS_FETCHED, visitResponseList),
                HttpStatus.OK);
    }

    /**
     * Utility method to create structured error responses.
     */
    private ResponseEntity<GenericResponse> buildErrorResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.FAIL.name(), message, null),
                status);
    }
}