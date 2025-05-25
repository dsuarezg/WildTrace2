package org.ironhack.sightingService.functional;

import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SightingFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should create and fetch sighting successfully")
    void createAndFetchSighting() {
        SightingRequestDTO dto = new SightingRequestDTO();
        dto.setZoneId(1L);      // Asegúrate de que esta zona y especie existen
        dto.setSpeciesId(1L);
        dto.setDate(LocalDate.now());
        dto.setObservedBy("María");
        dto.setMethod("Telescopio");
        dto.setNotes("Búho real cerca del embalse");

        ResponseEntity<SightingResponseDTO> response = restTemplate.postForEntity(
                "/api/sightings", dto, SightingResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());

        Long id = response.getBody().getId();

        SightingResponseDTO fetched = restTemplate.getForObject(
                "/api/sightings/" + id, SightingResponseDTO.class);

        assertEquals("María", fetched.getObservedBy());
        assertEquals("Telescopio", fetched.getMethod());
        assertEquals("Búho real cerca del embalse", fetched.getNotes());
    }

    @Test
    @DisplayName("Should return 404 when fetching non-existent sighting")
    void getNonExistentSighting() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/sightings/9999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
