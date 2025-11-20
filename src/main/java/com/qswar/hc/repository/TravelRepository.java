package com.qswar.hc.repository;

import com.qswar.hc.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Travel entity.
 */
@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    // Find a travel segment by its associated itinerary ID
    Travel findByItineraryItineraryId(Long itineraryId);
}