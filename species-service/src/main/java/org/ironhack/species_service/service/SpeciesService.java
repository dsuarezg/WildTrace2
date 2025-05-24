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


    /****
     * Initializes the SpeciesService with the provided SpeciesRepository for managing species data.
     *
     * @param speciesRepository the repository used for species persistence operations
     */
    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }



    /**
     * Returns a list of all species as response DTOs.
     *
     * @return a list of SpeciesResponseDTO objects representing all species
     */
    public List<SpeciesResponseDTO> getAll() {
        return speciesRepository.findAll().stream()
                .map(SpeciesMapper::toResponseDTO)
                .toList();
    }


    /**
     * Retrieves a species by its unique identifier and returns its data as a response DTO.
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

    /**
     * Creates and persists a new species using the provided request data, returning the saved species as a response DTO.
     *
     * @param dto the request data for the species to create
     * @return the saved species as a response DTO
     */
    public SpeciesResponseDTO saveSpecies(SpeciesRequestDTO dto) {
        Species species = SpeciesMapper.toEntity(dto);
        return SpeciesMapper.toResponseDTO(speciesRepository.save(species));
    }


    /****
     * Updates the details of an existing species using data from the provided request DTO.
     *
     * @param id the unique identifier of the species to update
     * @param dto the request DTO containing the new species data
     * @return a response DTO representing the updated species
     * @throws SpeciesNotFoundException if no species with the specified ID is found
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
     * Removes the species entity with the specified ID from the repository.
     *
     * @param id the unique identifier of the species to be deleted
     * @throws SpeciesNotFoundException if a species with the given ID does not exist
     */
    public void deleteSpecies(Long id) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        speciesRepository.delete(found);
    }
}
