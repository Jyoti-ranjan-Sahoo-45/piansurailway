package com.railbookpro.dto.train;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainResponse {
    private Long id;
    private String trainNumber;
    private String name;
    private String type;
    private Long sourceStationId;
    private String sourceStationName;
    private String sourceStationCode;
    private Long destinationStationId;
    private String destinationStationName;
    private String destinationStationCode;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double fare;
    private String runningDays;
    private String status;
}
