package com.hostMonitoring.dto;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String eventType;

    @Column
    private String clientAddr;

    @Column
    private String userId;

    @Column
    private String eventResult;

    @Column
    @CreatedDate
    private LocalDateTime eventDateTime;

    @PrePersist
    public void prePersist() {
        this.eventDateTime = this.eventDateTime == null ? LocalDateTime.now() : this.eventDateTime;
    }
}
