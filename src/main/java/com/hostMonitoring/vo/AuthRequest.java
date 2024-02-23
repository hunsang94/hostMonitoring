package com.hostMonitoring.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthRequest {
    private String refreshToken;

    public AuthRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
