package com.qswar.hc.utility;

import org.apache.commons.lang3.RandomStringUtils;

public class IdGenerator {
    public static int ID_LENGTH = 10;

    public static String getUniqueId() {
        String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String NUM = "1234567890";
        String SPECIAL = "#%&";
        return RandomStringUtils.random(ID_LENGTH, ALPHA + NUM + SPECIAL);
    }
}
