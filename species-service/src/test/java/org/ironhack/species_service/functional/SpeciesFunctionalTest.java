package org.ironhack.species_service.functional;

import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpeciesFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should create and fetch species successfully")
    void createAndFetchSpecies() {
        SpeciesRequestDTO dto = new SpeciesRequestDTO();
        dto.setCommonName("Lince ibérico");
        dto.setScientificName("Lynx pardinus");
        dto.setConservationStatus("Endangered");

        ResponseEntity<SpeciesResponseDTO> response = restTemplate.postForEntity(
                "/api/species", dto, SpeciesResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Long id = response.getBody().getId();

        SpeciesResponseDTO fetched = restTemplate.getForObject(
                "/api/species/" + id, SpeciesResponseDTO.class);

        assertEquals("Lince ibérico", fetched.getCommonName());
    }

    @Test
    @DisplayName("Should return 404 when fetching non-existent species")
    void getNonExistentSpecies() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/species/9999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
