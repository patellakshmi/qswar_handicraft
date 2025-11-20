package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.service.EmployeeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * REST Controller for managing Employee resources.
 * Handles API requests related to employees.
 */
@RestController
public class EmployeeController {

    private static final Log log = LogFactory.getLog(EmployeeController.class);
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * GET /api/v1/employees
     * Retrieves a list of all employees.
     * @return ResponseEntity containing a list of Employee objects.
     */
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
    /**
     * POST /api/v1/employees
     * Creates a new employee.
     * @param employee The employee object from the request body.
     * @return ResponseEntity with the created Employee object and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        // Assuming the client provides a unique emp_id as per the schema, or the database handles it.
        // In a production app, more thorough validation and security (like password hashing) would be added here.

        Employee savedEmployee = employeeService.saveEmployee(employee);

        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    /**
     * PUT /api/v1/employees/{id}
     * Fully updates an existing employee by ID.
     * @param id The ID of the employee to update.
     * @param employeeDetails The new employee details from the request body.
     * @return ResponseEntity with the updated Employee object and HTTP status 200 (OK), or 404 (Not Found).
     */
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
}