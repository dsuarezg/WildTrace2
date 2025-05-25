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
     * Constructs a SpeciesService with the specified SpeciesRepository for data access.
     *
     * @param speciesRepository the repository used for species persistence operations
     */
    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }


    /**
     * Retrieves all species records and returns them as a list of response DTOs.
     *
     * @return a list of SpeciesResponseDTO representing all species
     */
    public List<SpeciesResponseDTO> getAll() {
        return speciesRepository.findAll().stream()
                .map(SpeciesMapper::toResponseDTO)
                .toList();
    }

    /**
     * Retrieves a species by its ID and returns it as a response DTO.
     *
     * @param id the unique identifier of the species
     * @return the species data as a SpeciesResponseDTO
     * @throws SpeciesNotFoundException if no species with the given ID exists
     */
    public SpeciesResponseDTO getById(Long id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        return SpeciesMapper.toResponseDTO(species);
    }

    /**
     * Creates a new species from the provided request data and returns the saved species as a response DTO.
     *
     * @param dto the data for the species to be created
     * @return the saved species represented as a response DTO
     */
    public SpeciesResponseDTO saveSpecies(SpeciesRequestDTO dto) {
        Species species = SpeciesMapper.toEntity(dto);
        return SpeciesMapper.toResponseDTO(speciesRepository.save(species));
    }

    /**
     * Updates an existing species with new data provided in the request DTO.
     *
     * @param id the ID of the species to update
     * @param dto the data to update the species with
     * @return the updated species as a response DTO
     * @throws SpeciesNotFoundException if no species with the given ID exists
     */
    public SpeciesResponseDTO updateSpecies(Long id, SpeciesRequestDTO dto) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        found.setCommonName(dto.getCommonName());
        found.setScientificName(dto.getScientificName());
        found.setConservationStatus(dto.getConservationStatus());

        return SpeciesMapper.toResponseDTO(speciesRepository.save(found));
    }

    /**
     * Deletes a species by its ID.
     *
     * @param id the ID of the species to delete
     * @throws SpeciesNotFoundException if no species with the given ID exists
     */
    public void deleteSpecies(Long id) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        speciesRepository.delete(found);
    }
}
