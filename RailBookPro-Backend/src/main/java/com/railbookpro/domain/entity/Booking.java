package com.railbookpro.domain.entity;

import com.railbookpro.domain.enums.BookingStatus;
import com.railbookpro.domain.enums.TravelClass;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bookings", uniqueConstraints = @UniqueConstraint(columnNames = "pnr"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String pnr;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;

    @Column(nullable = false)
    private LocalDate journeyDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TravelClass travelClass;

    @Column(nullable = false)
    private Integer seatCount;

    @Column(nullable = false)
    private Double totalFare;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BookingStatus status;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<BookingPassenger> passengers = new ArrayList<>();

    @Column(nullable = false, updatable = false, columnDefinition = "datetime")
    private LocalDateTime bookedAt;

    @Column(columnDefinition = "datetime")
    private LocalDateTime cancelledAt;

    @PrePersist
    void onCreate() {
        this.bookedAt = LocalDateTime.now();
    }
}
