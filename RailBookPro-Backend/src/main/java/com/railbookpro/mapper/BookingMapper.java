package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Booking;
import com.railbookpro.domain.entity.BookingPassenger;
import com.railbookpro.dto.booking.BookingResponse;
import com.railbookpro.dto.booking.PassengerDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public PassengerDto toPassengerDto(BookingPassenger p) {
        PassengerDto dto = new PassengerDto();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setAge(p.getAge());
        dto.setGender(p.getGender());
        dto.setSeatNumber(p.getSeatNumber());
        return dto;
    }

    public BookingResponse toResponse(Booking b) {
        List<PassengerDto> passengers = b.getPassengers().stream()
                .map(this::toPassengerDto)
                .collect(Collectors.toList());

        return BookingResponse.builder()
                .id(b.getId())
                .pnr(b.getPnr())
                .userId(b.getUser().getId())
                .passengerName(b.getUser().getFullName())
                .trainId(b.getTrain().getId())
                .trainNumber(b.getTrain().getTrainNumber())
                .trainName(b.getTrain().getName())
                .sourceStation(b.getTrain().getSource().getName())
                .destinationStation(b.getTrain().getDestination().getName())
                .journeyDate(b.getJourneyDate())
                .travelClass(b.getTravelClass().name())
                .seatCount(b.getSeatCount())
                .totalFare(b.getTotalFare())
                .status(b.getStatus().name())
                .passengers(passengers)
                .bookedAt(b.getBookedAt())
                .cancelledAt(b.getCancelledAt())
                .build();
    }
}
