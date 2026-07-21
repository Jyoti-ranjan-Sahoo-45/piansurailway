package com.railbookpro.service;

import com.railbookpro.domain.entity.Station;
import com.railbookpro.domain.entity.Train;
import com.railbookpro.domain.enums.TrainStatus;
import com.railbookpro.domain.enums.TrainType;
import com.railbookpro.dto.train.TrainRequest;
import com.railbookpro.dto.train.TrainResponse;
import com.railbookpro.exception.BadRequestException;
import com.railbookpro.exception.DuplicateResourceException;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.TrainMapper;
import com.railbookpro.repository.StationRepository;
import com.railbookpro.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final TrainMapper trainMapper;

    @Transactional(readOnly = true)
    public List<TrainResponse> getAll() {
        return trainRepository.findAll().stream().map(trainMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TrainResponse getById(Long id) {
        return trainMapper.toResponse(find(id));
    }

    @Transactional(readOnly = true)
    public List<TrainResponse> search(String source, String destination, String date, String trainNumber, String trainName) {
        String src = emptyToNull(source);
        String dst = emptyToNull(destination);
        String num = emptyToNull(trainNumber);
        String name = emptyToNull(trainName);
        return trainRepository.search(src, dst, num, name).stream()
                .map(trainMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TrainResponse create(TrainRequest request) {
        if (trainRepository.existsByTrainNumber(request.getTrainNumber())) {
            throw new DuplicateResourceException("Train number already exists: " + request.getTrainNumber());
        }
        Train train = new Train();
        applyRequest(train, request);
        return trainMapper.toResponse(trainRepository.save(train));
    }

    @Transactional
    public TrainResponse update(Long id, TrainRequest request) {
        Train train = find(id);
        if (!train.getTrainNumber().equals(request.getTrainNumber())
                && trainRepository.existsByTrainNumber(request.getTrainNumber())) {
            throw new DuplicateResourceException("Train number already exists: " + request.getTrainNumber());
        }
        applyRequest(train, request);
        return trainMapper.toResponse(trainRepository.save(train));
    }

    @Transactional
    public void delete(Long id) {
        Train train = find(id);
        trainRepository.delete(train);
    }

    private void applyRequest(Train train, TrainRequest request) {
        Station source = stationRepository.findById(request.getSourceStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Source Station", request.getSourceStationId()));
        Station destination = stationRepository.findById(request.getDestinationStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Station", request.getDestinationStationId()));

        if (source.getId().equals(destination.getId())) {
            throw new BadRequestException("Source and destination stations must differ");
        }
        if (request.getAvailableSeats() > request.getTotalSeats()) {
            throw new BadRequestException("Available seats cannot exceed total seats");
        }

        train.setTrainNumber(request.getTrainNumber());
        train.setName(request.getName());
        train.setType(parseEnum(TrainType.class, request.getType(), "train type"));
        train.setSource(source);
        train.setDestination(destination);
        train.setTotalSeats(request.getTotalSeats());
        train.setAvailableSeats(request.getAvailableSeats());
        train.setFare(request.getFare());
        train.setRunningDays(request.getRunningDays());
        train.setStatus(parseEnum(TrainStatus.class, request.getStatus(), "train status"));
    }

    private Train find(Long id) {
        return trainRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Train", id));
    }

    private <E extends Enum<E>> E parseEnum(Class<E> type, String value, String label) {
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid " + label + ": " + value);
        }
    }

    private String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
