package com.hostMonitoring.vo;

import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostRequest {
    private String ip;
    private String name;
}