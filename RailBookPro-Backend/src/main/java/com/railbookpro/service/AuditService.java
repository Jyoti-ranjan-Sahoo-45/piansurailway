package com.railbookpro.service;

import com.railbookpro.domain.entity.AuditLog;
import com.railbookpro.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    @Transactional
    public void log(String username, String action, String details) {
        auditLogRepository.save(AuditLog.builder()
                .username(username)
                .action(action)
                .details(details)
                .build());
    }
}
