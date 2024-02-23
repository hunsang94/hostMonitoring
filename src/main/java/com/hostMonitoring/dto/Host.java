package com.hostMonitoring.dto;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "host")
public class Host {
    @EmbeddedId
    private HostPk hostPk;

    @Column
    @CreatedDate
    private LocalDateTime createDateTime;

    @Column
    private LocalDateTime updateDateTime;

    @PrePersist
    public void prePersist() {
        this.createDateTime = this.createDateTime == null ? LocalDateTime.now() : this.createDateTime;
    }
}
