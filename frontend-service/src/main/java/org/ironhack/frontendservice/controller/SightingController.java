package org.ironhack.frontendservice.controller;

import org.ironhack.frontendservice.dto.SightingRequestDTO;
import org.ironhack.frontendservice.dto.SightingResponseDTO;
import org.ironhack.frontendservice.dto.SpeciesResponseDTO;
import org.ironhack.frontendservice.dto.ZoneResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/sightings")
public class SightingController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    public SightingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String listSightings(Model model) {
        ResponseEntity<SightingResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/sightings", SightingResponseDTO[].class);

        model.addAttribute("sightingList", Arrays.asList(response.getBody()));
        return "sightings/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sighting", new SightingRequestDTO());
        model.addAttribute("speciesOptions", fetchSpecies());
        model.addAttribute("zoneOptions", fetchZones());
        model.addAttribute("action", "create");
        return "sightings/form";
    }

    @PostMapping("/create")
    public String createSighting(@ModelAttribute("sighting") SightingRequestDTO dto) {
        HttpEntity<SightingRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/sightings", request, SightingResponseDTO.class);
        return "redirect:/sightings";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SightingResponseDTO existing = restTemplate.getForObject(
                gatewayBaseUrl + "/api/sightings/" + id, SightingResponseDTO.class);

        SightingRequestDTO formDto = new SightingRequestDTO();
        formDto.setZoneId(existing.getZoneId());
        formDto.setSpeciesId(existing.getSpeciesId());
        formDto.setDate(existing.getDate());
        formDto.setObservedBy(existing.getObservedBy());
        formDto.setMethod(existing.getMethod());
        formDto.setNotes(existing.getNotes());

        model.addAttribute("sighting", formDto);
        model.addAttribute("sightingId", id);
        model.addAttribute("speciesOptions", fetchSpecies());
        model.addAttribute("zoneOptions", fetchZones());
        model.addAttribute("action", "edit");

        return "sightings/form";
    }

    @PostMapping("/update/{id}")
    public String updateSighting(@PathVariable Long id, @ModelAttribute("sighting") SightingRequestDTO dto) {
        HttpEntity<SightingRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(gatewayBaseUrl + "/api/sightings/" + id, HttpMethod.PUT, request, Void.class);
        return "redirect:/sightings";
    }

    @PostMapping("/delete/{id}")
    public String deleteSighting(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/sightings/" + id);
        return "redirect:/sightings";
    }

    // Métodos auxiliares para cargar listas de especies y zonas
    private List<SpeciesResponseDTO> fetchSpecies() {
        ResponseEntity<SpeciesResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);
        return Arrays.asList(response.getBody());
    }

    private List<ZoneResponseDTO> fetchZones() {
        ResponseEntity<ZoneResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class);
        return Arrays.asList(response.getBody());
    }
}
