package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.route.RouteRequest;
import com.railbookpro.dto.route.RouteResponse;
import com.railbookpro.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(routeService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(routeService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RouteResponse>> create(@Valid @RequestBody RouteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Route created", routeService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteResponse>> update(@PathVariable Long id, @Valid @RequestBody RouteRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Route updated", routeService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        routeService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Route deleted", null));
    }
}
