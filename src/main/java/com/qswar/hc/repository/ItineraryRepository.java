package com.qswar.hc.repository;

import com.qswar.hc.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for Itinerary entity.
 */
@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    // Find all itineraries associated with a specific visit ID
    List<Itinerary> findByVisitVisitId(Long visitId);
}