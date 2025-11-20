package com.qswar.hc.pojos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonPropertyOrder({"error_code","desc"})
public class ErrorResponse {
    private String errorCode;
    private String desc;
}
