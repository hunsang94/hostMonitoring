package com.hostMonitoring.vo;

import java.time.LocalDateTime;
import com.hostMonitoring.dto.Host;

import lombok.Setter;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HostVO {
    private String ip;
    private String name;
    private LocalDateTime createDateTime;
    private LocalDateTime updateDateTime;

    public HostVO(Host host) {
        this.ip = host.getHostPk().getIp();
        this.name = host.getHostPk().getName();
        this.createDateTime = host.getCreateDateTime();
        this.updateDateTime = host.getUpdateDateTime();
    }
}