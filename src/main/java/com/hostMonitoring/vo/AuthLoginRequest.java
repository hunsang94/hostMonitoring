package com.hostMonitoring.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthLoginRequest {
    private String userId;
    private String password;

    public AuthLoginRequest(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
