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

    public Employee getEmployee(String identity) {
        return employeeRepository.findByAnyOfUniqueField(identity);
    }

    public List<Employee> findAllEmployees() {
        // Simple delegation to the repository for now
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(Integer id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee findManager(Integer managerId) {
        return employeeRepository.findManager(managerId);
    }

    public List<Employee> findSubordinate(Integer id) {
        return employeeRepository.findAllSubordinate(id);
    }
}