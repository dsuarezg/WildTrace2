package org.ironhack.sightingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data returned after creating or retrieving a sighting")
public class SightingResponseDTO {

    @Schema(example = "10")
    private Long id;

    @Schema(example = "1")
    private Long zoneId;

    @Schema(example = "5")
    private Long speciesId;

    @Schema(example = "2024-05-24")
    private LocalDate date;

    @Schema(example = "Juan Pérez")
    private String observedBy;

    @Schema(example = "Visual observation with binoculars")
    private String method;

    @Schema(example = "It was seen flying over the pine forest")
    private String notes;
}