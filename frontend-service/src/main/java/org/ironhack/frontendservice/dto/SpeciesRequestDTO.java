package org.ironhack.frontendservice.dto;

import lombok.Data;

@Data
public class SpeciesRequestDTO {
    private String commonName;
    private String scientificName;
    private String conservationStatus;
}
