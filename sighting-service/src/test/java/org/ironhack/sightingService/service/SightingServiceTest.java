package org.ironhack.sightingService.service;

import org.ironhack.sightingService.client.SpeciesServiceClient;
import org.ironhack.sightingService.client.ZoneServiceClient;
import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.exception.SightingNotFoundException;
import org.ironhack.sightingService.exception.SpeciesNotFoundException;
import org.ironhack.sightingService.exception.ZoneNotFoundException;
import org.ironhack.sightingService.model.Sighting;
import org.ironhack.sightingService.repository.SightingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SightingServiceTest {

    @Mock
    private SightingRepository sightingRepository;

    @Mock
    private SpeciesServiceClient speciesServiceClient;

    @Mock
    private ZoneServiceClient zoneServiceClient;

    @InjectMocks
    private SightingService sightingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return all sightings")
    void testGetAll() {
        Sighting sighting = new Sighting(1L, 1L, 1L, LocalDate.now(), "Juan", "Visual", "Notas");
        when(sightingRepository.findAll()).thenReturn(List.of(sighting));

        List<SightingResponseDTO> result = sightingService.getAll();

        assertEquals(1, result.size());
        assertEquals("Juan", result.get(0).getObservedBy());
    }

    @Test
    @DisplayName("Should return a sighting by ID")
    void testGetById() {
        Sighting sighting = new Sighting(1L, 1L, 1L, LocalDate.now(), "Pedro", "Fotografía", "Avistado en zona húmeda");
        when(sightingRepository.findById(1L)).thenReturn(Optional.of(sighting));

        SightingResponseDTO result = sightingService.getById(1L);

        assertEquals("Pedro", result.getObservedBy());
    }

    @Test
    @DisplayName("Should throw when sighting ID does not exist")
    void testGetByIdNotFound() {
        when(sightingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(SightingNotFoundException.class, () -> sightingService.getById(99L));
    }

    @Test
    @DisplayName("Should save sighting after validating species and zone")
    void testSaveSighting() {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("Luis");
        dto.setMethod("Binoculars");
        dto.setNotes("Zona de pinares");

        when(speciesServiceClient.getSpeciesById(1L)).thenReturn(null);
        when(zoneServiceClient.getZoneById(1L)).thenReturn(null);

        Sighting saved = new Sighting(1L, 1L, 1L, dto.getDate(), dto.getObservedBy(), dto.getMethod(), dto.getNotes());
        when(sightingRepository.save(any(Sighting.class))).thenReturn(saved);

        SightingResponseDTO result = sightingService.save(dto);

        assertEquals("Luis", result.getObservedBy());
    }


    @Test
    @DisplayName("Should throw SpeciesNotFoundException when species is invalid")
    void testSaveInvalidSpecies() {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("Luis");
        dto.setMethod("Binoculars");
        dto.setNotes("Zona de pinares");


        doThrow(new RuntimeException()).when(speciesServiceClient).getSpeciesById(1L);

        assertThrows(SpeciesNotFoundException.class, () -> sightingService.save(dto));
    }

    @Test
    @DisplayName("Should throw ZoneNotFoundException when zone is invalid")
    void testSaveInvalidZone() {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("Luis");
        dto.setMethod("Binoculars");
        dto.setNotes("Zona de pinares");

        when(speciesServiceClient.getSpeciesById(1L)).thenReturn(null);
        when(zoneServiceClient.getZoneById(1L)).thenThrow(new RuntimeException());

        assertThrows(ZoneNotFoundException.class, () -> sightingService.save(dto));
    }

    @Test
    @DisplayName("Should delete a sighting by ID")
    void testDeleteSighting() {
        Sighting sighting = new Sighting(1L, 1L, 1L, LocalDate.now(), "Miguel", "App", null);
        when(sightingRepository.findById(1L)).thenReturn(Optional.of(sighting));
        doNothing().when(sightingRepository).delete(sighting);

        assertDoesNotThrow(() -> sightingService.delete(1L));
    }

    @Test
    @DisplayName("Should throw SightingNotFoundException when deleting non-existent sighting")
    void testDeleteNotFound() {
        when(sightingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(SightingNotFoundException.class, () -> sightingService.delete(999L));
    }
}