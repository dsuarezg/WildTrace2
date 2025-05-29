package org.ironhack.frontendservice.dto;

import lombok.Data;

@Data
public class ZoneResponseDTO {
    private Long zoneId;
    private String zoneName;
    private Double latitude;
    private Double longitude;
    private String mapImageUrl;

}
