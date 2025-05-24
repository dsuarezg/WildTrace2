package org.ironhack.species_service.service;

import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.ironhack.species_service.exception.SpeciesNotFoundException;
import org.ironhack.species_service.mapper.SpeciesMapper;
import org.ironhack.species_service.model.Species;
import org.ironhack.species_service.repository.SpeciesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpeciesService {

    private final SpeciesRepository speciesRepository;

    /**
     * Initializes the SpeciesService with a repository for species persistence operations.
     *
     * @param speciesRepository repository for accessing and managing species entities
     */
    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }


    /**
     * Retrieves all species records and returns them as response DTOs.
     *
     * @return a list of SpeciesResponseDTO objects representing all species
     */
    public List<SpeciesResponseDTO> getAll() {
        return speciesRepository.findAll().stream()
                .map(SpeciesMapper::toResponseDTO)
                .toList();
    }

    /****
     * Returns the species data for the given ID as a response DTO.
     *
     * @param id the unique identifier of the species
     * @return the species data as a SpeciesResponseDTO
     * @throws SpeciesNotFoundException if no species with the specified ID exists
     */
    public SpeciesResponseDTO getById(Long id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        return SpeciesMapper.toResponseDTO(species);
    }

    /****
     * Creates and persists a new species using the provided request data, returning the saved species as a response DTO.
     *
     * @param dto the request data for the species to be created
     * @return the saved species as a response DTO
     */
    public SpeciesResponseDTO saveSpecies(SpeciesRequestDTO dto) {
        Species species = SpeciesMapper.toEntity(dto);
        return SpeciesMapper.toResponseDTO(speciesRepository.save(species));
    }

    /**
     * Updates the details of an existing species identified by its ID.
     *
     * @param id the unique identifier of the species to update
     * @param dto the data transfer object containing updated species information
     * @return a response DTO representing the updated species
     * @throws SpeciesNotFoundException if a species with the specified ID does not exist
     */
    public SpeciesResponseDTO updateSpecies(Long id, SpeciesRequestDTO dto) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        found.setCommonName(dto.getCommonName());
        found.setScientificName(dto.getScientificName());
        found.setConservationStatus(dto.getConservationStatus());

        return SpeciesMapper.toResponseDTO(speciesRepository.save(found));
    }

    /****
     * Removes the species entity with the given ID from the repository.
     *
     * @param id the unique identifier of the species to delete
     * @throws SpeciesNotFoundException if a species with the specified ID does not exist
     */
    public void deleteSpecies(Long id) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        speciesRepository.delete(found);
    }
}
