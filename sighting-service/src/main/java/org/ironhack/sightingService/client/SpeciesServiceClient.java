package org.ironhack.sightingService.client;

import org.ironhack.sightingService.dto.SpeciesResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("species-service")
public interface SpeciesServiceClient {

    /****
     * Retrieves species data from the species service by its unique identifier.
     *
     * @param id the unique identifier of the species
     * @return the species data as a SpeciesResponseDTO
     */
    @GetMapping("/api/species/{id}")
    SpeciesResponseDTO getSpeciesById(@PathVariable Long id);
}
