package com.hostMonitoring.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserInfo {
    @Id
    private String id;

    @Column
    private String password;

    @Column(columnDefinition = "length = 5 default 'USER'")
    private String role;
}
