package com.qswar.hc.repository;

import com.qswar.hc.model.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ItineraryRepository extends JpaRepository<Itinerary, Long> {
    // Example: Find Itineraries by Visit ID
    List<Itinerary> findByVisitVisitId(Long visitId);
}