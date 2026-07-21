package com.railbookpro.service;

import com.railbookpro.domain.entity.Booking;
import com.railbookpro.domain.enums.BookingStatus;
import com.railbookpro.dto.common.DashboardStats;
import com.railbookpro.repository.BookingRepository;
import com.railbookpro.repository.RouteRepository;
import com.railbookpro.repository.TrainRepository;
import com.railbookpro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final TrainRepository trainRepository;
    private final BookingRepository bookingRepository;
    private final RouteRepository routeRepository;

    @Transactional(readOnly = true)
    public DashboardStats getAdminStats() {
        List<Booking> allBookings = bookingRepository.findAll();
        LocalDate today = LocalDate.now();

        double revenue = allBookings.stream()
                .filter(b -> b.getStatus() != BookingStatus.CANCELLED)
                .mapToDouble(Booking::getTotalFare)
                .sum();

        long todaysTrips = allBookings.stream()
                .filter(b -> today.equals(b.getJourneyDate()))
                .count();

        return DashboardStats.builder()
                .totalUsers(userRepository.count())
                .totalTrains(trainRepository.count())
                .totalBookings(bookingRepository.count())
                .todaysTrips(todaysTrips)
                .cancelledTickets(bookingRepository.countByStatus(BookingStatus.CANCELLED))
                .revenue(revenue)
                .activeRoutes(routeRepository.count())
                .build();
    }
}
