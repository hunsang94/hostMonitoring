package com.hostMonitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostMonitoring.vo.AuthLoginRequest;
import com.hostMonitoring.vo.AuthRequest;
import com.hostMonitoring.vo.AuthResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;

import com.hostMonitoring.security.JwtBlacklist;
import com.hostMonitoring.security.JwtHeaderExtractor;
import com.hostMonitoring.security.JwtUtil;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHeaderExtractor jwtHeaderExtractor;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtBlacklist jwtBlacklist;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthLoginRequest authRequest) throws Exception {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUserId(),
                        authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .accessToken(jwtUtil.generateAccessToken(authRequest.getUserId()))
                            .refreshToken(jwtUtil.generateRefreshToken(authRequest.getUserId()))
                            .build());
        } else {
            AuthResponse res = new AuthResponse();
            res.setMessage("invalid user request");
            return ResponseEntity.badRequest().body(res);
        }
    }

    @PostMapping("/renewal")
    public ResponseEntity<AuthResponse> renewalToken(
            @RequestBody AuthRequest authRequest) throws Exception {

        AuthResponse res = new AuthResponse();
        String refreshToken = authRequest.getRefreshToken();
        try {
            jwtUtil.validateRefreshToken(refreshToken);
            return ResponseEntity.ok(
                    AuthResponse.builder()
                            .accessToken(jwtUtil.generateAccessToken(jwtUtil.extractUsername(refreshToken)))
                            .refreshToken(refreshToken)
                            .build());
        } catch (ExpiredJwtException e) {
            res.setMessage("expired refesh token");
        } catch (JwtException e) {
            res.setMessage("invalid refesh token request");
        }
        return ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws Exception {
        String jwt = jwtHeaderExtractor.extract(request.getHeader("X-Authorization"));
        jwtBlacklist.addToBlacklist(jwt);
        return ResponseEntity.ok("Logout successful");
    }
}
