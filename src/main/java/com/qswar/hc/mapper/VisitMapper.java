package com.qswar.hc.mapper;

import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.UpdateVisitRequest;
import com.qswar.hc.pojos.requests.VisitRequest;
import com.qswar.hc.pojos.responses.EmployeeResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VisitMapper {

    // --- Employee Conversion (Keep simple to prevent deep nesting) ---

    public EmployeeResponse convert(Employee employee) {
        if (employee == null) return null;
        EmployeeResponse employeeResponse = new EmployeeResponse();

        // Fix: Original code used employeeResponse.getEmail() for setting email.
        Optional.ofNullable(employee.getEmail()).filter(StringUtils::isNotBlank).ifPresent(employeeResponse::setEmail);
        Optional.ofNullable(employee.getQSwarId()).filter(StringUtils::isNotBlank).ifPresent(employeeResponse::setQSwarId);
        Optional.ofNullable(employee.getGovEmpId()).filter(StringUtils::isNotBlank).ifPresent(employeeResponse::setGovEmpId);
        Optional.ofNullable(employee.getUsername()).filter(StringUtils::isNotBlank).ifPresent(employeeResponse::setUsername);
        Optional.ofNullable(employee.getPhone()).filter(StringUtils::isNotBlank).ifPresent(employeeResponse::setPhone);

        // Ensure nesting is explicitly null for this simple conversion
        employeeResponse.setSubordinates(null);
        employeeResponse.setManager(null);
        employeeResponse.setVisits(null);
        return employeeResponse;
    }

    // --- Visit DTO to Model Conversion ---

    public Visit convert(VisitRequest visitRequest) {
        if (visitRequest == null) return null;

        Visit visit = new Visit();

        // Use Optional/Filter for clean, null-safe assignment
        Optional.ofNullable(visitRequest.getTitle()).filter(StringUtils::isNotBlank).ifPresent(visit::setTitle);
        Optional.ofNullable(visitRequest.getPurpose()).filter(StringUtils::isNotBlank).ifPresent(visit::setPurpose);
        Optional.ofNullable(visitRequest.getPlace()).filter(StringUtils::isNotBlank).ifPresent(visit::setPlace);
        Optional.ofNullable(visitRequest.getLocation()).filter(StringUtils::isNotBlank).ifPresent(visit::setLocation);
        Optional.ofNullable(visitRequest.getStartDate()).ifPresent(visit::setStartDate);
        Optional.ofNullable(visitRequest.getEndDate()).ifPresent(visit::setEndDate);
        Optional.ofNullable(visitRequest.getVisitIconLink()).filter(StringUtils::isNotBlank).ifPresent(visit::setVisitIconLink);

        return visit;
    }

    // --- Visit Model to Response DTO Conversion ---

    public VisitResponse convert(Visit visit, boolean includeEmployee) {
        if (visit == null) return null;

        VisitResponse visitResponse = new VisitResponse();

        Optional.ofNullable(visit.getVisitId()).ifPresent(visitResponse::setVisitId);
        Optional.ofNullable(visit.getTitle()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setTitle);
        Optional.ofNullable(visit.getPurpose()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setPurpose);
        Optional.ofNullable(visit.getPlace()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setPlace);
        Optional.ofNullable(visit.getLocation()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setLocation);
        Optional.ofNullable(visit.getStartDate()).ifPresent(visitResponse::setStartDate);
        Optional.ofNullable(visit.getEndDate()).ifPresent(visitResponse::setEndDate);
        Optional.ofNullable(visit.getVisitIconLink()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setVisitIconLink);
        Optional.ofNullable(visit.getVisitStatus()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setVisitStatus);

        if (includeEmployee) {
            Optional.ofNullable(visit.getEmployee()).ifPresent(emp -> visitResponse.setEmployee(convert(emp)));
        }

        return visitResponse;
    }

    public VisitResponse convert(Visit visit){
        return convert(visit, true);
    }

    // --- Visit Update Logic ---

    /**
     * Updates an existing Visit entity with non-blank/non-null fields from the request.
     */
    public void updateVisit(Visit needToBeUpdatedVisit, UpdateVisitRequest updateVisitRequest) {

        // Date update logic (checking if dates are truly different)
        Optional.ofNullable(updateVisitRequest.getStartDate())
                .filter(newDate -> !DateUtils.isSameDay(newDate, needToBeUpdatedVisit.getStartDate()))
                .ifPresent(needToBeUpdatedVisit::setStartDate);

        Optional.ofNullable(updateVisitRequest.getEndDate())
                .filter(newDate -> !DateUtils.isSameDay(newDate, needToBeUpdatedVisit.getEndDate()))
                .ifPresent(needToBeUpdatedVisit::setEndDate);

        // String update logic (checking if not blank/empty)
        Optional.ofNullable(updateVisitRequest.getTitle()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setTitle);
        Optional.ofNullable(updateVisitRequest.getPurpose()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setPurpose);
        Optional.ofNullable(updateVisitRequest.getLocation()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setLocation);
        Optional.ofNullable(updateVisitRequest.getPlace()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setPlace);
        Optional.ofNullable(updateVisitRequest.getVisitIconLink()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setVisitIconLink);
        Optional.ofNullable(updateVisitRequest.getVisitStatus()).filter(StringUtils::isNotBlank).ifPresent(needToBeUpdatedVisit::setVisitStatus);
    }
}