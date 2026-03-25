package com.smartcampus.smartcampus.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "delivery_log")
public class DeliveryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime deliveredAt;

    private boolean acknowledged = false;

    private LocalDateTime acknowledgedAt;

    @PrePersist
    public void prePersist() {
        this.deliveredAt = LocalDateTime.now();
    }
}