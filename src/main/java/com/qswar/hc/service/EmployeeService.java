package com.qswar.hc.service;

import com.qswar.hc.model.Employee;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    Employee getEmployeeByIdentity(String identity);

    List<Employee> findAllEmployees();

    Optional<Employee> findById(Integer id);

    Employee saveEmployee(Employee employee);

    Optional<Employee> findManagerById(Integer managerId);

    List<Employee> findSubordinatesById(Integer id);
}