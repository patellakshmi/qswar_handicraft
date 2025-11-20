package com.qswar.hc.utility;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNumberValidator {

    private static final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    /**
     * Validates a phone number based on a default region (e.g., "IN" for India).
     *
     * @param phoneNumber   The phone number string to validate.
     * @param defaultRegion The ISO 3166-1 alpha-2 country code (e.g., "US", "IN").
     * @return true if the number is valid for the specified region, false otherwise.
     */
    public static boolean isValidPhoneNumber(String phoneNumber, String defaultRegion) {
        try {
            // 1. Parse the string into a PhoneNumber object
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, defaultRegion);

            // 2. Validate the parsed number
            boolean isValid = phoneUtil.isValidNumber(numberProto);

            // Optional: Check if the number is valid for the specific region
            // boolean isValidForRegion = phoneUtil.isValidNumberForRegion(numberProto, defaultRegion);

            return isValid;

        } catch (NumberParseException e) {
            // This happens if the number format is completely unparseable
            System.err.println("NumberParseException was thrown: " + e.toString());
            return false;
        }
    }
}