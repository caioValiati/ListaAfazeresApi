package com.example.security;

import com.example.config.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityUtils(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public Long getCurrentUserId() {
        String token = getTokenFromSecurityContext();
        if (token == null || token.isEmpty()) {
            throw new IllegalStateException("No valid JWT token found in request");
        }
        return jwtUtil.getUserIdFromToken(token);
    }

    private String getTokenFromSecurityContext() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
            if (credentials instanceof String) {
                String token = (String) credentials;
                return token.startsWith("Bearer ") ? token.substring(7) : token;
            }
        }
        return null;
    }
}