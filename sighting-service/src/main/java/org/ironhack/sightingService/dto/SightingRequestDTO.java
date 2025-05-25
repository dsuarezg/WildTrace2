package org.ironhack.sightingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Data required to create or update a sighting")
public class SightingRequestDTO {

    @NotNull
    @Schema(example = "1", description = "ID of the zone where the species was sighted")
    private Long zoneId;

    @NotNull
    @Schema(example = "5", description = "ID of the species that was sighted")
    private Long speciesId;

    @NotNull
    @Schema(example = "2024-05-24", description = "Date of the sighting")
    private LocalDate date;

    @NotBlank
    @Schema(example = "Juan Pérez", description = "Observer's name")
    private String observedBy;

    @NotBlank
    @Schema(example = "Visual observation with binoculars", description = "Method used to observe")
    private String method;

    @Schema(example = "It was seen flying over the pine forest")
    private String notes;
}