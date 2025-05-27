package org.ironhack.frontendservice.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SightingResponseDTO {
    private Long id;
    private Long zoneId;
    private Long speciesId;
    private LocalDate date;
    private String observedBy;
    private String method;
    private String notes;
}
