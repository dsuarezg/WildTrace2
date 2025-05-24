package org.ironhack.species_service.mapper;

import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.ironhack.species_service.model.Species;

public class SpeciesMapper {

    /****
     * Converts a Species entity into a SpeciesResponseDTO.
     *
     * @param species the Species entity to convert
     * @return a SpeciesResponseDTO populated with the species' ID, common name, scientific name, and conservation status
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
     * Converts a SpeciesRequestDTO to a new Species entity.
     *
     * Copies the common name, scientific name, and conservation status from the DTO to the entity.
     *
     * @param dto the SpeciesRequestDTO containing species data
     * @return a new Species entity with values from the DTO
     */
    public static Species toEntity(SpeciesRequestDTO dto) {
        Species species = new Species();
        species.setCommonName(dto.getCommonName());
        species.setScientificName(dto.getScientificName());
        species.setConservationStatus(dto.getConservationStatus());
        return species;
    }
}
