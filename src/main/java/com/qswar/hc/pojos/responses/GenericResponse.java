package com.qswar.hc.pojos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Builder
@JsonPropertyOrder({ "status", "message", "data"})
public class GenericResponse implements Serializable {

    @JsonProperty("status")
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("message")
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    private Object data;

    public GenericResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public GenericResponse(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}

