package com.railbookpro.dto.station;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StationRequest {

    @NotBlank(message = "Station code is required")
    @Size(max = 10)
    private String code;

    @NotBlank(message = "Station name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotNull(message = "Platform count is required")
    @Min(value = 1, message = "Platform count must be at least 1")
    private Integer platformCount;
}
