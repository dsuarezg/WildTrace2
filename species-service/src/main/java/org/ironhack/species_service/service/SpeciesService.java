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

    public SpeciesService(SpeciesRepository speciesRepository) {
        this.speciesRepository = speciesRepository;
    }


    public List<SpeciesResponseDTO> getAll() {
        return speciesRepository.findAll().stream()
                .map(SpeciesMapper::toResponseDTO)
                .toList();
    }

    public SpeciesResponseDTO getById(Long id) {
        Species species = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        return SpeciesMapper.toResponseDTO(species);
    }

    public SpeciesResponseDTO saveSpecies(SpeciesRequestDTO dto) {
        Species species = SpeciesMapper.toEntity(dto);
        return SpeciesMapper.toResponseDTO(speciesRepository.save(species));
    }

    public SpeciesResponseDTO updateSpecies(Long id, SpeciesRequestDTO dto) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        found.setCommonName(dto.getCommonName());
        found.setScientificName(dto.getScientificName());
        found.setConservationStatus(dto.getConservationStatus());

        return SpeciesMapper.toResponseDTO(speciesRepository.save(found));
    }

    public void deleteSpecies(Long id) {
        Species found = speciesRepository.findById(id)
                .orElseThrow(() -> new SpeciesNotFoundException(id));
        speciesRepository.delete(found);
    }
}
