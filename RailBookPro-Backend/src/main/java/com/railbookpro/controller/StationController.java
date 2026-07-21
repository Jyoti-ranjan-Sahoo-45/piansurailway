package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.station.StationRequest;
import com.railbookpro.dto.station.StationResponse;
import com.railbookpro.service.StationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stations")
@RequiredArgsConstructor
public class StationController {

    private final StationService stationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StationResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(stationService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StationResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(stationService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StationResponse>> create(@Valid @RequestBody StationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Station created", stationService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StationResponse>> update(@PathVariable Long id, @Valid @RequestBody StationRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Station updated", stationService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        stationService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Station deleted", null));
    }
}
