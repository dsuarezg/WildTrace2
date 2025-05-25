package org.ironhack.sightingService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.sightingService.client.SpeciesServiceClient;
import org.ironhack.sightingService.client.ZoneServiceClient;
import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SpeciesResponseDTO;
import org.ironhack.sightingService.dto.ZoneResponseDTO;
import org.ironhack.sightingService.model.Sighting;
import org.ironhack.sightingService.repository.SightingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SightingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SightingRepository sightingRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SpeciesServiceClient speciesServiceClient;

    @MockBean
    private ZoneServiceClient zoneServiceClient;

    @BeforeEach
    void setUp() {
        sightingRepository.deleteAll();

        SpeciesResponseDTO species = new SpeciesResponseDTO();
        species.setSpeciesId(1L);
        species.setCommonName("Lobo");
        species.setScientificName("Canis lupus");
        species.setConservationStatus("Endangered");

        ZoneResponseDTO zone = new ZoneResponseDTO();
        zone.setZoneId(1L);
        zone.setZoneName("Montejo");
        zone.setLatitude(41.1);
        zone.setLongitude(-3.5);

        when(speciesServiceClient.getSpeciesById(1L)).thenReturn(species);
        when(zoneServiceClient.getZoneById(1L)).thenReturn(zone);

    }

    @Test
    @DisplayName("Should return 404 when retrieving non-existent sighting")
    void testGetSightingByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/sightings/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    @DisplayName("Should delete sighting successfully")
    void testDeleteSighting() throws Exception {
        Sighting sighting = new Sighting(null, 1L, 1L, LocalDate.now(), "Luis", "Binoculars", "Montejo");
        sighting = sightingRepository.save(sighting);

        mockMvc.perform(delete("/api/sightings/" + sighting.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent sighting")
    void testDeleteSightingNotFound() throws Exception {
        mockMvc.perform(delete("/api/sightings/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    @DisplayName("Should retrieve all sightings (including Águila real)")
    void testGetAllSightings() throws Exception {
        Sighting s = new Sighting(null, 1L, 1L, LocalDate.now(), "Carlos", "Cámara", "Águila real");
        sightingRepository.save(s);

        mockMvc.perform(get("/api/sightings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].observedBy").value("Carlos"));
    }

    @Test
    @DisplayName("Should retrieve sighting by ID (Zorro rojo)")
    void testGetSightingById() throws Exception {
        Sighting s = new Sighting(null, 1L, 1L, LocalDate.now(), "Ana", "Prismáticos", "Zorro rojo");
        s = sightingRepository.save(s);

        mockMvc.perform(get("/api/sightings/" + s.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.observedBy").value("Ana"));
    }

    @Test
    @DisplayName("Should create a sighting successfully (Lobo)")
    void testCreateSighting() throws Exception {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("Juan");
        dto.setMethod("Cámara trampa");
        dto.setNotes("Lobo ibérico");

        mockMvc.perform(post("/api/sightings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.observedBy").value("Juan"));
    }

    @Test
    @DisplayName("Should update sighting successfully (Ciervo)")
    void testUpdateSighting() throws Exception {
        Sighting original = new Sighting(null, 1L, 1L, LocalDate.now(), "Pedro", "Observación directa", "Ciervo común");
        original = sightingRepository.save(original);

        SightingRequestDTO update = new SightingRequestDTO();
        update.setZoneId(1L);
        update.setSpeciesId(1L);
        update.setDate(LocalDate.now());
        update.setObservedBy("Pedro");
        update.setMethod("Observación directa");
        update.setNotes("Ciervo rojo adulto");

        mockMvc.perform(put("/api/sightings/" + original.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Ciervo rojo adulto"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent sighting")
    void testUpdateSightingNotFound() throws Exception {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("Ignacio");
        dto.setMethod("Prismáticos");
        dto.setNotes("Tejón");

        mockMvc.perform(put("/api/sightings/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }



}
