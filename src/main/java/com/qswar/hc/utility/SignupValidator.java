package com.qswar.hc.utility;

import com.qswar.hc.config.helper.AuthUser;
import org.apache.commons.lang3.StringUtils;

public class SignupValidator {

    public static boolean isValidSignupDetail(AuthUser authUser){
        if(StringUtils.isBlank(authUser.getUsername()) || StringUtils.isBlank(authUser.getPassword()))  return  false;
        if( authUser.getUsername().length() < 5 || authUser.getPassword().length() < 5) return false;
        return true;
    }
}
