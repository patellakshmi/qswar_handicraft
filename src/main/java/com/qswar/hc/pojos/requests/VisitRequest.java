package com.qswar.hc.pojos.requests;

import com.qswar.hc.model.CloserReport;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Itinerary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VisitRequest {

    @JsonProperty("visitId")
    private Long visitId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("purpose")
    private String purpose;

    @JsonProperty("place")
    private String place;

    @JsonProperty("location")
    private String location;

    @JsonProperty("fromDate")
    private LocalDate fromDate; // Use LocalDate for DATE type

    @JsonProperty("toDate")
    private LocalDate toDate; // Use LocalDate for DATE type

    @JsonProperty("visitStatus")
    private String visitStatus;

    @JsonProperty("managerApproval")
    private String managerApproval;

    @JsonProperty("managerApproval")
    private String visitIconLink;

    @JsonProperty("employee")
    private Employee employee;

    @JsonProperty("itineraries")
    private List<Itinerary> itineraries;

    // One visit has one closer report (UNIQUE FK: visit_id)
    @JsonProperty("closerReport")
    private CloserReport closerReport;
}
