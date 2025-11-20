package com.qswar.hc.pojos.requests;

import com.qswar.hc.model.Employee;
import com.qswar.hc.model.Visit;
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
public class EmployeeRequest {

    @JsonProperty("qSwarId")
    private String qSwarId;

    @JsonProperty("govEmpId")
    private String govEmpId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("position")
    private String position;

    @JsonProperty("authorised")
    private Boolean authorised = true; // Maps to BOOLEAN DEFAULT TRUE

    @JsonProperty("department")
    private String department;

    @JsonProperty("userPhotoLink")
    private String userPhotoLink;

    @JsonProperty("manager")
    private Employee manager;

    @JsonProperty("subordinates")
    private List<Employee> subordinates;

    @JsonProperty("visits")
    private List<Visit> visits;
}
