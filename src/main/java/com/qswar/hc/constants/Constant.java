package com.qswar.hc.constants;

import lombok.ToString;

public class Constant {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAIL = "FAIL";
    public static final String UNMATCHED_FMT_TYPE= "Invalid type";
    public static final String GET_ALL_COURSE= "Extracted all course successfully";
    public static final String COURSE_CREATED= "Course has been created successfully";
    public static final String SUCCESSFULLY_DELETE_COURSE = "Course deleted successfully";
    public static final String SUBJECT_CREATED = "Subject has been created successfully";
    public static final String PLATFORM_DETAIL_CREATED = "Platform detail has been created successfully";
    public static final String PLATFORM_DETAIL_DELETED = "Platform detail has been deleted successfully";
    public static final String SLIDER_CREATED= "Slider image has been created successfully";
    public static final String GET_ALL_SLIDER= "Extracted all slider images successfully";
    public static final String OBJECTIVE_CREATED= "Objective has been created successfully";
    public static final String GET_ALL_IDS= "Extracted all IDS successfully";

    public static final String NO_EMPLOYEE_FOUND= "No employee found";
    public static final String EMPLOYEE_CREATED= "Creation of employee done successfully";
    public static final String FAIL_TO_CREATE_EMPLOYEE= "Failed to create the employee";
    public static final String EMPLOYEE_UPDATED= "Employee has been updated successfully";
    public static final String EMPLOYEE_DELETED= "Employee has been deleted successfully";
    public static final String UNAUTHORIZED_FOR_CREATE_EMP= "Your are unauthorized";

    @ToString
    public enum STATUS {
        SUCCESS("SUCCESS"),
        FAIL("FAIL");

        private final String name;
        STATUS(String name) {
            this.name = name;
        }
    }
}
