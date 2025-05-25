package org.ironhack.sightingService.service;

import org.ironhack.sightingService.client.SpeciesServiceClient;
import org.ironhack.sightingService.client.ZoneServiceClient;
import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.exception.SightingNotFoundException;
import org.ironhack.sightingService.exception.SpeciesNotFoundException;
import org.ironhack.sightingService.exception.ZoneNotFoundException;
import org.ironhack.sightingService.mapper.SightingMapper;
import org.ironhack.sightingService.model.Sighting;
import org.ironhack.sightingService.repository.SightingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SightingService {

    private final SightingRepository sightingRepository;
    private final SpeciesServiceClient speciesServiceClient;
    private final ZoneServiceClient zoneServiceClient;

    /****
     * Constructs a SightingService with the required repository and external service clients.
     */
    public SightingService(SightingRepository sightingRepository,
                           SpeciesServiceClient speciesServiceClient,
                           ZoneServiceClient zoneServiceClient) {
        this.sightingRepository = sightingRepository;
        this.speciesServiceClient = speciesServiceClient;
        this.zoneServiceClient = zoneServiceClient;
    }

    /**
     * Returns a list of all recorded sightings as response DTOs.
     *
     * @return a list of SightingResponseDTO objects representing all sightings
     */
    public List<SightingResponseDTO> getAll() {
        return sightingRepository.findAll().stream()
                .map(SightingMapper::toResponseDTO)
                .toList();
    }

    /****
     * Returns the sighting with the specified ID.
     *
     * @param id the unique identifier of the sighting
     * @return the corresponding SightingResponseDTO
     * @throws SightingNotFoundException if no sighting with the given ID exists
     */
    public SightingResponseDTO getById(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        return SightingMapper.toResponseDTO(sighting);
    }

    /**
     * Creates and persists a new sighting after verifying the existence of the specified species and zone.
     *
     * @param dto the sighting data to be saved
     * @return the persisted sighting as a response DTO
     * @throws SpeciesNotFoundException if the provided species ID does not exist
     * @throws ZoneNotFoundException if the provided zone ID does not exist
     */
    public SightingResponseDTO save(SightingRequestDTO dto) {
        try {
            speciesServiceClient.getSpeciesById(dto.getSpeciesId());
        } catch (Exception e) {
            throw new SpeciesNotFoundException(dto.getSpeciesId());
        }

        try {
            zoneServiceClient.getZoneById(dto.getZoneId());
        } catch (Exception e) {
            throw new ZoneNotFoundException(dto.getZoneId());
        }

        Sighting sighting = SightingMapper.toEntity(dto);
        return SightingMapper.toResponseDTO(sightingRepository.save(sighting));
    }

    /**
     * Updates an existing sighting with new data.
     *
     * @param id the ID of the sighting to update
     * @param dto the new data for the sighting
     * @return the updated sighting as a response DTO
     * @throws SightingNotFoundException if no sighting with the given ID exists
     */
    public SightingResponseDTO update(Long id, SightingRequestDTO dto) {
        Sighting existing = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));

        existing.setZoneId(dto.getZoneId());
        existing.setSpeciesId(dto.getSpeciesId());
        existing.setDate(dto.getDate());
        existing.setObservedBy(dto.getObservedBy());
        existing.setMethod(dto.getMethod());
        existing.setNotes(dto.getNotes());

        return SightingMapper.toResponseDTO(sightingRepository.save(existing));
    }


    /**
     * Removes a sighting with the specified ID.
     *
     * @param id the unique identifier of the sighting to delete
     * @throws SightingNotFoundException if no sighting with the given ID exists
     */
    public void delete(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        sightingRepository.delete(sighting);
    }
}
