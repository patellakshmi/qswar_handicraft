package com.qswar.hc.service;

import com.qswar.hc.model.Employee;
import com.qswar.hc.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for handling Employee-related business logic.
 */
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieves all employees from the database.
     * @return A list of all Employee entities.
     */
    public List<Employee> findAllEmployees() {
        // Simple delegation to the repository for now
        return employeeRepository.findAll();
    }
    /**
     * Retrieves an employee by their ID.
     * @param id The ID of the employee.
     * @return An Optional containing the Employee, or empty if not found.
     */
    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    /**
     * Saves a new Employee or updates an existing one.
     * In a real application, you would add validation and potentially hash the password here.
     * @param employee The Employee entity to save.
     * @return The saved Employee entity.
     */
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}