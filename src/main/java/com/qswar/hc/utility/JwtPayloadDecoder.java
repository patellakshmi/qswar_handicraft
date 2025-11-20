package com.qswar.hc.utility;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for manually decoding the payload of a JWT token string.
 * This class does NOT perform signature verification.
 */
public class JwtPayloadDecoder {
    /**
     * Splits the JWT and decodes the payload (the second part).
     * @param token The JWT string.
     * @return The decoded payload as a JSON string, or null if decoding fails.
     */
    public static String decodePayload(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        try {
            // JWT format is Header.Payload.Signature
            String[] parts = token.split("\\.");

            if (parts.length < 2) {
                System.err.println("Error: Invalid JWT format (less than two parts).");
                return null;
            }

            // The payload is the second part (index 1)
            String payloadBase64 = parts[1];

            // Use getUrlDecoder() to handle Base64Url format (which uses - and _ instead of + and /)
            byte[] decodedBytes = Base64.getUrlDecoder().decode(payloadBase64);

            // Convert the bytes to a UTF-8 string
            return new String(decodedBytes, StandardCharsets.UTF_8);

        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding Base64 payload: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred during decoding: " + e.getMessage());
            return null;
        }
    }

    /**
     * Helper to convert Unix timestamp (seconds) to a readable date.
     * @param timestamp The Unix time in seconds.
     * @return A formatted date string.
     */
    private static String formatTimestamp(long timestamp) {
        // Create an Instant from the seconds
        Instant instant = Instant.ofEpochSecond(timestamp);

        // Define a formatter for readability
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss O")
                .withZone(ZoneId.of("UTC"));

        return formatter.format(instant);
    }

}