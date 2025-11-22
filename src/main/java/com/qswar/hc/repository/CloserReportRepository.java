package com.qswar.hc.repository;

import com.qswar.hc.model.CloserReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CloserReport entity.
 */
@Repository
public interface CloserReportRepository extends JpaRepository<CloserReport, Long> {
    // Find a closer report by its associated visit ID
    List<CloserReport> findByVisitVisitId(Long visitId);
}
