package org.ironhack.speciesService.mapper;

import org.ironhack.speciesService.dto.SpeciesRequestDTO;
import org.ironhack.speciesService.dto.SpeciesResponseDTO;
import org.ironhack.speciesService.model.Species;

public class SpeciesMapper {


    /****
     * Converts a Species entity to a SpeciesResponseDTO.
     *
     * Transfers the species ID, common name, scientific name, and conservation status from the entity to the DTO.
     *
     * @param species the Species entity to convert
     * @return a SpeciesResponseDTO containing the corresponding data from the entity
     */

    public static SpeciesResponseDTO toResponseDTO(Species species) {
        SpeciesResponseDTO dto = new SpeciesResponseDTO();
        dto.setId(species.getSpeciesId());
        dto.setCommonName(species.getCommonName());
        dto.setScientificName(species.getScientificName());
        dto.setConservationStatus(species.getConservationStatus());
        return dto;
    }


    /****
     * Creates a new Species entity from the provided SpeciesRequestDTO.
     *
     * Copies the common name, scientific name, and conservation status from the DTO to the new entity.
     *
     * @param dto the SpeciesRequestDTO containing species information
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
