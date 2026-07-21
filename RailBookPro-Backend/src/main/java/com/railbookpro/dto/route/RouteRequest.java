package com.railbookpro.dto.route;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RouteRequest {

    @NotNull(message = "Train id is required")
    private Long trainId;

    @NotNull(message = "Source station id is required")
    private Long sourceStationId;

    @NotNull(message = "Destination station id is required")
    private Long destinationStationId;

    @NotNull(message = "Distance is required")
    @Positive(message = "Distance must be positive")
    private Double distanceKm;

    private String intermediateStations;
}
