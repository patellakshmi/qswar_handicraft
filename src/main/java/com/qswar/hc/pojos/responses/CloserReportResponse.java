package com.qswar.hc.pojos.responses;

import com.qswar.hc.model.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CloserReportResponse {

    @JsonProperty("crId")
    private Long crId;

    @JsonProperty("docType")
    private String docType;

    @JsonProperty("docSubType")
    private String docSubType;

    @JsonProperty("title")
    private String title;

    @JsonProperty("details")
    private String details;

    @JsonProperty("docLink")
    private String docLink;

    @JsonProperty("description")
    private String description;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt; // Use @CreationTimestamp for DATETIME DEFAULT CURRENT_TIMESTAMP

    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt; // Use @UpdateTimestamp for DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP

    @JsonProperty("managerFeedback")
    private String managerFeedback;

    @JsonProperty("managerApproval")
    private String managerApproval;

    @JsonProperty("visit")
    private Visit visit;
}
