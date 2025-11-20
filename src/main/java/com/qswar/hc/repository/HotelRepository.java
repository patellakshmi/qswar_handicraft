package com.qswar.hc.repository;


import com.qswar.hc.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    // Since Hotel is One-to-One with Itinerary, finding by Itinerary ID is common
    Hotel findByItineraryItineraryId(Long itineraryId);
}