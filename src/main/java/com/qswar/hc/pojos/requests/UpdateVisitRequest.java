package com.qswar.hc.pojos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.sql.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateVisitRequest {

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

    @JsonProperty("visitIconLink")
    private String visitIconLink;

    @JsonProperty("visitStatus")
    private String visitStatus;

}
