package com.qswar.hc.controller;

import com.qswar.hc.constants.APIConstant;
import com.qswar.hc.constants.Constant;
import com.qswar.hc.pojos.responses.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExitingIdController {

    @GetMapping(APIConstant.PUBLIC+APIConstant.EXISTING+APIConstant.VERSION_1+APIConstant.IDENTITY)
    public ResponseEntity<GenericResponse> getAllExitingIds() {
        return new ResponseEntity<GenericResponse>(
                new GenericResponse( Constant.STATUS.SUCCESS.name(),
                        Constant.GET_ALL_IDS, null),
                HttpStatus.OK);
    }

}
