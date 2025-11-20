package com.qswar.hc.config.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CorsFilter extends OncePerRequestFilter {

    @Override
    public void destroy() {

    }

    private String getAllowedDomainsRegex() {
        return "individual / customized Regex";
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        setCorsHeader(response);
        filterChain.doFilter(request, response);

    }

    void setCorsHeader(HttpServletResponse response){
        final String origin = "http://www.fight4edu.com";
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers",
                "content-type, x-gwt-module-base, x-gwt-permutation, clientid, longpush");

    }
}
