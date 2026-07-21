package com.railbookpro.service;

import com.railbookpro.domain.entity.Station;
import com.railbookpro.dto.station.StationRequest;
import com.railbookpro.dto.station.StationResponse;
import com.railbookpro.exception.DuplicateResourceException;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.StationMapper;
import com.railbookpro.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StationService {

    private final StationRepository stationRepository;
    private final StationMapper stationMapper;

    @Transactional(readOnly = true)
    public List<StationResponse> getAll() {
        return stationRepository.findAll().stream().map(stationMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationResponse getById(Long id) {
        return stationMapper.toResponse(find(id));
    }

    @Transactional
    public StationResponse create(StationRequest request) {
        if (stationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Station code already exists: " + request.getCode());
        }
        Station station = Station.builder()
                .code(request.getCode().toUpperCase())
                .name(request.getName())
                .city(request.getCity())
                .state(request.getState())
                .platformCount(request.getPlatformCount())
                .build();
        return stationMapper.toResponse(stationRepository.save(station));
    }

    @Transactional
    public StationResponse update(Long id, StationRequest request) {
        Station station = find(id);
        if (!station.getCode().equalsIgnoreCase(request.getCode()) && stationRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Station code already exists: " + request.getCode());
        }
        station.setCode(request.getCode().toUpperCase());
        station.setName(request.getName());
        station.setCity(request.getCity());
        station.setState(request.getState());
        station.setPlatformCount(request.getPlatformCount());
        return stationMapper.toResponse(stationRepository.save(station));
    }

    @Transactional
    public void delete(Long id) {
        Station station = find(id);
        stationRepository.delete(station);
    }

    private Station find(Long id) {
        return stationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Station", id));
    }
}
