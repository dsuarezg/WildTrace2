package org.ironhack.sightingService.client;

import org.ironhack.sightingService.dto.SpeciesResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("species-service")
public interface SpeciesServiceClient {

    /****
     * Retrieves species information by its unique identifier from the species service.
     *
     * @param id the unique identifier of the species
     * @return a SpeciesResponseDTO containing the species data
     */
    @GetMapping("/api/species/{id}")
    SpeciesResponseDTO getSpeciesById(@PathVariable Long id);
}
