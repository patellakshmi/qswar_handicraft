package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.model.Employee;
import com.qswar.hc.pojos.common.Identity;
import com.qswar.hc.pojos.responses.GenericResponse;
import com.qswar.hc.pojos.responses.IdentitiesResponse;
import com.qswar.hc.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExitingIdController {


    private final EmployeeService employeeService;

    @Autowired
    public ExitingIdController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping(APIConstant.PUBLIC+APIConstant.VERSION_1+APIConstant.EXISTING+APIConstant.IDENTITY)
    public ResponseEntity<GenericResponse> getAllExitingIds() {

        List<Employee> listEmployee = employeeService.findAllEmployees();

        if( CollectionUtils.isEmpty(listEmployee)) {
            return new ResponseEntity<GenericResponse>(
                    new GenericResponse( Constant.STATUS.SUCCESS.name(),
                    Constant.GET_ALL_IDS, null),
                    HttpStatus.OK);
        }

        IdentitiesResponse identitiesResponse = IdentitiesResponse.builder().identities(new ArrayList<>()).build();
        for (Employee employee : listEmployee) {
            Identity identity = Identity.builder()
                    .email(employee.getEmail())
                    .phone(employee.getPhone())
                    .username(employee.getUsername())
                    .build();
            identitiesResponse.getIdentities().add(identity);
        }

        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.GET_ALL_IDS, identitiesResponse),
                HttpStatus.OK);
    }

}
