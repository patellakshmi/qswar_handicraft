package com.qswar.hc.pojos.responses;

import com.qswar.hc.pojos.common.Identity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class IdentitiesResponse {
    @JsonProperty("description")
    String description = "Existing Identities";

    @JsonProperty("identities")
    List<Identity> identities;
}
