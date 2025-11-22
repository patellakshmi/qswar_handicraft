package com.qswar.hc.service;

import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
import com.qswar.hc.repository.EmployeeRepository;
import com.qswar.hc.repository.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisitService {

    private VisitRepository visitRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    public Visit save(Visit visit) {
        return visitRepository.save(visit);
    }

    public List<Visit> findByEmployeeId(Employee  empId) {
        return visitRepository.findByEmployeeId(empId);
    }

    public Visit getVisitById(Long id){
        return visitRepository.getVisitById(id);
    }
}
