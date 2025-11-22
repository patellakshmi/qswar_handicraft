package com.qswar.hc.utility;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Utility class for generating secure, fixed-length alphanumeric IDs.
 */
@Service
public class IdGenerator {

    private static final String ALPHANUMERIC_CHARS =
            "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final int ID_LENGTH = 10;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * Generates a unique ID string of 10 alphanumeric characters.
     * Note: While highly unlikely, caller logic (like in the service layer)
     * should check for true uniqueness against the database.
     * * @return A 10-character alphanumeric ID.
     */
    public String getUniqueId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        int charCount = ALPHANUMERIC_CHARS.length();

        for (int i = 0; i < ID_LENGTH; i++) {
            // Generate a secure random index within the bounds of the character set
            int randomIndex = SECURE_RANDOM.nextInt(charCount);
            sb.append(ALPHANUMERIC_CHARS.charAt(randomIndex));
        }

        return sb.toString();
    }
}