package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Notification;
import com.railbookpro.dto.notification.NotificationResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .readFlag(n.isReadFlag())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
