package com.qswar.hc.pojos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import javax.persistence.Column;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({"id","name","fee","currency","off","offKeyword","streamStd","duration","durationUnit",
"mode","imageUrl","description","benefit","enable"})
public class CourseRequest {

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("head")
    private String head;

    @JsonProperty("headEmail")
    private String headEmail;

    @JsonProperty("headPhone")
    private String headPhone;

    @JsonProperty("fee")
    private Double fee;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("off")
    private Double off;

    @JsonProperty("offKeyword")
    private String offKeyword;

    @JsonProperty("offMode")
    private String offMode;

    @JsonProperty("streamStd")
    private String streamStd;

    @JsonProperty("duration")
    private Long duration;

    @JsonProperty("durationUnit")
    private String durationUnit;

    @JsonProperty("mode")
    private String mode;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @JsonProperty("description")
    private String description;

    @JsonProperty("benefit")
    private String benefit;

    @JsonProperty("enable")
    private boolean enable;
}
