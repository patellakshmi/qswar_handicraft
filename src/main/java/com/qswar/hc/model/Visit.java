package com.qswar.hc.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Represents the 'visit' table.
 */
@Entity
@Table(name = "visit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Visit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "visit_id")
    private Long visitId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Lob // Used for TEXT/BLOB types
    @Column(name = "purpose")
    private String purpose;

    @Column(name = "place", length = 100)
    private String place;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "from")
    private LocalDate fromDate; // Use LocalDate for DATE type

    @Column(name = "to")
    private LocalDate toDate; // Use LocalDate for DATE type

    @Column(name = "visit_status", length = 50)
    private String visitStatus;

    @Column(name = "manager_approval", length = 50)
    private String managerApproval;

    @Column(name = "visit_icon_link", nullable = true, length = 512)
    private String visitIconLink;
    // --- Relationships ---

    // Many visits belong to one employee (FK: emp_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private Employee employee;

    // One visit can have many itineraries
    @OneToMany(mappedBy = "visit", cascade = CascadeType.ALL)
    private List<Itinerary> itineraries;

    // One visit has one closer report (UNIQUE FK: visit_id)
    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true)
    private CloserReport closerReport;
}
