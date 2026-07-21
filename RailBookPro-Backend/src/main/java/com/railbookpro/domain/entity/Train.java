package com.railbookpro.domain.entity;

import com.railbookpro.domain.enums.TrainStatus;
import com.railbookpro.domain.enums.TrainType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "trains", uniqueConstraints = @UniqueConstraint(columnNames = "train_number"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_number", nullable = false, length = 10)
    private String trainNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrainType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "source_station_id", nullable = false)
    private Station source;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_station_id", nullable = false)
    private Station destination;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false)
    private Double fare;

    @Column(nullable = false, length = 30)
    private String runningDays;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrainStatus status;
}
