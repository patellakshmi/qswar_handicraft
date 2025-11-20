package com.qswar.hc.pojos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Identity {

    @JsonProperty("username")
    private String username;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;
}
