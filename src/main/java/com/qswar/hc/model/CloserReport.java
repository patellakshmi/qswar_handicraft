package com.qswar.hc.model;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents the 'closer_report' table.
 */
@Entity
@Table(name = "closer_report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CloserReport implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cr_id")
    private Long crId;

    @Column(name = "doc_type", length = 50)
    private String docType;

    @Column(name = "doc_sub_type", length = 50)
    private String docSubType;

    @Column(name = "title", length = 255)
    private String title;

    @Lob // Used for TEXT/BLOB types
    @Column(name = "details")
    private String details;

    @Column(name = "doc_link", length = 255)
    private String docLink;

    @Lob // Used for TEXT/BLOB types
    @Column(name = "description")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // Use @CreationTimestamp for DATETIME DEFAULT CURRENT_TIMESTAMP

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Use @UpdateTimestamp for DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

    @Lob // Used for TEXT/BLOB types
    @Column(name = "manager_feedback")
    private String managerFeedback;

    @Column(name = "manager_approval", length = 50)
    private String managerApproval;

    // --- Relationships ---

    // One closer report belongs to one visit (One-to-One FK: visit_id)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visit_id", unique = true, nullable = false)
    private Visit visit;
}
