package org.ironhack.species_service.mapper;

import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.ironhack.species_service.model.Species;

public class SpeciesMapper {

    // Converts entity to response DTO
    public static SpeciesResponseDTO toResponseDTO(Species species) {
        SpeciesResponseDTO dto = new SpeciesResponseDTO();
        dto.setId(species.getSpeciesId());
        dto.setCommonName(species.getCommonName());
        dto.setScientificName(species.getScientificName());
        dto.setConservationStatus(species.getConservationStatus());
        return dto;
    }

    // Converts request DTO to entity
    public static Species toEntity(SpeciesRequestDTO dto) {
        Species species = new Species();
        species.setCommonName(dto.getCommonName());
        species.setScientificName(dto.getScientificName());
        species.setConservationStatus(dto.getConservationStatus());
        return species;
    }
}
