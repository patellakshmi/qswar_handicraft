package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.pojos.common.Identity;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.IdentitiesResponse;
import com.qswar.hc.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(APIConstant.PUBLIC + APIConstant.VERSION_1 + APIConstant.EXISTING + APIConstant.IDENTITY)
public class IdentityLookupController {

    private static final Logger log = LoggerFactory.getLogger(IdentityLookupController.class);

    private final EmployeeService employeeService;

    // 1. Constructor Injection is clean and preferred
    public IdentityLookupController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Retrieves the email, phone, and username for all existing employees.
     * Maps to: /public/v1/existing/identity
     */
    @GetMapping
    public ResponseEntity<GenericResponse> getAllExitingIds() {

        log.info("Request received to fetch all existing employee identities.");

        List<Employee> listEmployee = employeeService.findAllEmployees();

        if (CollectionUtils.isEmpty(listEmployee)) {
            // 2. Use 204 No Content for successful, but empty, retrieval.
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.SUCCESS.name(),
                            Constant.NO_EMPLOYEE_FOUND, null), // Use a more appropriate constant
                    HttpStatus.NO_CONTENT);
        }

        // 3. Use Java 8 Streams for efficient and beautiful mapping
        List<Identity> identities = listEmployee.stream()
                .map(employee -> Identity.builder()
                        .email(employee.getEmail())
                        .phone(employee.getPhone())
                        .username(employee.getUsername())
                        .build())
                .collect(Collectors.toList());

        IdentitiesResponse identitiesResponse = IdentitiesResponse.builder()
                .identities(identities)
                .build();

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(),
                        Constant.GET_ALL_IDS, identitiesResponse),
                HttpStatus.OK);
    }
}