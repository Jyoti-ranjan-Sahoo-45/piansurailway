package com.railbookpro.repository;

import com.railbookpro.domain.entity.Booking;
import com.railbookpro.domain.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserIdOrderByBookedAtDesc(Long userId);
    Optional<Booking> findByPnr(String pnr);
    long countByStatus(BookingStatus status);
    boolean existsByPnr(String pnr);
}
