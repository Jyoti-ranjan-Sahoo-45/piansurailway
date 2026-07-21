package com.railbookpro.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false, columnDefinition = "time")
    private LocalTime departureTime;

    @Column(nullable = false, columnDefinition = "time")
    private LocalTime arrivalTime;

    @Column(nullable = false, length = 20)
    private String duration;

    @Column(nullable = false)
    private Integer platform;

    @Column(nullable = false, length = 30)
    private String runningDays;
}
