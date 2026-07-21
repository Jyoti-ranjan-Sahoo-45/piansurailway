package com.railbookpro.controller;

import com.railbookpro.dto.common.ApiResponse;
import com.railbookpro.dto.notification.NotificationResponse;
import com.railbookpro.security.SecurityUtils;
import com.railbookpro.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> myNotifications() {
        Long userId = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(ApiResponse.ok(notificationService.getForUser(userId)));
    }

    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markRead(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok("Marked as read", notificationService.markRead(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.ok(ApiResponse.ok("Notification deleted", null));
    }
}
