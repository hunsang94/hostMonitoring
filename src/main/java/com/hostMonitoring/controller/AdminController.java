package com.hostMonitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hostMonitoring.dto.Event;
import com.hostMonitoring.service.EventService;

import java.util.List;

@RequestMapping("/api/v1/admin")
@RestController
public class AdminController {

    @Autowired
    EventService eventService;

    @GetMapping("/audits")
    public ResponseEntity<List<Event>> getAuditAll() throws Exception {
        return ResponseEntity.ok(eventService.getEventAll());
    }
}
