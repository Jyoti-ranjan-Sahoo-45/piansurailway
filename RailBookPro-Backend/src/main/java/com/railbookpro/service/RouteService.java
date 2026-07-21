package com.railbookpro.service;

import com.railbookpro.domain.entity.Route;
import com.railbookpro.domain.entity.Station;
import com.railbookpro.domain.entity.Train;
import com.railbookpro.dto.route.RouteRequest;
import com.railbookpro.dto.route.RouteResponse;
import com.railbookpro.exception.ResourceNotFoundException;
import com.railbookpro.mapper.RouteMapper;
import com.railbookpro.repository.RouteRepository;
import com.railbookpro.repository.StationRepository;
import com.railbookpro.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final TrainRepository trainRepository;
    private final StationRepository stationRepository;
    private final RouteMapper routeMapper;

    @Transactional(readOnly = true)
    public List<RouteResponse> getAll() {
        return routeRepository.findAll().stream().map(routeMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RouteResponse getById(Long id) {
        return routeMapper.toResponse(find(id));
    }

    @Transactional
    public RouteResponse create(RouteRequest request) {
        Route route = new Route();
        applyRequest(route, request);
        return routeMapper.toResponse(routeRepository.save(route));
    }

    @Transactional
    public RouteResponse update(Long id, RouteRequest request) {
        Route route = find(id);
        applyRequest(route, request);
        return routeMapper.toResponse(routeRepository.save(route));
    }

    @Transactional
    public void delete(Long id) {
        routeRepository.delete(find(id));
    }

    private void applyRequest(Route route, RouteRequest request) {
        Train train = trainRepository.findById(request.getTrainId())
                .orElseThrow(() -> new ResourceNotFoundException("Train", request.getTrainId()));
        Station source = stationRepository.findById(request.getSourceStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Source Station", request.getSourceStationId()));
        Station destination = stationRepository.findById(request.getDestinationStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Station", request.getDestinationStationId()));

        route.setTrain(train);
        route.setSource(source);
        route.setDestination(destination);
        route.setDistanceKm(request.getDistanceKm());
        route.setIntermediateStations(request.getIntermediateStations());
    }

    private Route find(Long id) {
        return routeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route", id));
    }
}
