package com.qswar.hc.mapper;


import com.google.gson.Gson;
import com.qswar.hc.model.CloserReport;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.pojos.requests.EmployeeRequest;
import com.qswar.hc.pojos.responses.CloserReportResponse;
import com.qswar.hc.pojos.responses.EmployeeResponse;
import com.qswar.hc.pojos.responses.VisitResponse;
import com.qswar.hc.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.util.Optional;

import java.util.stream.Collectors;

@Component
public class EmployeeMapper {

    private final EmployeeService employeeService;
    private final Gson gson = new Gson(); // Kept for the authorization logic

    public EmployeeMapper(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Gson getGson() {
        return gson;
    }

    /**
     * Converts Employee model to EmployeeResponse DTO, including nested Manager and Subordinates.
     * @param employee The Employee entity.
     * @return The EmployeeResponse DTO.
     */
    public EmployeeResponse convert(Employee employee) {
        if (employee == null) { return null; }

        EmployeeResponse employeeResponse = new EmployeeResponse();

        // Mapping basic fields
        employeeResponse.setGovEmpId(employee.getGovEmpId());
        employeeResponse.setName(employee.getName());
        employeeResponse.setPhone(employee.getPhone());
        employeeResponse.setEmail(employee.getEmail());
        employeeResponse.setPosition(employee.getPosition());
        employeeResponse.setAuthorised(employee.getAuthorised());
        employeeResponse.setDepartment(employee.getDepartment());
        employeeResponse.setUsername(employee.getUsername());
        employeeResponse.setQSwarId(employee.getQSwarId());

        // Handle Manager (avoiding deep recursion by creating a lighter manager DTO)
        if (employee.getManager() != null) {
            // OPTIMIZATION: Instead of full conversion, use a lighter DTO or only ID/Name if possible.
            // For now, retaining the original logic but calling the same mapper method.
            Employee manager = employeeService.getEmployeeByIdentity(employee.getEmail());
            employeeResponse.setManager(convertLight(manager));
        }

        // Handle Subordinates using streams
        employeeResponse.setSubordinates(
                employeeService.findSubordinatesById(employee.getId()).stream()
                        .map(this::convertLight) // Use light conversion for subordinates
                        .collect(Collectors.toList())
        );

        return employeeResponse;
    }

    // A lighter conversion method to prevent deep, possibly recursive calls
    public EmployeeResponse convertLight(Employee employee) {
        if (employee == null) { return null; }

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
        // Do NOT set Manager or Subordinates here

        return employeeResponse;
    }

    /**
     * Converts EmployeeRequest DTO to Employee model.
     */
    public Employee convert(EmployeeRequest employeeRequest) {
        if (employeeRequest == null) { return null; }

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

    /**
     * Converts Visit model to VisitResponse DTO.
     */
    public VisitResponse convert(Visit visit) {
        if (visit == null) { return null; }

        VisitResponse visitResponse = new VisitResponse();
        // Use Optional/conditional setting for clarity/safety if fields can be null
        Optional.ofNullable(visit.getVisitId()).ifPresent(visitResponse::setVisitId);
        Optional.ofNullable(visit.getStartDate()).ifPresent(visitResponse::setStartDate);
        Optional.ofNullable(visit.getEndDate()).ifPresent(visitResponse::setEndDate);
        Optional.ofNullable(visit.getVisitIconLink()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setVisitIconLink);
        Optional.ofNullable(visit.getLocation()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setLocation);
        Optional.ofNullable(visit.getPurpose()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setPurpose);
        Optional.ofNullable(visit.getTitle()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setTitle);
        Optional.ofNullable(visit.getPlace()).filter(StringUtils::isNotBlank).ifPresent(visitResponse::setPlace);

        return visitResponse;
    }

    /**
     * Converts CloserReport model to CloserReportResponse DTO.
     */
    public CloserReportResponse convert(CloserReport closerReport) {
        if (closerReport == null) { return null; }

        CloserReportResponse closerReportResponse = new CloserReportResponse();

        // Fix: Original code had 'closerReport.getCrId() == null' which is incorrect.
        Optional.ofNullable(closerReport.getCrId()).ifPresent(closerReportResponse::setCrId);

        Optional.ofNullable(closerReport.getDocType()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setDocType);
        Optional.ofNullable(closerReport.getDocSubType()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setDocSubType);
        Optional.ofNullable(closerReport.getTitle()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setTitle);

        // Fix: Original code mixed 'getDetails' and 'getDescription'. Assuming you intended to map both.
        Optional.ofNullable(closerReport.getDetails()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setDetails);
        Optional.ofNullable(closerReport.getDescription()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setDescription);

        Optional.ofNullable(closerReport.getDocLink()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setDocLink);
        Optional.ofNullable(closerReport.getCreatedAt()).ifPresent(closerReportResponse::setCreatedAt);
        Optional.ofNullable(closerReport.getUpdatedAt()).ifPresent(closerReportResponse::setUpdatedAt);
        Optional.ofNullable(closerReport.getManagerFeedback()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setManagerFeedback);
        Optional.ofNullable(closerReport.getManagerApproval()).filter(StringUtils::isNotBlank).ifPresent(closerReportResponse::setManagerApproval);

        return closerReportResponse;
    }
}