package com.railbookpro.dto.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {
    private Long id;
    private Long trainId;
    private String trainNumber;
    private String trainName;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private Integer platform;
    private String runningDays;
}
