package com.railbookpro.service;

import com.railbookpro.domain.entity.*;
import com.railbookpro.domain.enums.BookingStatus;
import com.railbookpro.domain.enums.TravelClass;
import com.railbookpro.dto.booking.BookingRequest;
import com.railbookpro.dto.booking.BookingResponse;
import com.railbookpro.dto.booking.PassengerDto;
import com.railbookpro.exception.BadRequestException;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.BookingMapper;
import com.railbookpro.repository.BookingRepository;
import com.railbookpro.repository.TrainRepository;
import com.railbookpro.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TrainRepository trainRepository;
    private final BookingMapper bookingMapper;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final SecurityUtils securityUtils;

    @Transactional(readOnly = true)
    public List<BookingResponse> getAll() {
        return bookingRepository.findAll().stream().map(bookingMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {
        User user = securityUtils.getCurrentUser();
        return bookingRepository.findByUserIdOrderByBookedAtDesc(user.getId()).stream()
                .map(bookingMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BookingResponse getById(Long id) {
        Booking booking = find(id);
        enforceOwnershipOrAdmin(booking);
        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingResponse create(BookingRequest request) {
        User user = securityUtils.getCurrentUser();

        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new ResourceNotFoundException("Train", request.getTrainId()));

        if (request.getPassengers().size() != request.getSeatCount()) {
            throw new BadRequestException("Passenger count must match seat count");
        }
        if (train.getAvailableSeats() < request.getSeatCount()) {
            throw new BadRequestException("Only " + train.getAvailableSeats() + " seats available on this train");
        }

        TravelClass travelClass = parseTravelClass(request.getTravelClass());
        double classMultiplier = fareMultiplier(travelClass);
        double totalFare = train.getFare() * classMultiplier * request.getSeatCount();

        String pnr;
        do {
            pnr = com.railbookpro.util.PnrGenerator.generate();
        } while (bookingRepository.existsByPnr(pnr));

        Booking booking = Booking.builder()
                .pnr(pnr)
                .user(user)
                .train(train)
                .journeyDate(request.getJourneyDate())
                .travelClass(travelClass)
                .seatCount(request.getSeatCount())
                .totalFare(totalFare)
                .status(BookingStatus.CONFIRMED)
                .build();

        List<BookingPassenger> passengers = new ArrayList<>();
        int seatSeq = train.getTotalSeats() - train.getAvailableSeats() + 1;
        for (PassengerDto dto : request.getPassengers()) {
            passengers.add(BookingPassenger.builder()
                    .booking(booking)
                    .name(dto.getName())
                    .age(dto.getAge())
                    .gender(dto.getGender())
                    .seatNumber(travelClass.name() + "-" + (seatSeq++))
                    .build());
        }
        booking.setPassengers(passengers);

        train.setAvailableSeats(train.getAvailableSeats() - request.getSeatCount());
        trainRepository.save(train);

        Booking saved = bookingRepository.save(booking);

        notificationService.create(user, "Booking Successful",
                "Your ticket is confirmed. PNR: " + pnr + " for train " + train.getName() + ".");
        auditService.log(user.getUsername(), "BOOK_TICKET", "PNR " + pnr);

        return bookingMapper.toResponse(saved);
    }

    @Transactional
    public BookingResponse cancel(Long id) {
        Booking booking = find(id);
        enforceOwnershipOrAdmin(booking);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(java.time.LocalDateTime.now());

        Train train = booking.getTrain();
        train.setAvailableSeats(train.getAvailableSeats() + booking.getSeatCount());
        trainRepository.save(train);

        Booking saved = bookingRepository.save(booking);

        notificationService.create(booking.getUser(), "Ticket Cancelled",
                "Your ticket with PNR " + booking.getPnr() + " has been cancelled.");
        auditService.log(booking.getUser().getUsername(), "CANCEL_TICKET", "PNR " + booking.getPnr());

        return bookingMapper.toResponse(saved);
    }

    private void enforceOwnershipOrAdmin(Booking booking) {
        if (securityUtils.isAdmin()) {
            return;
        }
        User current = securityUtils.getCurrentUser();
        if (!booking.getUser().getId().equals(current.getId())) {
            throw new BadRequestException("You can only access your own bookings");
        }
    }

    private TravelClass parseTravelClass(String value) {
        try {
            return TravelClass.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid travel class: " + value);
        }
    }

    private double fareMultiplier(TravelClass travelClass) {
        return switch (travelClass) {
            case SL -> 1.0;
            case CC -> 1.3;
            case AC3 -> 1.6;
            case AC2 -> 2.2;
            case EC -> 2.8;
            case AC1 -> 3.5;
        };
    }

    private Booking find(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Booking", id));
    }
}
