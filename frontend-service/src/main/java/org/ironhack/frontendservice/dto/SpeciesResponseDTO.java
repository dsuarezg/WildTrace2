package org.ironhack.frontendservice.dto;

import lombok.Data;

@Data
public class SpeciesResponseDTO {
    private Long speciesId;
    private String commonName;
    private String scientificName;
    private String conservationStatus;
}
