package com.railbookpro.service;

import com.railbookpro.domain.entity.Notification;
import com.railbookpro.domain.entity.User;
import com.railbookpro.dto.notification.NotificationResponse;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.NotificationMapper;
import com.railbookpro.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void create(User user, String title, String message) {
        Notification n = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .readFlag(false)
                .build();
        notificationRepository.save(n);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponse markRead(Long id) {
        Notification n = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        n.setReadFlag(true);
        return notificationMapper.toResponse(notificationRepository.save(n));
    }

    @Transactional
    public void delete(Long id) {
        if (!notificationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Notification", id);
        }
        notificationRepository.deleteById(id);
    }
}
