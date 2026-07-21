package com.railbookpro.mapper;

import com.railbookpro.domain.entity.Station;
import com.railbookpro.dto.station.StationResponse;
import org.springframework.stereotype.Component;

@Component
public class StationMapper {

    public StationResponse toResponse(Station s) {
        return StationResponse.builder()
                .id(s.getId())
                .code(s.getCode())
                .name(s.getName())
                .city(s.getCity())
                .state(s.getState())
                .platformCount(s.getPlatformCount())
                .build();
    }
}
