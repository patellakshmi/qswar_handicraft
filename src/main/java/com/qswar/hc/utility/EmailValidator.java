package com.qswar.hc.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    // Regex pattern for a common email format
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    // Compile the regex into a Pattern object for performance
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Validates an email address against the defined regex pattern.
     *
     * @param email The email string to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean isValid(String email) {
        if (email == null) {
            return false;
        }

        // Create a Matcher object from the Pattern
        Matcher matcher = EMAIL_PATTERN.matcher(email);

        // Return true if the email matches the pattern
        return matcher.matches();
    }
}
