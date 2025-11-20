package com.qswar.hc.config.header;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.*;

/**
 * A custom HttpServletRequestWrapper that allows modifications to request headers.
 * It stores modified headers internally and returns them instead of the original
 * request headers when queried.
 */
public class HeaderModifyingRequestWrapper extends HttpServletRequestWrapper {

    // Internal map to hold the custom/modified headers
    private final Map<String, String> customHeaders;

    public HeaderModifyingRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    /**
     * Use this method in your Filter's doFilter to set or replace a header value.
     * @param name The name of the header (e.g., "hc_auth").
     * @param value The new value for the header.
     */
    public void addHeader(String name, String value) {
        // Headers are typically case-insensitive, but we store them as provided.
        this.customHeaders.put(name, value);
    }

    // --- Override Header Retrieval Methods ---

    @Override
    public String getHeader(String name) {
        // 1. Check if the header was explicitly added/modified by this wrapper.
        String headerValue = customHeaders.get(name);

        if (headerValue != null) {
            return headerValue;
        }

        // 2. If not modified, fall back to the original request object.
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // If the header was modified, return only the new value as a single-element enumeration.
        if (customHeaders.containsKey(name)) {
            return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
        }

        // Otherwise, return the original headers (which may be multiple).
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        // 1. Get the header names from the original request.
        Set<String> names = new HashSet<>();
        Enumeration<String> originalHeaderNames = super.getHeaderNames();
        while (originalHeaderNames.hasMoreElements()) {
            names.add(originalHeaderNames.nextElement());
        }

        // 2. Add all custom header names (the ones we added/modified).
        // A Set ensures no duplicates, even if a header was modified.
        names.addAll(customHeaders.keySet());

        // 3. Return the combined set of names.
        return Collections.enumeration(names);
    }
}
