package com.hostMonitoring.vo;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HostStatus {
    private String ip;
    private String name;
    private String status;
    private String lastAliveTime;

    public HostStatus(String ip, String name) {
        this.ip = ip;
        this.name = name;
    }
}