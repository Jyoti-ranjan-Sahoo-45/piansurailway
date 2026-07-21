package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Schedule;
import com.railbookpro.dto.schedule.ScheduleResponse;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMapper {

    public ScheduleResponse toResponse(Schedule s) {
        return ScheduleResponse.builder()
                .id(s.getId())
                .trainId(s.getTrain().getId())
                .trainNumber(s.getTrain().getTrainNumber())
                .trainName(s.getTrain().getName())
                .departureTime(s.getDepartureTime().toString())
                .arrivalTime(s.getArrivalTime().toString())
                .duration(s.getDuration())
                .platform(s.getPlatform())
                .runningDays(s.getRunningDays())
                .build();
    }
}
