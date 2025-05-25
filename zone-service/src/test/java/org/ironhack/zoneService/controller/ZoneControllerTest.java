package org.ironhack.zoneService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.model.Zone;
import org.ironhack.zoneService.repository.ZoneRepository;
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
class ZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ZoneRepository zoneRepository;

    @BeforeEach
    void setUp() {
        zoneRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create a zone successfully (Guadarrama)")
    void testCreateZone() throws Exception {
        ZoneRequestDTO dto = new ZoneRequestDTO();
        dto.setZoneName("Parque Nacional de Guadarrama");
        dto.setLatitude(40.785);
        dto.setLongitude(-3.739);

        mockMvc.perform(post("/api/zones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.zoneName").value("Parque Nacional de Guadarrama"));
    }

    @Test
    @DisplayName("Should retrieve all zones (including Alto Tajo)")
    void testGetAllZones() throws Exception {
        Zone zone = new Zone(null, "Alto Tajo", 40.712, -2.345);
        zoneRepository.save(zone);

        mockMvc.perform(get("/api/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].zoneName").value("Alto Tajo"));
    }

    @Test
    @DisplayName("Should retrieve zone by ID (Sierra Norte)")
    void testGetZoneById() throws Exception {
        Zone zone = new Zone(null, "Sierra Norte", 40.881, -3.623);
        zone = zoneRepository.save(zone);

        mockMvc.perform(get("/api/zones/" + zone.getZoneId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneName").value("Sierra Norte"));
    }

    @Test
    @DisplayName("Should return 404 when retrieving non-existent zone")
    void testGetZoneByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/zones/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should update zone successfully (Valle del Lozoya)")
    void testUpdateZone() throws Exception {
        Zone zone = new Zone(null, "Zona vieja", 40.0, -3.0);
        zone = zoneRepository.save(zone);

        ZoneRequestDTO updated = new ZoneRequestDTO();
        updated.setZoneName("Valle del Lozoya");
        updated.setLatitude(40.980);
        updated.setLongitude(-3.670);

        mockMvc.perform(put("/api/zones/" + zone.getZoneId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.zoneName").value("Valle del Lozoya"));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent zone")
    void testUpdateZoneNotFound() throws Exception {
        ZoneRequestDTO dto = new ZoneRequestDTO();
        dto.setZoneName("La Hiruela");
        dto.setLatitude(41.000);
        dto.setLongitude(-3.570);

        mockMvc.perform(put("/api/zones/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should delete zone successfully (Montejo de la Sierra)")
    void testDeleteZone() throws Exception {
        Zone zone = new Zone(null, "Montejo de la Sierra", 41.053, -3.460);
        zone = zoneRepository.save(zone);

        mockMvc.perform(delete("/api/zones/" + zone.getZoneId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 when deleting non-existent zone")
    void testDeleteZoneNotFound() throws Exception {
        mockMvc.perform(delete("/api/zones/999"))
                .andExpect(status().isNotFound());
    }
}