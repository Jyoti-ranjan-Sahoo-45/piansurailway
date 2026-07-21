package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.user.ChangePasswordRequest;
import com.railbookpro.dto.user.UserRequest;
import com.railbookpro.dto.user.UserResponse;
import com.railbookpro.security.SecurityUtils;
import com.railbookpro.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final SecurityUtils securityUtils;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.ok(userService.getAll()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserResponse>>> search(@RequestParam String q) {
        return ResponseEntity.ok(ApiResponse.ok(userService.search(q)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me() {
        Long id = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(id)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(userService.getById(id)));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User created", userService.createByAdmin(request)));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UserRequest request) {
        Long id = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok("Profile updated", userService.update(id, request)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("User updated", userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("User deleted", null));
    }

    @PostMapping("/me/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        Long id = securityUtils.getCurrentUser().getId();
        userService.changePassword(id, request);
        return ResponseEntity.ok(ApiResponse.ok("Password changed", null));
    }
}
