package com.qswar.hc.controller;

import com.qswar.hc.config.helper.AuthUser;
import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
public class SignupController {

    @Autowired
    SignupService signupService;

    @PostMapping(APIConstant.SIGNUP)
    public ResponseEntity<GenericResponse> signupUser(@RequestBody AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return signupService.signupUser(authUser, request, response);
    }

    @PostMapping(APIConstant.LOGIN)
    public ResponseEntity<GenericResponse> loginUser(@RequestBody AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return signupService.loginUser(authUser, request, response);
    }




}
