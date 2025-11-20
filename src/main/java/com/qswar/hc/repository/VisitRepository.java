package com.qswar.hc.repository;

import com.qswar.hc.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Visit entity.
 */
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

}
