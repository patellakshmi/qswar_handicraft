package com.qswar.hc.config.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.qswar.hc.config.header.HeaderModifyingRequestWrapper;
import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.pojos.common.AuthDetail;
import com.qswar.hc.utility.EmailValidator;
import com.qswar.hc.utility.PhoneNumberValidator;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final Gson gson = new Gson();
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        this.authenticationManager = authenticationManager;
        setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(APIConstant.LOGIN,"POST", false));
        setFilterProcessesUrl(APIConstant.LOGIN);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthUser authUser = new ObjectMapper().readValue(request.getInputStream(), AuthUser.class);

            String username = authUser.getUsername();
            boolean phoneStatus = PhoneNumberValidator.isValidPhoneNumber(authUser.getPhone(),"IN");
            if(phoneStatus)
                username = authUser.getPhone();

            if(EmailValidator.isValid(authUser.getEmail()))
                username = authUser.getEmail();

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, authUser.getPassword(), new ArrayList<>()));

            setCorsHeader(response);
            return authentication;
        } catch (IOException e) {
            throw new RuntimeException("Could not read request" + e);
        }
    }

    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException {
        String token = Jwts.builder()
                .setSubject(((User) authentication.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_000))
                .signWith(SignatureAlgorithm.HS512, SecurityConfigConst.SECRETE_KEY.getBytes())
                .compact();
        response.addHeader(SecurityConfigConst.F4E_AUTH, "" + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        AuthDetail authDetail = AuthDetail.builder().hcAuth(token).status("SUCCESS").message("Login successfully").build();
        String jsonString = this.gson.toJson(authDetail);
        response.getWriter().write(jsonString);
    }

    void setCorsHeader(HttpServletResponse response){
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers",
                "content-type, x-gwt-module-base, x-gwt-permutation, clientid, longpush");

    }

}
