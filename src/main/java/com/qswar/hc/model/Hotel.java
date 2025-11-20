package com.qswar.hc.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents the 'hotel' table.
 */
@Entity
@Table(name = "hotel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long hotelId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "checkin")
    private LocalDateTime checkin; // Use LocalDateTime for DATETIME type

    @Column(name = "checkout")
    private LocalDateTime checkout; // Use LocalDateTime for DATETIME type

    @Column(name = "category", length = 50)
    private String category;

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

    // One hotel booking belongs to one itinerary (One-to-One FK: itinerary_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itinerary_id", unique = true, nullable = false)
    private Itinerary itinerary;
}