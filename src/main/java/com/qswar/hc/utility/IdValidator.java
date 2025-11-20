package com.qswar.hc.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class IdValidator {
    public static int COURSE_ID_LENGTH = 10;
    public static int SUBJECT_ID_LENGTH = 3;
    public static int SUBJECT_PART_ID_LENGTH = 3;

    public static boolean isValid(String id) {
        if(id.length() == COURSE_ID_LENGTH && id.matches("[A-Za-z0-9]+")){ return true; }
        return false;
    }

    public static boolean isValidSubjectId(String id) {
        if(id.length() == SUBJECT_ID_LENGTH && id.matches("[A-Za-z0-9]+")){ return true; }
        return false;
    }

    public static boolean isValidSubjectPartId(String id) {
        if(id.length() == SUBJECT_PART_ID_LENGTH && id.matches("[A-Za-z0-9]+")){ return true; }
        return false;
    }
}
