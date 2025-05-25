package org.ironhack.sightingService.mapper;


import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.model.Sighting;

public class SightingMapper {

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