package org.ironhack.zoneService.functional;

import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.dto.ZoneResponseDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneFunctionalTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DisplayName("Should create and fetch zone successfully")
    void createAndFetchZone() {
        ZoneRequestDTO dto = new ZoneRequestDTO();
        dto.setZoneName("Valle del Lozoya");
        dto.setLatitude(40.980);
        dto.setLongitude(-3.670);

        ResponseEntity<ZoneResponseDTO> response = restTemplate.postForEntity(
                "/api/zones", dto, ZoneResponseDTO.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Long id = response.getBody().getZoneId();

        ZoneResponseDTO fetched = restTemplate.getForObject(
                "/api/zones/" + id, ZoneResponseDTO.class);

        assertEquals("Valle del Lozoya", fetched.getZoneName());
        assertEquals(40.980, fetched.getLatitude());
        assertEquals(-3.670, fetched.getLongitude());
    }

    @Test
    @DisplayName("Should return 404 when fetching non-existent zone")
    void getNonExistentZone() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/zones/9999", String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
