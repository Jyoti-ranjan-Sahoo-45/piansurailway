package com.railbookpro.controller;

import com.railbookpro.dto.booking.BookingRequest;
import com.railbookpro.dto.booking.BookingResponse;
import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getAll()));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> myBookings() {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getMyBookings()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(bookingService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Booking successful", bookingService.create(request)));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<BookingResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Booking cancelled", bookingService.cancel(id)));
    }
}
