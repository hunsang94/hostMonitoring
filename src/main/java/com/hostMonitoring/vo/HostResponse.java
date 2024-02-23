package com.hostMonitoring.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HostResponse {
    private Boolean success;
    private String message;

    public HostResponse() {
        this.success = true;
        this.message = "";
    }

    public HostResponse(String message) {
        this.success = message.equals("");
        this.message = message;
    }

    public HostResponse(String message, boolean success) {
        this.success = success;
        this.message = message;
    }
}