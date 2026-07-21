package com.railbookpro.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private Long id;
    private String pnr;
    private Long userId;
    private String passengerName;
    private Long trainId;
    private String trainNumber;
    private String trainName;
    private String sourceStation;
    private String destinationStation;
    private LocalDate journeyDate;
    private String travelClass;
    private Integer seatCount;
    private Double totalFare;
    private String status;
    private List<PassengerDto> passengers;
    private LocalDateTime bookedAt;
    private LocalDateTime cancelledAt;
}
