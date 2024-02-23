package com.hostMonitoring.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtBlacklist {
    private Set<String> blacklist;

    @Autowired
    private JwtUtil jwtUtil;

    public JwtBlacklist() {
        blacklist = new HashSet<>();
    }

    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    public boolean isBlacklisted(String token) {
        return blacklist.contains(token) && !jwtUtil.isTokenExpired(token);
    }

    public void cleanupBlacklist() {
        blacklist.removeIf(jwtUtil::isTokenExpired);
    }
}
