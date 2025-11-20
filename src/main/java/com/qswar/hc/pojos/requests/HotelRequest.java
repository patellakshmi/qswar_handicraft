package com.qswar.hc.pojos.requests;

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
public class HotelRequest {

    @JsonProperty("hotelId")
    private Long hotelId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("checkin")
    private LocalDateTime checkin; // Use LocalDateTime for DATETIME type

    @JsonProperty("checkout")
    private LocalDateTime checkout; // Use LocalDateTime for DATETIME type

    @JsonProperty("category")
    private String category;

    @JsonProperty("totalGuest")
    private Integer totalGuest;

    @JsonProperty("males")
    private Integer males;

    @JsonProperty("females")
    private Integer females;

    @JsonProperty("childs")
    private Integer childs;

    @JsonProperty("cost")
    private BigDecimal cost; // Use BigDecimal for DECIMAL type

    @JsonProperty("bookingId")
    private String bookingId;

    @JsonProperty("bookingLink")
    private String bookingLink;

    @JsonProperty("bookingStatus")
    private String bookingStatus;

    @JsonProperty("managerApproval")
    private String managerApproval;

    @JsonProperty("itinerary")
    private Itinerary itinerary;
}
