package com.example.program.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Objects;

public final class Matchers {
    private Matchers(){}

    /**
     * Combine the servlet path and any extra path info into one full path.
     * This ignores the context path (base path of the app) and cleans it up
     * so we don’t have double slashes or an unnecessary slash at the end.
     */
    private static String requestPath(HttpServletRequest req) {
        // Path inside the app (without context path), e.g. "/auth/register"
        String servletPath = req.getServletPath();
        // Extra path after the main mapping, sometimes used with patterns like "/api/*"
        String pathInfo    = req.getPathInfo();
        // Merge them into one string
        String full = (servletPath == null ? "" : servletPath) + (pathInfo == null ? "" : pathInfo);
        // Remove trailing slash if not just "/"
        if (full.length() > 1 && full.endsWith("/")) {
            full = full.substring(0, full.length() - 1);
        }
        // If empty, return "/", otherwise return cleaned path
        return full.isEmpty() ? "/" : full;
    }

    /**
     * Create a matcher that checks:
     * 1. HTTP method matches (case-insensitive).
     * 2. Path matches exactly after cleanup.
     */
    public static RequestMatcher methodAndPath(String httpMethod, String exactPath) {
        // Clean up the expected path the same way we clean up the request path
        final String expected = (exactPath != null && exactPath.length() > 1 && exactPath.endsWith("/"))
                ? exactPath.substring(0, exactPath.length() - 1)
                : (Objects.equals(exactPath, "") ? "/" : exactPath);

        return request -> {
            // First check the method (GET, POST, etc.)
            if (!request.getMethod().equalsIgnoreCase(httpMethod)) return false;
            // Then check the path
            String actual = requestPath(request);
            return actual.equals(expected);
        };
    }


    /** Quick helpers for matching by method + path without writing out the method name each time. */
    public static RequestMatcher get(String path)  { return methodAndPath("GET",  path); }
    public static RequestMatcher post(String path) { return methodAndPath("POST", path); }
    public static RequestMatcher put(String path)  { return methodAndPath("PUT",  path); }
    public static RequestMatcher del(String path)  { return methodAndPath("DELETE", path); }


}
