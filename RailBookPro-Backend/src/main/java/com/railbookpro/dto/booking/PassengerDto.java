package com.railbookpro.dto.booking;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PassengerDto {

    private Long id;

    @NotBlank(message = "Passenger name is required")
    private String name;

    @NotNull(message = "Age is required")
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "MALE|FEMALE|OTHER", message = "Gender must be MALE, FEMALE or OTHER")
    private String gender;

    private String seatNumber;
}
