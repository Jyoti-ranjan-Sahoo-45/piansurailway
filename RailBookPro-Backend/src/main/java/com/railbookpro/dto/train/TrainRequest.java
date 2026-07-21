package com.railbookpro.dto.train;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TrainRequest {

    @NotBlank(message = "Train number is required")
    @Pattern(regexp = "^[0-9]{4,6}$", message = "Train number must be 4-6 digits")
    private String trainNumber;

    @NotBlank(message = "Train name is required")
    private String name;

    @NotBlank(message = "Train type is required")
    private String type;

    @NotNull(message = "Source station id is required")
    private Long sourceStationId;

    @NotNull(message = "Destination station id is required")
    private Long destinationStationId;

    @NotNull(message = "Total seats is required")
    @Min(value = 1, message = "Total seats must be at least 1")
    private Integer totalSeats;

    @NotNull(message = "Available seats is required")
    @Min(value = 0, message = "Available seats cannot be negative")
    private Integer availableSeats;

    @NotNull(message = "Fare is required")
    @Positive(message = "Fare must be positive")
    private Double fare;

    @NotBlank(message = "Running days is required")
    private String runningDays;

    @NotBlank(message = "Status is required")
    private String status;
}
