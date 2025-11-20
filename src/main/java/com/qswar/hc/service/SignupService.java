package com.qswar.hc.service;

import com.qswar.hc.config.helper.AuthUser;
import com.qswar.hc.pojos.responses.GenericResponse;
import org.codehaus.jettison.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface SignupService {
    ResponseEntity<GenericResponse> signupUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception;
    ResponseEntity<GenericResponse> loginUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
