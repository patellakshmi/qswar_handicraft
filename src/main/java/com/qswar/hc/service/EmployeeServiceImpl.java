package com.qswar.hc.service;

import com.qswar.hc.model.Employee;
import com.qswar.hc.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for correctness
import java.util.List;
import java.util.Optional;

/**
 * Service layer implementation for handling Employee-related business logic.
 */
@Service // Marks this as a service bean
@Transactional(readOnly = true) // Set default transaction to read-only for efficiency
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    // 1. Constructor Injection is already perfect
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    /**
     * Finds an employee by a unique identifier (username, email, or phone).
     */
    @Override
    public Employee getEmployeeByIdentity(String identity) {
        // Renamed method for clarity on what the identity string represents.
        return employeeRepository.findByAnyOfUniqueField(identity);
    }

    /**
     * Retrieves all employees.
     */
    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Finds an employee by their primary key ID.
     */
    @Override
    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    /**
     * Saves a new employee or updates an existing one.
     */
    @Override
    @Transactional // Override the class-level readOnly flag for write operations
    public Employee saveEmployee(Employee employee) {
        // Business logic (e.g., validation, pre-save processing) would go here.
        return employeeRepository.save(employee);
    }

    /**
     * Finds the manager employee by the manager's ID.
     */
    @Override
    public Optional<Employee> findManagerById(Integer managerId) {
        // 2. Improvement: Repositories typically return Optional for single results.
        // If findManager is expected to return Optional<Employee> in the Repository,
        // this should be refactored. Assuming the repository method returns a single Employee:

        Employee manager = employeeRepository.findManager(managerId);
        return Optional.ofNullable(manager);
        // The return type is changed to Optional<Employee> for safer null handling by consumers.
    }

    /**
     * Finds all direct subordinates of an employee given the employee's ID.
     */
    @Override
    public List<Employee> findSubordinatesById(Integer id) {
        // Renamed method for clearer context (it's the parent employee's ID)
        return employeeRepository.findAllSubordinate(id);
    }
}