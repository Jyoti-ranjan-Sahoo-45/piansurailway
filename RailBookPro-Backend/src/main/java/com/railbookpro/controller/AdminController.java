package com.railbookpro.controller;

import com.railbookpro.dto.booking.BookingResponse;
import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.common.DashboardStats;
import com.railbookpro.dto.train.TrainResponse;
import com.railbookpro.dto.user.UserResponse;
import com.railbookpro.service.BookingService;
import com.railbookpro.service.DashboardService;
import com.railbookpro.service.TrainService;
import com.railbookpro.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DashboardService dashboardService;
    private final BookingService bookingService;
    private final TrainService trainService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStats>> dashboard() {
        return ResponseEntity.ok(ApiResponse.ok(dashboardService.getAdminStats()));
    }

    @GetMapping("/reports/bookings")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> bookingReport() {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getAll()));
    }

    @GetMapping("/reports/trains")
    public ResponseEntity<ApiResponse<List<TrainResponse>>> trainReport() {
        return ResponseEntity.ok(ApiResponse.ok(trainService.getAll()));
    }

    @GetMapping("/reports/users")
    public ResponseEntity<ApiResponse<List<UserResponse>>> userReport() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAll()));
    }

    @GetMapping("/reports/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> summary() {
        DashboardStats stats = dashboardService.getAdminStats();
        Map<String, Object> summary = Map.of(
                "totalBookings", stats.getTotalBookings(),
                "cancelledTickets", stats.getCancelledTickets(),
                "revenue", stats.getRevenue(),
                "totalTrains", stats.getTotalTrains(),
                "totalUsers", stats.getTotalUsers()
        );
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }
}
