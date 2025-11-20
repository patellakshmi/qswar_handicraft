package com.qswar.hc.config.helper;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.utility.JwtPayloadDecoder;
import com.qswar.hc.utility.StringMatcher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private UserDetailsInterface userDetailsInterface;

    ObjectMapper objectMapper = new ObjectMapper();
    public AuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsInterface userDetailsInterface) {
        super(authenticationManager);
        this.userDetailsInterface = userDetailsInterface;

    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String hc_auth = request.getHeader(SecurityConfigConst.F4E_AUTH);
        String identity = request.getHeader(SecurityConfigConst.F4E_IDENTITY);

        String requestPath = request.getRequestURI();

        String privateApiPath = APIConstant.PRIVATE + "/";
        if (requestPath.startsWith(privateApiPath)) {
            if( StringUtils.isBlank(hc_auth) || StringUtils.isBlank(identity) ){
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        String publicApiPath = APIConstant.PUBLIC + "/";
        if (requestPath.startsWith(publicApiPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        if( isRequestAutherize(request)){
            filterChain.doFilter(request, response);
            return;
        }
        throw new IOException();

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConfigConst.F4E_AUTH);
        String identity = request.getHeader(SecurityConfigConst.F4E_IDENTITY);
        Employee employee = userDetailsInterface.getUser(identity);
        String decodedPayload = JwtPayloadDecoder.decodePayload(token);

        if( employee == null) {
            return null;
        }

        boolean identityAuthContain = StringMatcher.containsRegex(decodedPayload, identity,false)
                || StringMatcher.containsRegex(decodedPayload, employee.getUsername(),false)
                || StringMatcher.containsRegex(decodedPayload, employee.getEmail(),false)
                || StringMatcher.containsRegex(decodedPayload, employee.getPhone(),false);

        if ( !identityAuthContain ){
            return null;
        }

        boolean userClaim = StringUtils.equals(employee.getUsername(), identity) || StringUtils.equals(employee.getEmail(), identity) || StringUtils.equals(employee.getPhone(), identity);
        if (userClaim) {
            return new UsernamePasswordAuthenticationToken(employee.getUsername(), employee.getPassword(), new ArrayList<>());
        }
        return null;

    }

    private boolean isRequestAutherize(HttpServletRequest request) {

        String token = request.getHeader(SecurityConfigConst.F4E_AUTH);
        String identity = request.getHeader(SecurityConfigConst.F4E_IDENTITY);
        Employee employee = userDetailsInterface.getUser(identity);
        String decodedPayload = JwtPayloadDecoder.decodePayload(token);

        if( employee == null) {
            return false;
        }

        boolean identityAuthContain = StringMatcher.containsRegex(decodedPayload, identity,false)
                || StringMatcher.containsRegex(decodedPayload, employee.getUsername(),false)
                || StringMatcher.containsRegex(decodedPayload, employee.getEmail(),false)
                || StringMatcher.containsRegex(decodedPayload, employee.getPhone(),false);

        return identityAuthContain;
    }

}
