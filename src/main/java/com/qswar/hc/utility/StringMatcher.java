package com.qswar.hc.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringMatcher {

    /**
     * Finds a simple, exact match (case-sensitive).
     * This is the simplest and fastest method for a direct substring check.
     * * @param sentence The main string to search within.
     *
     * @param query The specific substring to search for.
     * @return true if the sentence contains the query, false otherwise.
     */
    public static boolean containsSimple(String sentence, String query) {
        if (sentence == null || query == null) {
            return false;
        }
        // String.contains() is the simplest implementation
        return sentence.contains(query);
    }

    /**
     * Finds a match using Regular Expressions, allowing for case-insensitivity.
     * This is ideal for pattern matching or ignoring capitalization.
     * * @param sentence The main string to search within.
     *
     * @param pattern       The regex pattern to search for (can be a simple word).
     * @param caseSensitive If true, matching is case-sensitive; otherwise, it's case-insensitive.
     * @return true if the pattern is found, false otherwise.
     */
    public static boolean containsRegex(String sentence, String pattern, boolean caseSensitive) {
        if (sentence == null || pattern == null) {
            return false;
        }

        // Determine the flags for the Pattern compilation
        int flags = 0;
        if (!caseSensitive) {
            // Pattern.CASE_INSENSITIVE flag makes the matching ignore case
            flags = Pattern.CASE_INSENSITIVE;
        }

        try {
            // Compile the pattern once
            Pattern compiledPattern = Pattern.compile(pattern, flags);

            // Create a Matcher object
            Matcher matcher = compiledPattern.matcher(sentence);

            // matcher.find() searches for the pattern anywhere in the sentence
            return matcher.find();
        } catch (java.util.regex.PatternSyntaxException e) {
            System.err.println("Invalid regex pattern: " + e.getMessage());
            return false;
        }
    }


}
