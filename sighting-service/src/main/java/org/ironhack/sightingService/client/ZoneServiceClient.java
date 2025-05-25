package org.ironhack.sightingService.client;

import org.ironhack.sightingService.dto.ZoneResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "zone-service")
public interface ZoneServiceClient {

    /****
     * Retrieves zone details for the specified zone ID from the zone service.
     *
     * @param id the unique identifier of the zone to retrieve
     * @return a ZoneResponseDTO containing the zone's information
     */
    @GetMapping("/api/zones/{id}")
    ZoneResponseDTO getZoneById(@PathVariable Long id);
}
