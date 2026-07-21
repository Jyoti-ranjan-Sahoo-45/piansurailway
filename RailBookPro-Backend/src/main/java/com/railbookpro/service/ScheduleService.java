package com.railbookpro.service;

import com.railbookpro.domain.entity.Schedule;
import com.railbookpro.domain.entity.Train;
import com.railbookpro.dto.schedule.ScheduleRequest;
import com.railbookpro.dto.schedule.ScheduleResponse;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.ScheduleMapper;
import com.railbookpro.repository.ScheduleRepository;
import com.railbookpro.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TrainRepository trainRepository;
    private final ScheduleMapper scheduleMapper;

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAll() {
        return scheduleRepository.findAll().stream().map(scheduleMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScheduleResponse getById(Long id) {
        return scheduleMapper.toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getByTrain(Long trainId) {
        return scheduleRepository.findByTrainId(trainId).stream()
                .map(scheduleMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public ScheduleResponse create(ScheduleRequest request) {
        Schedule schedule = new Schedule();
        applyRequest(schedule, request);
        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleResponse update(Long id, ScheduleRequest request) {
        Schedule schedule = find(id);
        applyRequest(schedule, request);
        return scheduleMapper.toResponse(scheduleRepository.save(schedule));
    }

    @Transactional
    public void delete(Long id) {
        scheduleRepository.delete(find(id));
    }

    private void applyRequest(Schedule schedule, ScheduleRequest request) {
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new ResourceNotFoundException("Train", request.getTrainId()));
        schedule.setTrain(train);
        schedule.setDepartureTime(request.getDepartureTime());
        schedule.setArrivalTime(request.getArrivalTime());
        schedule.setPlatform(request.getPlatform());
        schedule.setRunningDays(request.getRunningDays());
        schedule.setDuration(computeDuration(request.getDepartureTime(), request.getArrivalTime()));
    }

    private String computeDuration(LocalTime departure, LocalTime arrival) {
        long minutes = Duration.between(departure, arrival).toMinutes();
        if (minutes < 0) {
            minutes += 24 * 60; // overnight journey
        }
        return String.format("%02dh %02dm", minutes / 60, minutes % 60);
    }

    private Schedule find(Long id) {
        return scheduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Schedule", id));
    }
}
