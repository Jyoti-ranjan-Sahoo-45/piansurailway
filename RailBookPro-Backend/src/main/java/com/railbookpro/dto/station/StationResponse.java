package com.railbookpro.dto.station;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationResponse {
    private Long id;
    private String code;
    private String name;
    private String city;
    private String state;
    private Integer platformCount;
}
