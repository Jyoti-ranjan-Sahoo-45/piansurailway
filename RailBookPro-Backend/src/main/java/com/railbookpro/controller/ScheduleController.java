package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.schedule.ScheduleRequest;
import com.railbookpro.dto.schedule.ScheduleResponse;
import com.railbookpro.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.getAll()));
    }

    @GetMapping("/train/{trainId}")
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getByTrain(@PathVariable Long trainId) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.getByTrain(trainId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(scheduleService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ScheduleResponse>> create(@Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Schedule created", scheduleService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ScheduleResponse>> update(@PathVariable Long id, @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Schedule updated", scheduleService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Schedule deleted", null));
    }
}
