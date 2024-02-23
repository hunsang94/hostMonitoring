package com.hostMonitoring.security;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtHeaderExtractor {
    public static String HEADER_PREFIX = "Bearer ";

    public String extract(String header) {
        if (header == null) {
            return null;
        }

        if (header.isEmpty()) {
            throw new AuthenticationServiceException("Authorization header cannot be empty!");
        }

        if (!header.startsWith(HEADER_PREFIX)) {
            throw new AuthenticationServiceException("Authorization header not bearer!");
        }
        return header.substring(HEADER_PREFIX.length());
    }
}
