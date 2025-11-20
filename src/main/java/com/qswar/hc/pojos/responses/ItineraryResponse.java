package com.qswar.hc.pojos.responses;

import com.qswar.hc.model.Hotel;
import com.qswar.hc.model.Travel;
import com.qswar.hc.model.Visit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItineraryResponse {
    @JsonProperty("itineraryId")
    private Long itineraryId;

    @JsonProperty("paymentType")
    private String paymentType;

    @JsonProperty("paymentStatus")
    private String paymentStatus;

    @JsonProperty("itineraryStatus")
    private String itineraryStatus;

    @JsonProperty("managerApproval")
    private String managerApproval;

    @JsonProperty("visit")
    private Visit visit;

    @JsonProperty("hotel")
    private Hotel hotel;

    @JsonProperty("travel")
    private Travel travel;
}
