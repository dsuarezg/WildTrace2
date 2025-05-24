package org.ironhack.species_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Species data returned by the API")
public class SpeciesResponseDTO {

    @Schema(description = "Unique identifier", example = "1")
    private Long id;

    @Schema(description = "Common name", example = "Lince ibérico")
    private String commonName;

    @Schema(description = "Scientific name", example = "Lynx pardinus")
    private String scientificName;

    @Schema(description = "Conservation status", example = "Endangered")

    private String conservationStatus;
}
