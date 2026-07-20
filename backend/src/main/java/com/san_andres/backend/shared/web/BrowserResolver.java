package com.san_andres.backend.shared.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class BrowserResolver {

    private static final String USER_AGENT = "User-Agent";

    private static final String UNKNOWN = "Unknown";
    private static final String CHROME = "Chrome";
    private static final String EDGE = "Edge";
    private static final String FIREFOX = "Firefox";
    private static final String SAFARI = "Safari";

    public String resolve(HttpServletRequest request) {

        String userAgent = request.getHeader(USER_AGENT);

        if (userAgent == null || userAgent.isBlank()) {
            return UNKNOWN;
        }

        if (userAgent.contains(CHROME) && !userAgent.contains("Edg")) {
            return CHROME;
        }

        if (userAgent.contains("Edg")) {
            return EDGE;
        }

        if (userAgent.contains(FIREFOX)) {
            return FIREFOX;
        }

        if (userAgent.contains(SAFARI) && !userAgent.contains(CHROME)) {
            return SAFARI;
        }

        return UNKNOWN;
    }
}