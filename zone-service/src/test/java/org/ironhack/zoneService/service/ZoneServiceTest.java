package org.ironhack.zoneService.service;

import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.dto.ZoneResponseDTO;
import org.ironhack.zoneService.exception.ZoneNotFoundException;
import org.ironhack.zoneService.model.Zone;
import org.ironhack.zoneService.repository.ZoneRepository;
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

class ZoneServiceTest {

    @Mock
    private ZoneRepository zoneRepository;

    @InjectMocks
    private ZoneService zoneService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return list of zones when data exists")
    void testGetAllReturnsZones() {
        Zone zone = new Zone(1L, "Sierra Norte", 40.750, -3.570);
        when(zoneRepository.findAll()).thenReturn(List.of(zone));

        List<ZoneResponseDTO> result = zoneService.getAll();

        assertEquals(1, result.size());
        assertEquals("Sierra Norte", result.get(0).getZoneName());
    }

    @Test
    @DisplayName("Should return empty list when no zones exist")
    void testGetAllReturnsEmptyList() {
        when(zoneRepository.findAll()).thenReturn(List.of());

        List<ZoneResponseDTO> result = zoneService.getAll();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return a zone by ID")
    void testGetByIdSuccess() {
        Zone zone = new Zone(1L, "Alto Tajo", 40.712, -2.345);
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));

        ZoneResponseDTO result = zoneService.getById(1L);

        assertEquals("Alto Tajo", result.getZoneName());
        assertEquals(40.712, result.getLatitude());
    }

    @Test
    @DisplayName("Should throw ZoneNotFoundException when ID not found")
    void testGetByIdNotFound() {
        when(zoneRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ZoneNotFoundException.class, () -> zoneService.getById(1L));
    }

    @Test
    @DisplayName("Should save and return zone")
    void testSaveZone() {
        ZoneRequestDTO dto = new ZoneRequestDTO();
        dto.setZoneName("Guadarrama");
        dto.setLatitude(40.783);
        dto.setLongitude(-3.721);

        Zone saved = new Zone(1L, "Guadarrama", 40.783, -3.721);
        when(zoneRepository.save(any(Zone.class))).thenReturn(saved);

        ZoneResponseDTO result = zoneService.save(dto);

        assertEquals("Guadarrama", result.getZoneName());
    }

    @Test
    @DisplayName("Should update existing zone")
    void testUpdateZoneSuccess() {
        Zone existing = new Zone(1L, "Old", 40.0, -3.0);
        ZoneRequestDTO dto = new ZoneRequestDTO();
        dto.setZoneName("Nueva Zona");
        dto.setLatitude(41.0);
        dto.setLongitude(-3.5);

        when(zoneRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(zoneRepository.save(any(Zone.class))).thenAnswer(inv -> inv.getArgument(0));

        ZoneResponseDTO result = zoneService.update(1L, dto);

        assertEquals("Nueva Zona", result.getZoneName());
        assertEquals(41.0, result.getLatitude());
    }

    @Test
    @DisplayName("Should throw ZoneNotFoundException when updating non-existent zone")
    void testUpdateZoneNotFound() {
        ZoneRequestDTO dto = new ZoneRequestDTO();
        when(zoneRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ZoneNotFoundException.class, () -> zoneService.update(1L, dto));
    }

    @Test
    @DisplayName("Should delete zone when found")
    void testDeleteZoneSuccess() {
        Zone zone = new Zone(1L, "El Paular", 40.900, -3.700);
        when(zoneRepository.findById(1L)).thenReturn(Optional.of(zone));
        doNothing().when(zoneRepository).delete(zone);

        assertDoesNotThrow(() -> zoneService.delete(1L));
    }

    @Test
    @DisplayName("Should throw ZoneNotFoundException when deleting non-existent zone")
    void testDeleteZoneNotFound() {
        when(zoneRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ZoneNotFoundException.class, () -> zoneService.delete(1L));
    }
}
