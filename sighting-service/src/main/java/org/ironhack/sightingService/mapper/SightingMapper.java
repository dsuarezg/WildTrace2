package org.ironhack.sightingService.mapper;


import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.model.Sighting;

public class SightingMapper {

    /**
     * Converts a SightingRequestDTO to a new Sighting entity.
     *
     * Copies zone ID, species ID, date, observer, method, and notes from the DTO to the entity.
     *
     * @param dto the SightingRequestDTO containing sighting information
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
     * Converts a Sighting entity to a SightingResponseDTO by copying its details.
     *
     * @param entity the Sighting entity to convert
     * @return a SightingResponseDTO with the ID, zone ID, species ID, date, observer, method, and notes from the entity
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