package org.ironhack.sightingService.client;

import org.ironhack.sightingService.dto.ZoneResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zone-service")
public interface ZoneServiceClient {

    /****
     * Retrieves the details of a zone by its unique identifier.
     *
     * @param id the unique identifier of the zone
     * @return the details of the specified zone as a ZoneResponseDTO
     */
    @GetMapping("/api/zones/{id}")
    ZoneResponseDTO getZoneById(@PathVariable Long id);
}
