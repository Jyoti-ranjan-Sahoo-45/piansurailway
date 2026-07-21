package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.train.TrainRequest;
import com.railbookpro.dto.train.TrainResponse;
import com.railbookpro.service.TrainService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trains")
@RequiredArgsConstructor
public class TrainController {

    private final TrainService trainService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TrainResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(trainService.getAll()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TrainResponse>>> search(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String trainNumber,
            @RequestParam(required = false) String trainName) {
        return ResponseEntity.ok(ApiResponse.ok(
                trainService.search(source, destination, date, trainNumber, trainName)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(trainService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrainResponse>> create(@Valid @RequestBody TrainRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Train created", trainService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TrainResponse>> update(@PathVariable Long id, @Valid @RequestBody TrainRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Train updated", trainService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        trainService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Train deleted", null));
    }
}
