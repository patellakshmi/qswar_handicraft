package com.qswar.hc.repository;


import com.qswar.hc.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Hotel entity.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Find a hotel booking by its associated itinerary ID
    Hotel findByItineraryItineraryId(Long itineraryId);
}