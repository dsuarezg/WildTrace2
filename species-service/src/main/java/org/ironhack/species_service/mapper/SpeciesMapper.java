package org.ironhack.species_service.mapper;

import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.ironhack.species_service.model.Species;

public class SpeciesMapper {

    /**
     * Converts a Species entity to a SpeciesResponseDTO.
     *
     * @param species the Species entity to convert
     * @return a SpeciesResponseDTO containing the species' ID, common name, scientific name, and conservation status
     */
    public static SpeciesResponseDTO toResponseDTO(Species species) {
        SpeciesResponseDTO dto = new SpeciesResponseDTO();
        dto.setId(species.getSpeciesId());
        dto.setCommonName(species.getCommonName());
        dto.setScientificName(species.getScientificName());
        dto.setConservationStatus(species.getConservationStatus());
        return dto;
    }

    /**
     * Creates a Species entity from a SpeciesRequestDTO.
     *
     * Copies the common name, scientific name, and conservation status from the DTO to a new Species entity.
     *
     * @param dto the data transfer object containing species information
     * @return a new Species entity populated with data from the DTO
     */
    public static Species toEntity(SpeciesRequestDTO dto) {
        Species species = new Species();
        species.setCommonName(dto.getCommonName());
        species.setScientificName(dto.getScientificName());
        species.setConservationStatus(dto.getConservationStatus());
        return species;
    }
}
