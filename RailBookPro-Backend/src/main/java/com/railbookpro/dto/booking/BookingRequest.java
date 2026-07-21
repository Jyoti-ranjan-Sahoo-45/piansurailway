package com.railbookpro.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BookingRequest {

    @NotNull(message = "Train id is required")
    private Long trainId;

    @NotNull(message = "Journey date is required")
    @FutureOrPresent(message = "Journey date cannot be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate journeyDate;

    @NotBlank(message = "Travel class is required")
    private String travelClass;

    @NotNull(message = "Seat count is required")
    @Min(value = 1, message = "At least 1 seat must be booked")
    @Max(value = 6, message = "Maximum 6 seats per booking")
    private Integer seatCount;

    @NotEmpty(message = "At least one passenger is required")
    @Valid
    private List<PassengerDto> passengers;
}
