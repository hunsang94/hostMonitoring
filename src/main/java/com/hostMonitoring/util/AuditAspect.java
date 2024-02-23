package com.hostMonitoring.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hostMonitoring.dto.Event;
import com.hostMonitoring.security.JwtHeaderExtractor;
import com.hostMonitoring.security.JwtUtil;
import com.hostMonitoring.service.EventService;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtHeaderExtractor jwtHeaderExtractor;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EventService eventService;

    @AfterReturning(pointcut = "execution(* com.hostMonitoring.controller.*Controller.*(..))", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        eventService.saveEvent(Event
                .builder()
                .eventType(joinPoint.getSignature().getName())
                .clientAddr(getClientAddr())
                .userId(getUserIdByToken())
                .eventResult("SUCCESS")
                .build());
    }

    @AfterThrowing(pointcut = "execution(* com.hostMonitoring.controller.*Controller.*(..))", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        eventService.saveEvent(Event
                .builder()
                .eventType(joinPoint.getSignature().getName())
                .clientAddr(getClientAddr())
                .userId(getUserIdByToken())
                .eventResult("Fail. Error: " + e.getMessage())
                .build());
    }

    private String getClientAddr() {
        String addr = request.getHeader("X-Forwarded-For");
        if (addr == null) {
            addr = request.getRemoteAddr();
        }
        return addr;
    }

    private String getUserIdByToken() {
        String jwt = jwtHeaderExtractor.extract(request.getHeader("X-Authorization"));
        if (jwt == null) {
            return "";
        }
        return jwtUtil.extractUsername(jwt);
    }
}
