package com.qswar.hc.pojos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({"id","name","description","imageUrl","position","enable"})
public class ObjectiveResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("objective")
    private String objective;

    @JsonProperty("enable")
    private boolean enable;
}