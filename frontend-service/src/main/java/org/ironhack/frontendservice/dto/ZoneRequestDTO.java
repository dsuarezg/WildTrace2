package org.ironhack.frontendservice.dto;

import lombok.Data;

@Data
public class ZoneRequestDTO {
    private String zoneName;
    private Double latitude;
    private Double longitude;
}