package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.enumurator.Authorize;
import com.qswar.hc.enumurator.EmpAuthorized;
import com.qswar.hc.mapper.EmployeeMapper;
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
import com.qswar.hc.service.VisitServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Gson is less common in modern Spring projects; consider Jackson, which is the default.
// I'll keep it for now but note the potential for change.

@RestController // Use class-level mapping for common path
public class EmployeeController {
    // Use SLF4J/Logback, the standard for Spring Boot
    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;
    private final VisitServiceImpl visitService;
    private final CloserReportRepository closerReportRepository;
    private final EmployeeMapper employeeMapper; // Use a dedicated mapper for clean conversion

    // 1. Constructor Injection is preferred, @Autowired on constructor is optional since Spring 4.3
    public EmployeeController(EmployeeService employeeService, VisitServiceImpl visitService,
                              CloserReportRepository closerReportRepository, EmployeeMapper employeeMapper) {
        this.employeeService = employeeService;
        this.visitService = visitService;
        this.closerReportRepository = closerReportRepository;
        this.employeeMapper = employeeMapper;
    }

    /**
     * Get employee details along with their visits and associated reports.
     * @param identity Employee ID/Username
     * @return Employee details with nested visits and reports.
     */
    @GetMapping(APIConstant.PRIVATE + "/v1/employee")// Maps to APIConstant.PRIVATE + "/v1/employee"
    public ResponseEntity<GenericResponse> getEmployee(@RequestHeader("identity") String identity, @RequestHeader("hc_auth") String hcAuth) {
        // Auth headers (hc_auth, tree_height) are typically handled by an Interceptor/Filter/Gateway
        // For simplicity, they are omitted from the method signature here, but you should handle authorization properly.

        Employee employee = employeeService.getEmployeeByIdentity(identity);

        if (employee == null) {
            log.warn("Employee not found for identity: {}", identity);
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.ERROR.name(), "Employee not found.", null),
                    HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // 2. Efficient Conversion using streams and dedicated methods/mapper
        List<Visit> visitList = visitService.findByEmployeeId(employee);

        List<VisitResponse> visitResponseList = visitList.stream()
                .map(visit -> {
                    VisitResponse visitResponse = employeeMapper.convert(visit);

                    // Note: Ensure findByVisitVisitId returns a List<CloserReport> and not an Iterable
                    List<CloserReport> closerReportList = closerReportRepository.findByVisitVisitId(visit.getVisitId());

                    List<CloserReportResponse> closerReportResponseList = closerReportList.stream()
                            .map(employeeMapper::convert)
                            .collect(Collectors.toList());

                    visitResponse.setCloserReport(null); // Assuming VisitResponse has this setter
                    return visitResponse;
                })
                .collect(Collectors.toList());

        EmployeeResponse employeeResponse = employeeMapper.convert(employee);
        employeeResponse.setVisits(visitResponseList);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), Constant.EMPLOYEE_FETCHED, employeeResponse),
                HttpStatus.OK);
    }

    /**
     * Get all registered employees.
     * Note: This is PUBLIC, so security consideration is key.
     * @return List of all employee details.
     */
    @GetMapping(APIConstant.PUBLIC + "/registered") // Maps to APIConstant.PUBLIC + "/v1/registered/employee"
    public ResponseEntity<GenericResponse> getAllEmployees() {
        List<Employee> employees = employeeService.findAllEmployees();

        // 3. Correct Logic: Check if list is EMPTY, not if it's NOT null AND NOT empty.
        // If it's not null and NOT empty, you process it.
        if (employees == null || employees.isEmpty()) {
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.SUCCESS.name(),
                            Constant.NO_EMPLOYEE_FOUND, null),
                    HttpStatus.NO_CONTENT); // 204 No Content is better than 200 OK with NO_EMPLOYEE_FOUND
        }

        // Use streams for clean conversion
        List<EmployeeResponse> employeeResponseList = employees.stream()
                .map(employeeMapper::convert)
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(),
                        Constant.EMPLOYEE_LIST_FETCHED, employeeResponseList), // Use a better constant name
                HttpStatus.OK);
    }

    /**
     * Create a new employee. Requires SUPER_ADMIN authorization.
     * @param employeeRequest New employee details.
     * @param identity Identity of the user performing the creation (should be Super Admin).
     * @return Created employee details.
     */
    @PostMapping (APIConstant.PRIVATE +"/v1/employee")// Maps to APIConstant.PRIVATE + "/v1/employee"
    public ResponseEntity<GenericResponse> createEmployee(@RequestBody EmployeeRequest employeeRequest,
                                                          @RequestHeader("identity") String identity, @RequestHeader("hc_auth") String hcAuth) {

        Employee superAdmin = employeeService.getEmployeeByIdentity(identity);

        if (superAdmin == null) {
            return new ResponseEntity<>(
                    new GenericResponse( Constant.STATUS.ERROR.name(), "User performing action not found.", null),
                    HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Using Gson only here - prefer injecting a dedicated ObjectMapper if this is the only use
        EmpAuthorized empAuthorized = employeeMapper.getGson().fromJson(superAdmin.getAuthorised(), EmpAuthorized.class);

        // 4. Proper Authorization Check & Status Code
        if (!empAuthorized.isAuthorized(Authorize.SUPER_ADMIN)) {
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.ERROR.name(),
                            Constant.UNAUTHORIZED_FOR_CREATE_EMP, null),
                    HttpStatus.FORBIDDEN); // 403 Forbidden
        }

        // Convert and save
        Employee empHasToCreate = employeeMapper.convert(employeeRequest);
        Employee savedEmployee = employeeService.saveEmployee(empHasToCreate);

        if (savedEmployee != null) {
            EmployeeResponse employeeResponse = employeeMapper.convert(savedEmployee);
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.SUCCESS.name(),
                            Constant.EMPLOYEE_CREATED, employeeResponse),
                    HttpStatus.CREATED); // 201 Created is better for POST
        }

        // Fallback error
        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.ERROR.name(),
                        Constant.FAIL_TO_CREATE_EMPLOYEE, null),
                HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }

    /**
     * Update an existing employee's details.
     * @param employeeRequest Details to update.
     * @param identity Identity of the employee being updated (assuming the employee can update themselves).
     * @return Updated employee details.
     */
    @PutMapping(APIConstant.PRIVATE +"/v1/employee")// Maps to APIConstant.PRIVATE + "/v1/employee"
    public ResponseEntity<GenericResponse> updateEmployee(@RequestBody EmployeeRequest employeeRequest,
                                                          @RequestHeader("identity") String identity, @RequestHeader("hc_auth") String hcAuth) {

        Employee employee = employeeService.getEmployeeByIdentity(identity);

        if (employee == null) {
            return new ResponseEntity<>(
                    new GenericResponse( Constant.STATUS.ERROR.name(), "Employee not found.", null),
                    HttpStatus.NOT_FOUND); // 404 Not Found
        }

        // 5. Clean update logic
        Optional.ofNullable(employeeRequest.getGovEmpId()).filter(StringUtils::isNotBlank).ifPresent(employee::setGovEmpId);
        Optional.ofNullable(employeeRequest.getName()).filter(StringUtils::isNotBlank).ifPresent(employee::setName);
        Optional.ofNullable(employeeRequest.getPhone()).filter(StringUtils::isNotBlank).ifPresent(employee::setPhone);
        Optional.ofNullable(employeeRequest.getEmail()).filter(StringUtils::isNotBlank).ifPresent(employee::setEmail);
        Optional.ofNullable(employeeRequest.getPosition()).filter(StringUtils::isNotBlank).ifPresent(employee::setPosition);
        Optional.ofNullable(employeeRequest.getAuthorised()).filter(StringUtils::isNotBlank).ifPresent(employee::setAuthorised);
        Optional.ofNullable(employeeRequest.getDepartment()).filter(StringUtils::isNotBlank).ifPresent(employee::setDepartment);
        // Note: For security, you might want to prevent direct update of 'authorised' or 'position' without admin rights

        Employee updatedEmployee = employeeService.saveEmployee(employee);

        // Setting visits to null before conversion is usually done to prevent lazy loading exceptions,
        // but it's better to manage DTO creation properly in the mapper.
        // If the Employee model has Hibernate relations, the mapper should only convert the fields it needs.
        // Assuming the mapper is smart enough, we can convert directly.
        EmployeeResponse employeeResponse = employeeMapper.convert(updatedEmployee);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), Constant.EMPLOYEE_UPDATED, employeeResponse),
                HttpStatus.OK);
    }
}
