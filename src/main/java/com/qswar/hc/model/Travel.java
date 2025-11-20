package com.qswar.hc.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the 'travel' table.
 */
@Entity
@Table(name = "travel")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long travelId;

    @Column(name = "travel_type", length = 50)
    private String travelType;

    @Column(name = "from", length = 100)
    private String fromLocation; // Renamed to avoid reserved keyword

    @Column(name = "to", length = 100)
    private String toLocation; // Renamed to avoid reserved keyword

    @Column(name = "start")
    private LocalDateTime start; // Use LocalDateTime for DATETIME type

    @Column(name = "end")
    private LocalDateTime end; // Use LocalDateTime for DATETIME type

    @Column(name = "total_guest")
    private Integer totalGuest;

    @Column(name = "males")
    private Integer males;

    @Column(name = "females")
    private Integer females;

    @Column(name = "childs")
    private Integer childs;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost; // Use BigDecimal for DECIMAL type

    @Column(name = "booking_id", length = 50)
    private String bookingId;

    @Column(name = "booking_link", length = 255)
    private String bookingLink;

    @Column(name = "booking_status", length = 50)
    private String bookingStatus;

    @Column(name = "manager_approval", length = 50)
    private String managerApproval;

    // --- Relationships ---
    @ToString.Exclude
    // One travel segment belongs to one itinerary (One-to-One FK: itinerary_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", unique = true, nullable = false)
    private Itinerary itinerary;
}