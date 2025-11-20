package com.qswar.hc.utility;

import com.qswar.hc.config.helper.AuthUser;
import com.qswar.hc.config.helper.SecurityConfigConst;
import com.qswar.hc.model.Employee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.codehaus.jettison.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AuthUtility {

    public static void aadAuthHeader(HttpServletRequest request, HttpServletResponse response, Employee employee) throws IOException, ServletException, JSONException {
        String token = Jwts.builder()
                .setSubject(employee.getUsername())
                .claim("qswarid", employee.getQSwarId())

                // Custom Claim 3: Department ID
                .claim("email", employee.getEmail())
                .claim("phone", employee.getPhone())
                .claim("username", employee.getUsername())

                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, SecurityConfigConst.SECRETE_KEY.getBytes())
                .compact();
        response.addHeader(SecurityConfigConst.F4E_AUTH, "" + token);
    }

}
