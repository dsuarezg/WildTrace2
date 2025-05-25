package org.ironhack.sightingService.client;

import org.ironhack.sightingService.dto.SpeciesResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("species-service")
public interface SpeciesServiceClient {

    @GetMapping("/api/species/{id}")
    SpeciesResponseDTO getSpeciesById(@PathVariable Long id);
}
