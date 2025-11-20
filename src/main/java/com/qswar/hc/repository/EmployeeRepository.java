package com.qswar.hc.repository;

import com.qswar.hc.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Repository for Employee entity, providing standard CRUD operations.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select u from Employee u where u.qSwarId = ?1")
    Employee findByQSwarId(String value);


    @Query("select u from Employee u where u.qSwarId = ?1 or u.email = ?1 or u.phone = ?1 or u.username = ?1")
    Employee findByAnyOfUniqueField(String value);

}