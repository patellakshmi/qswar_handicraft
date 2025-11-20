package com.qswar.hc.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;

/**
 * Represents the 'itinerary' table.
 */
@Entity
@Table(name = "itinerary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Itinerary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "itinerary_id")
    private Long itineraryId;

    @Column(name = "payment_type", length = 50)
    private String paymentType;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "itinerary_status", length = 50)
    private String itineraryStatus;

    @Column(name = "manager_approval", length = 50)
    private String managerApproval;

    // --- Relationships ---

    // Many itineraries belong to one visit (FK: visit_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", nullable = false)
    private Visit visit;

    // One itinerary has one hotel booking (UNIQUE FK: itinerary_id)
    @OneToOne(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Hotel hotel;

    // One itinerary has one travel segment (UNIQUE FK: itinerary_id)
    @OneToOne(mappedBy = "itinerary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Travel travel;
}
