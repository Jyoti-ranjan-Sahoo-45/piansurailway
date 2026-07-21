package com.railbookpro.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 60)
    private String username;

    @Column(nullable = false, length = 60)
    private String action;

    @Column(length = 500)
    private String details;

    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime timestamp;

    @PrePersist
    void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
