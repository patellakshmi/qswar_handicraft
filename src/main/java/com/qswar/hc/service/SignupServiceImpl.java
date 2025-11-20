package com.qswar.hc.service;

import com.qswar.hc.config.helper.AuthUser;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.repository.EmployeeRepository;
import com.qswar.hc.utility.AuthUtility;
import com.qswar.hc.utility.IdGenerator;
import com.qswar.hc.utility.SignupValidator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SignupServiceImpl implements SignupService{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public ResponseEntity<GenericResponse> signupUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if(!SignupValidator.isValidSignupDetail(authUser)){
            return new ResponseEntity<GenericResponse>(new GenericResponse(Constant.FAIL, "User & Pass must contains more than 4 chars"), HttpStatus.BAD_REQUEST);
        }

        authUser.setPassword(this.passwordEncoder.encode(authUser.getPassword()));
        String uniqueId = null;
        Employee existingUser = null;
        do{
            uniqueId = IdGenerator.getUniqueId();
            existingUser = employeeRepository.findByQSwarId(uniqueId);
        }while (existingUser != null);

        Employee employee = employeeRepository.findByAnyOfUniqueField(authUser.getUsername());
        if( employee != null){
            return new ResponseEntity<GenericResponse>(new GenericResponse(Constant.FAIL, "Please use different user-name"), HttpStatus.FOUND);
        }
        employee = new Employee(uniqueId,  authUser.getUsername(), authUser.getPhone(), authUser.getEmail(), authUser.getPassword());
        employeeRepository.save(employee);
        AuthUtility.aadAuthHeader(request, response, employee);
        return new ResponseEntity<GenericResponse>(new GenericResponse("SUCCESS", "Thanks for signup!"), HttpStatus.OK);

    }


    @Override
    public ResponseEntity<GenericResponse> loginUser(AuthUser authUser, HttpServletRequest request, HttpServletResponse response) throws Exception {

        if(!SignupValidator.isValidSignupDetail(authUser)){
            return new ResponseEntity<GenericResponse>(new GenericResponse("SUCCESS", "User & Pass must contains more than 4 chars"), HttpStatus.FOUND);
        }

        authUser.setPassword(this.passwordEncoder.encode(authUser.getPassword()));

        Employee employee = employeeRepository.findByAnyOfUniqueField(authUser.getUsername());

        if( employee == null){
            return new ResponseEntity<GenericResponse>(new GenericResponse(Constant.FAIL, "Invalid credential"), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

        if( !StringUtils.equals(authUser.getPassword(), employee.getPassword())){
            return new ResponseEntity<GenericResponse>(new GenericResponse(Constant.FAIL, "Invalid credential"), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        AuthUtility.aadAuthHeader(request, response, employee);
        return new ResponseEntity<GenericResponse>(new GenericResponse(Constant.SUCCESS, "Thanks for signup!"), HttpStatus.OK);

    }


}
