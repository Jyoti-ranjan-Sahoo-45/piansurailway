package com.railbookpro.dto.route;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {
    private Long id;
    private Long trainId;
    private String trainNumber;
    private String trainName;
    private Long sourceStationId;
    private String sourceStationName;
    private Long destinationStationId;
    private String destinationStationName;
    private Double distanceKm;
    private String intermediateStations;
}
