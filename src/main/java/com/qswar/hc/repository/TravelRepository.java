package com.qswar.hc.repository;

import com.qswar.hc.model.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    // Since Travel is One-to-One with Itinerary, finding by Itinerary ID is common
    Travel findByItineraryItineraryId(Long itineraryId);
}