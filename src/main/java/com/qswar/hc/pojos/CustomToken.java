package com.qswar.hc.pojos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.security.Timestamp;


@Builder
@Data
public class CustomToken implements Serializable {
    private String sub;
    private String qswarid;
    private String email;
    private String phone;
    private String username;
    private Timestamp exp;

    public CustomToken() {
    }

    public CustomToken(String sub, String qswarid, String email, String phone, String username, Timestamp exp) {
        this.sub = sub;
        this.qswarid = qswarid;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.exp = exp;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getQswarid() {
        return qswarid;
    }

    public void setQswarid(String qswarid) {
        this.qswarid = qswarid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getExp() {
        return exp;
    }

    public void setExp(Timestamp exp) {
        this.exp = exp;
    }
}

