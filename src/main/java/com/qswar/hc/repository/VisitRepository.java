package com.qswar.hc.repository;

import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Query("select u from Visit u where u.employee = ?1")
    List<Visit> findByEmployeeId(Employee employee);

    @Query("select u from Visit u where u.visitId = ?1")
    Visit getVisitById(Long visitId);

}