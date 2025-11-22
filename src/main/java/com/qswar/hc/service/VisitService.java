package com.qswar.hc.service;

import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;

import java.util.List;
import java.util.Optional;

public interface VisitService {

    Visit save(Visit visit);

    List<Visit> findByEmployee(Employee employee);

    Optional<Visit> getVisitById(Long id);
}