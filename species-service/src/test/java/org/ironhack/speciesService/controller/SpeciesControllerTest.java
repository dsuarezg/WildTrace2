package org.ironhack.speciesService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.speciesService.dto.SpeciesRequestDTO;
import org.ironhack.speciesService.model.Species;
import org.ironhack.speciesService.repository.SpeciesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SpeciesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpeciesRepository speciesRepository;

    @BeforeEach
    void setUp() {
        speciesRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a species successfully (Lynx pardinus)")
    void testCreateSpecies() throws Exception {
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        dto.setCommonName("Lince ibérico");
        dto.setScientificName("Lynx pardinus");
        dto.setConservationStatus("Endangered");

        mockMvc.perform(post("/api/species")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.commonName").value("Lince ibérico"));
    }

    @Test
    @DisplayName("Should retrieve all species (including Cigüeña negra)")
    void testGetAllSpecies() throws Exception {
        Species stork = new Species(null, "Cigüeña negra", "Ciconia nigra", "Vulnerable");
        speciesRepository.save(stork);

        mockMvc.perform(get("/api/species"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].commonName").value("Cigüeña negra"));
    }

    @Test
    @DisplayName("Should retrieve species by ID (Gato montés)")
    void testGetSpeciesById() throws Exception {
        Species wildcat = new Species(null, "Gato montés", "Felis silvestris", "Least Concern");
        wildcat = speciesRepository.save(wildcat);

        mockMvc.perform(get("/api/species/" + wildcat.getSpeciesId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commonName").value("Gato montés"));
    }

    @Test
    @DisplayName("Should return 404 when retrieving non-existent species")
    void testGetSpeciesByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/species/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update species successfully (Águila imperial)")
    void testUpdateSpecies() throws Exception {
        Species eagle = new Species(null, "Águila imperial", "Aquila adalberti", "Endangered");
        eagle = speciesRepository.save(eagle);

        SpeciesRequestDTO updated = new SpeciesRequestDTO();
        updated.setCommonName("Águila imperial ibérica");
        updated.setScientificName("Aquila adalberti");
        updated.setConservationStatus("Vulnerable");

        mockMvc.perform(put("/api/species/" + eagle.getSpeciesId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.commonName").value("Águila imperial ibérica"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent species")
    void testUpdateSpeciesNotFound() throws Exception {
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        dto.setCommonName("Tejón");
        dto.setScientificName("Meles meles");
        dto.setConservationStatus("Least Concern");

        mockMvc.perform(put("/api/species/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete species successfully (Buitre negro)")
    void testDeleteSpecies() throws Exception {
        Species vulture = new Species(null, "Buitre negro", "Aegypius monachus", "Near Threatened");
        vulture = speciesRepository.save(vulture);

        mockMvc.perform(delete("/api/species/" + vulture.getSpeciesId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent species")
    void testDeleteSpeciesNotFound() throws Exception {
        mockMvc.perform(delete("/api/species/999"))
                .andExpect(status().isNotFound());
    }
}