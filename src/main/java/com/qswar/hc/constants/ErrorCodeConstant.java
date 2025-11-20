package com.qswar.hc.constants;

import lombok.ToString;

public class ErrorCodeConstant {
    public static final String INVALID_ID= "Invalid Id";
    public static final String ALREADY_FOUND= "Course already found";
    public static final String ERROR_TO_SAVE= "Error to save course";
    public static final String FAILED_TO_DELETE_COURSE= "Failed to delete given course";
    public static final String FAILED_TO_ADD_SUBJECT= "Failed to add subject";
    public static final String FAILED_TO_DELETE_SUBJECT= "Failed to delete subject";
    public static final String FAILED_TO_ADD_SUBJECT_PART= "Failed to add subject";
    public static final String FAILED_TO_DELETE_SUBJECT_PART= "Failed to delete subject part";
    public static final String FAILED_TO_ADD_PLATFORM_DETAIL= "Failed to add platform detail";
    public static final String FAILED_TO_DEL_PLATFORM_DETAIL= "Failed to delete platform detail";
    public static final String FAILED_TO_CREATED_SLIDER= "Failed to create slider image";
    public static final String FAILED_TO_CREATED_OBJECTIVE= "Failed to create objective";



    public enum ErrorCode {
        INVALID_ID("INVALID_ID","ID must contain 10 alpha-num"),
        FAILED_TO_SAVE("FAILED_TO_SAVE","Failed to save"),
        COURSE_ALREADY_EXIST("COURSE_ALREADY_EXIST","Course already exist with us"),
        BATCH_ALREADY_ALLOCATED("BATCH_ALREADY_ALLOCATED","Some batches are already allocated for given course"),
        FAILED_TO_CREATE_SUBJECT("FAILED_TO_CREATE_SUBJECT","Failed to add subject"),
        INVALID_SUB_PART_ID("INVALID_SUB_PART_ID","Subject part id must be 3 alpha-num chars"),
        INVALID_SUB_ID("INVALID_SUB_ID","Subject id must be 3 alpha-num chars"),
        FAILED_TO_ADD_DETAIL("FAILED_TO_ADD_DETAIL","Failed to add platform detail"),
        FAILED_TO_DEL_DETAIL("FAILED_TO_DEL_DETAIL","Failed to delete platform detail"),
        FAILED_TO_CREATED_SLIDER("FAILED_TO_CREATED_SLIDER","Failed to create slider image"),
        FAILED_TO_CREATED_OBJECTIVE("FAILED_TO_CREATED_OBJECTIVE","Failed to create objective");

        public String desc() {
            return desc;
        }
        private final String name;
        private final String desc;
        ErrorCode(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }
    }
}
