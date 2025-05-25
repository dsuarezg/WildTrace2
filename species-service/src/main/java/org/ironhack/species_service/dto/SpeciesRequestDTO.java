package org.ironhack.species_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Species data used for creating or updating a record")
public class SpeciesRequestDTO {

    @NotBlank
    @Schema(description = "Common name of the species", example = "Red Fox")
    private String commonName;

    @NotBlank
    @Schema(description = "Scientific name", example = "Vulpes vulpes")
    private String scientificName;

    @Schema(description = "Conservation status", example = "Least Concern")
    private String conservationStatus;
}
