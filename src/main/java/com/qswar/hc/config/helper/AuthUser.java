package com.qswar.hc.config.helper;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.qswar.hc.model.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"username", "phone", "email", "password"})
public class AuthUser {

    private String username;
    private String phone;
    private String email;
    private String password;

    public static AuthUser getUser(String username, String phone, String email, Employee employee){
        if(employee != null)
            return AuthUser.builder().username(username).phone(phone).email(email).password(employee.getPassword()).build();
        return null;
    }

    public static AuthUser getUser(String username, Employee employee){
        if(employee != null)
            return AuthUser.builder().username(username).phone(employee.getPhone()).email(employee.getEmail()).password(employee.getPassword()).build();
        return null;
    }


}
