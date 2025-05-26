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

    /**
     * Constructs a SightingService with the provided repository and external service clients.
     *
     * @param sightingRepository the repository for managing sighting entities
     * @param speciesServiceClient the client for accessing species data
     * @param zoneServiceClient the client for accessing zone data
     */
    public SightingService(SightingRepository sightingRepository,
                           SpeciesServiceClient speciesServiceClient,
                           ZoneServiceClient zoneServiceClient) {
        this.sightingRepository = sightingRepository;
        this.speciesServiceClient = speciesServiceClient;
        this.zoneServiceClient = zoneServiceClient;
    }

    /**
     * Retrieves all sightings and returns them as response DTOs.
     *
     * @return a list of response DTOs representing all recorded sightings
     */
    public List<SightingResponseDTO> getAll() {
        return sightingRepository.findAll().stream()
                .map(SightingMapper::toResponseDTO)
                .toList();
    }

    /****
     * Retrieves a sighting by its unique identifier.
     *
     * @param id the unique identifier of the sighting
     * @return the sighting as a SightingResponseDTO
     * @throws SightingNotFoundException if no sighting with the specified ID exists
     */
    public SightingResponseDTO getById(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        return SightingMapper.toResponseDTO(sighting);
    }

    /****
     * Creates and saves a new sighting after verifying that the specified species and zone exist.
     *
     * @param dto the data for the sighting to be created
     * @return the newly saved sighting as a response DTO
     * @throws SpeciesNotFoundException if the specified species ID does not exist
     * @throws ZoneNotFoundException if the specified zone ID does not exist
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
     * Updates an existing sighting with new information.
     *
     * @param id the unique identifier of the sighting to update
     * @param dto the updated sighting data
     * @return the updated sighting as a response DTO
     * @throws SightingNotFoundException if no sighting with the specified ID exists
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
     * Removes a sighting identified by the specified ID.
     *
     * @param id the unique identifier of the sighting to remove
     * @throws SightingNotFoundException if no sighting with the specified ID exists
     */
    public void delete(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        sightingRepository.delete(sighting);
    }
}
