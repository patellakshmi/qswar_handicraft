package com.qswar.hc.pojos.responses;

import com.qswar.hc.model.Itinerary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TravelResponse {

    @JsonProperty("travelId")
    private Long travelId;

    @JsonProperty("travelId")
    private String travelType;

    @JsonProperty("travelId")
    private String fromLocation; // Renamed to avoid reserved keyword

    @JsonProperty("travelId")
    private String toLocation; // Renamed to avoid reserved keyword

    @JsonProperty("travelId")
    private LocalDateTime start; // Use LocalDateTime for DATETIME type

    @JsonProperty("travelId")
    private LocalDateTime end; // Use LocalDateTime for DATETIME type

    @JsonProperty("travelId")
    private Integer totalGuest;

    @JsonProperty("travelId")
    private Integer males;

    @JsonProperty("travelId")
    private Integer females;

    @JsonProperty("travelId")
    private Integer childs;

    @JsonProperty("travelId")
    private BigDecimal cost; // Use BigDecimal for DECIMAL type

    @JsonProperty("travelId")
    private String bookingId;

    @JsonProperty("travelId")
    private String bookingLink;

    @JsonProperty("travelId")
    private String bookingStatus;

    @JsonProperty("travelId")
    private String managerApproval;

    @JsonProperty("travelId")
    private Itinerary itinerary;
}
