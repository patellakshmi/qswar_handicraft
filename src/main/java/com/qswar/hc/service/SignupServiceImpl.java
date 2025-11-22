package com.qswar.hc.service;

import com.qswar.hc.config.helper.AuthUser;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.repository.EmployeeRepository;
import com.qswar.hc.utility.AuthUtility;
import com.qswar.hc.utility.IdGenerator;
import com.qswar.hc.utility.SignupValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class SignupServiceImpl implements SignupService {

    private static final Logger log = LoggerFactory.getLogger(SignupServiceImpl.class);

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator; // Assuming IdGenerator can be injected

    // 1. Constructor Injection (preferred)
    public SignupServiceImpl(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, IdGenerator idGenerator) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }


    /**
     * Registers a new user (Employee).
     */
    @Override
    public ResponseEntity<GenericResponse> signupUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!SignupValidator.isValidSignupDetail(authUser)) {
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.FAIL.name(), "User & Pass must contains more than 4 chars"),
                    HttpStatus.BAD_REQUEST);
        }

        // Check for existing user by username/email/phone (assuming findByAnyOfUniqueField handles this)
        Employee existingEmployee = employeeRepository.findByAnyOfUniqueField(authUser.getUsername());
        if (existingEmployee != null) {
            // 2. Use 409 Conflict if the resource already exists
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.FAIL.name(), "A user with the provided credentials already exists."),
                    HttpStatus.CONFLICT);
        }

        // 3. Efficient Unique ID Generation (moved logic to a helper, or kept clean here)
        String uniqueId;
        do {
            uniqueId = idGenerator.getUniqueId(); // Assuming IdGenerator creates a unique ID string
        } while (employeeRepository.findByQSwarId(uniqueId) != null);


        // Encode password *before* creating the Employee object
        String encodedPassword = this.passwordEncoder.encode(authUser.getPassword());

        Employee newEmployee = new Employee(uniqueId, authUser.getUsername(), authUser.getPhone(), authUser.getEmail(), encodedPassword);

        // Note: You should set other essential fields on the Employee here (e.g., initial roles/authorities)

        employeeRepository.save(newEmployee);

        // Add authentication header and return success
        AuthUtility.aadAuthHeader(request, response, newEmployee);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), "Thanks for signing up!"),
                HttpStatus.CREATED); // 201 Created is better for a successful signup/creation
    }

    /**
     * Authenticates and logs in a user (Employee).
     */
    @Override
    public ResponseEntity<GenericResponse> loginUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (!SignupValidator.isValidSignupDetail(authUser)) {
            // 4. Return BAD_REQUEST for invalid input format/length
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.FAIL.name(), "User & Pass must contains more than 4 chars"),
                    HttpStatus.BAD_REQUEST);
        }

        Employee employee = employeeRepository.findByAnyOfUniqueField(authUser.getUsername());

        if (employee == null) {
            // Employee not found
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.FAIL.name(), "Invalid username or password."),
                    HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // 5. CRITICAL FIX: Use passwordEncoder.matches() for secure password verification!
        // Never compare encoded passwords directly or re-encode the request password.
        if (!passwordEncoder.matches(authUser.getPassword(), employee.getPassword())) {
            // Password mismatch
            log.warn("Login failed for user {}: Password mismatch.", authUser.getUsername());
            return new ResponseEntity<>(
                    new GenericResponse(Constant.STATUS.FAIL.name(), "Invalid username or password."),
                    HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        }

        // Authentication successful
        AuthUtility.aadAuthHeader(request, response, employee);

        return new ResponseEntity<>(
                new GenericResponse(Constant.STATUS.SUCCESS.name(), "Login successful!"),
                HttpStatus.OK);
    }
}