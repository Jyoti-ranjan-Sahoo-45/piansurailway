package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Train;
import com.railbookpro.dto.train.TrainResponse;
import org.springframework.stereotype.Component;

@Component
public class TrainMapper {

    public TrainResponse toResponse(Train t) {
        return TrainResponse.builder()
                .id(t.getId())
                .trainNumber(t.getTrainNumber())
                .name(t.getName())
                .type(t.getType().name())
                .sourceStationId(t.getSource().getId())
                .sourceStationName(t.getSource().getName())
                .sourceStationCode(t.getSource().getCode())
                .destinationStationId(t.getDestination().getId())
                .destinationStationName(t.getDestination().getName())
                .destinationStationCode(t.getDestination().getCode())
                .totalSeats(t.getTotalSeats())
                .availableSeats(t.getAvailableSeats())
                .fare(t.getFare())
                .runningDays(t.getRunningDays())
                .status(t.getStatus().name())
                .build();
    }
}
