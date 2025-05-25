package org.ironhack.speciesService.service;

import org.ironhack.speciesService.dto.SpeciesRequestDTO;
import org.ironhack.speciesService.dto.SpeciesResponseDTO;
import org.ironhack.speciesService.exception.SpeciesNotFoundException;
import org.ironhack.speciesService.model.Species;
import org.ironhack.speciesService.repository.SpeciesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SpeciesServiceTest {

    @Mock
    private SpeciesRepository speciesRepository;

    @InjectMocks
    private SpeciesService speciesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return a list with Lince ibérico when data exists")
    void testGetAllReturnsSpeciesList() {
        Species lynx = new Species(1L, "Lince ibérico", "Lynx pardinus", "Endangered");
        when(speciesRepository.findAll()).thenReturn(List.of(lynx));

        List<SpeciesResponseDTO> result = speciesService.getAll();

        assertEquals(1, result.size());
        assertEquals("Lince ibérico", result.get(0).getCommonName());
    }

    @Test
    @DisplayName("Should return empty list when no species exist")
    void testGetAllReturnsEmptyList() {
        when(speciesRepository.findAll()).thenReturn(List.of());

        List<SpeciesResponseDTO> result = speciesService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return Gato montés when valid ID is provided")
    void testGetByIdSuccess() {
        Species wildcat = new Species(1L, "Gato montés", "Felis silvestris", "Least Concern");
        when(speciesRepository.findById(1L)).thenReturn(Optional.of(wildcat));

        SpeciesResponseDTO result = speciesService.getById(1L);

        assertEquals("Gato montés", result.getCommonName());
        assertEquals("Felis silvestris", result.getScientificName());
    }

    @Test
    @DisplayName("Should throw SpeciesNotFoundException when species not found by ID")
    void testGetByIdNotFound() {
        when(speciesRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.getById(99L));
    }

    @Test
    @DisplayName("Should save and return Buitre negro")
    void testSaveSpecies() {
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        dto.setCommonName("Buitre negro");
        dto.setScientificName("Aegypius monachus");
        dto.setConservationStatus("Near Threatened");

        Species saved = new Species(1L, "Buitre negro", "Aegypius monachus", "Near Threatened");
        when(speciesRepository.save(any(Species.class))).thenReturn(saved);

        SpeciesResponseDTO result = speciesService.saveSpecies(dto);

        assertEquals("Buitre negro", result.getCommonName());
    }

    @Test
    @DisplayName("Should update Águila imperial successfully")
    void testUpdateSpeciesSuccess() {
        Species existing = new Species(1L, "Águila", "Old", "Old");
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        dto.setCommonName("Águila imperial ibérica");
        dto.setScientificName("Aquila adalberti");
        dto.setConservationStatus("Vulnerable");

        when(speciesRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(speciesRepository.save(any(Species.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        SpeciesResponseDTO result = speciesService.updateSpecies(1L, dto);

        assertEquals("Águila imperial ibérica", result.getCommonName());
        assertEquals("Aquila adalberti", result.getScientificName());
    }

    @Test
    @DisplayName("Should throw SpeciesNotFoundException when updating non-existent species")
    void testUpdateSpeciesNotFound() {
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        when(speciesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.updateSpecies(1L, dto));
    }

    @Test
    @DisplayName("Should delete Cigüeña negra when found by ID")
    void testDeleteSpeciesSuccess() {
        Species stork = new Species(1L, "Cigüeña negra", "Ciconia nigra", "Vulnerable");
        when(speciesRepository.findById(1L)).thenReturn(Optional.of(stork));
        doNothing().when(speciesRepository).delete(stork);

        assertDoesNotThrow(() -> speciesService.deleteSpecies(1L));
    }

    @Test
    @DisplayName("Should throw SpeciesNotFoundException when deleting non-existent species")
    void testDeleteSpeciesNotFound() {
        when(speciesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(SpeciesNotFoundException.class, () -> speciesService.deleteSpecies(1L));
    }
}