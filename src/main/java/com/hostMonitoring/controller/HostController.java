package com.hostMonitoring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hostMonitoring.vo.HostRequest;
import com.hostMonitoring.vo.HostResponse;
import com.hostMonitoring.vo.HostStatus;
import com.hostMonitoring.vo.HostVO;
import com.hostMonitoring.service.HostService;

import java.util.List;

@RequestMapping("/api/v1")
@RestController
public class HostController {

    @Autowired
    HostService hostService;

    @GetMapping("/hosts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HostVO>> getHostAll() throws Exception {
        return ResponseEntity.ok(hostService.getHostAll());
    }

    @GetMapping("/host/{ip}/{name}")
    public ResponseEntity<HostVO> getHost(@PathVariable String ip, @PathVariable String name) throws Exception {
        HostVO host;
        try {
            host = hostService.getHost(ip, name);
        } catch (Exception e) {
            // error 로그
            host = new HostVO();
        }
        return ResponseEntity.ok(host);
    }

    @PostMapping("/host")
    public ResponseEntity<HostResponse> registeHost(@RequestBody HostRequest request) {
        return ResponseEntity.ok(hostService.registeHost(request));
    }

    @PutMapping("/host")
    public ResponseEntity<HostResponse> updateHost(@RequestBody HostRequest request) {
        return ResponseEntity.ok(hostService.updateHost(request));
    }

    @DeleteMapping("/host/{ip}/{name}")
    public ResponseEntity<Void> deleteHost(@PathVariable String ip, @PathVariable String name) {
        hostService.deleteHost(ip, name);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/current/{ip}/{name}")
    public ResponseEntity<String> getCurrentHostStatus(@PathVariable String ip, @PathVariable String name) {
        return ResponseEntity.ok(hostService.getCurrentHostStatus(ip, name));
    }

    @GetMapping("/monitoring/hosts")
    public ResponseEntity<List<HostStatus>> getMonitoring(@RequestParam(required = false) String ip,
            @RequestParam(required = false) String name) {
        if (ip == null && name == null) {
            return ResponseEntity.ok(hostService.getMonitoringAll());
        } else {
            return ResponseEntity.ok(hostService.getMonitoring(ip, name));
        }
    }
}
