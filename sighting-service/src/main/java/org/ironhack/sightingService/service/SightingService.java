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
     * Initializes the SightingService with required dependencies.
     *
     * @param sightingRepository repository for CRUD operations on sightings
     * @param speciesServiceClient      Feign client for validating species existence
     * @param zoneServiceClient         Feign client for validating zone existence
     */
    public SightingService(SightingRepository sightingRepository,
                           SpeciesServiceClient speciesServiceClient,
                           ZoneServiceClient zoneServiceClient) {
        this.sightingRepository = sightingRepository;
        this.speciesServiceClient = speciesServiceClient;
        this.zoneServiceClient = zoneServiceClient;
    }

    /**
     * Retrieves all sightings.
     *
     * @return a list of SightingResponseDTO objects
     */
    public List<SightingResponseDTO> getAll() {
        return sightingRepository.findAll().stream()
                .map(SightingMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a specific sighting by ID.
     *
     * @param id the sighting ID
     * @return the found SightingResponseDTO
     * @throws SightingNotFoundException if the sighting doesn't exist
     */
    public SightingResponseDTO getById(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        return SightingMapper.toResponseDTO(sighting);
    }

    /**
     * Saves a new sighting after validating related zone and species.
     *
     * @param dto the request data for the sighting
     * @return the saved SightingResponseDTO
     * @throws SpeciesNotFoundException if species ID is invalid
     * @throws ZoneNotFoundException    if zone ID is invalid
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
     * Deletes a sighting by ID.
     *
     * @param id the sighting ID
     * @throws SightingNotFoundException if the sighting does not exist
     */
    public void delete(Long id) {
        Sighting sighting = sightingRepository.findById(id)
                .orElseThrow(() -> new SightingNotFoundException(id));
        sightingRepository.delete(sighting);
    }
}
