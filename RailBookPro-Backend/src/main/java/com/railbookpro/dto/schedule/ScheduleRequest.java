package com.railbookpro.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleRequest {

    @NotNull(message = "Train id is required")
    private Long trainId;

    @NotNull(message = "Departure time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;

    @NotNull(message = "Arrival time is required")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;

    @NotNull(message = "Platform is required")
    private Integer platform;

    @NotNull(message = "Running days is required")
    private String runningDays;
}
