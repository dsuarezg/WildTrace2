package org.ironhack.sightingService.mapper;


import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.model.Sighting;

public class SightingMapper {

    /**
     * Converts a SightingRequestDTO into a Sighting entity by copying all relevant fields.
     *
     * @param dto the data transfer object containing sighting information
     * @return a Sighting entity populated with data from the DTO
     */
    public static Sighting toEntity(SightingRequestDTO dto) {
        Sighting entity = new Sighting();
        entity.setZoneId(dto.getZoneId());
        entity.setSpeciesId(dto.getSpeciesId());
        entity.setDate(dto.getDate());
        entity.setObservedBy(dto.getObservedBy());
        entity.setMethod(dto.getMethod());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    /**
     * Converts a Sighting entity to a SightingResponseDTO.
     *
     * @param entity the Sighting entity to convert
     * @return a SightingResponseDTO populated with data from the entity
     */
    public static SightingResponseDTO toResponseDTO(Sighting entity) {
        SightingResponseDTO dto = new SightingResponseDTO();
        dto.setId(entity.getId());
        dto.setZoneId(entity.getZoneId());
        dto.setSpeciesId(entity.getSpeciesId());
        dto.setDate(entity.getDate());
        dto.setObservedBy(entity.getObservedBy());
        dto.setMethod(entity.getMethod());
        dto.setNotes(entity.getNotes());
        return dto;
    }
}