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
     * Initializes the SightingService with the specified repository and external service clients for species and zones.
     */
    public SightingService(SightingRepository sightingRepository,
                           SpeciesServiceClient speciesServiceClient,
                           ZoneServiceClient zoneServiceClient) {
        this.sightingRepository = sightingRepository;
        this.speciesServiceClient = speciesServiceClient;
        this.zoneServiceClient = zoneServiceClient;
    }

    /**
     * Retrieves all recorded sightings and returns them as response DTOs.
     *
     * @return a list of SightingResponseDTO objects for each sighting
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

    /**
     * Saves a new sighting after validating the existence of the specified species and zone.
     *
     * @param dto the sighting request data
     * @return the saved sighting as a response DTO
     * @throws SpeciesNotFoundException if the species ID does not exist
     * @throws ZoneNotFoundException if the zone ID does not exist
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
     * Updates the details of an existing sighting with new information.
     *
     * @param id the unique identifier of the sighting to update
     * @param dto the data containing updated sighting information
     * @return a response DTO representing the updated sighting
     * @throws SightingNotFoundException if a sighting with the specified ID does not exist
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
     * Deletes the sighting with the specified ID.
     *
     * @param id the ID of the sighting to delete
     * @throws SightingNotFoundException if a sighting with the given ID does not exist
     */
    public void delete(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        sightingRepository.delete(sighting);
    }
}
