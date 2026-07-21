package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Route;
import com.railbookpro.dto.route.RouteResponse;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {

    public RouteResponse toResponse(Route r) {
        return RouteResponse.builder()
                .id(r.getId())
                .trainId(r.getTrain().getId())
                .trainNumber(r.getTrain().getTrainNumber())
                .trainName(r.getTrain().getName())
                .sourceStationId(r.getSource().getId())
                .sourceStationName(r.getSource().getName())
                .destinationStationId(r.getDestination().getId())
                .destinationStationName(r.getDestination().getName())
                .distanceKm(r.getDistanceKm())
                .intermediateStations(r.getIntermediateStations())
                .build();
    }
}
