package com.qswar.hc.pojos.responses;

import com.qswar.hc.model.CloserReport;
import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Itinerary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class VisitResponse {

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

    @JsonProperty("startDate")
    private Date startDate; // Use LocalDate for DATE type

    @JsonProperty("endDate")
    private Date endDate; // Use LocalDate for DATE type

    @JsonProperty("visitStatus")
    private String visitStatus;

    @JsonProperty("managerApproval")
    private String managerApproval;

    @JsonProperty("managerApproval")
    private String visitIconLink;

    @JsonProperty("employee")
    private EmployeeResponse employee;

    @JsonProperty("itineraries")
    private List<Itinerary> itineraries;

    // One visit has one closer report (UNIQUE FK: visit_id)
    @JsonProperty("closerReport")
    private CloserReportResponse closerReport;
}
