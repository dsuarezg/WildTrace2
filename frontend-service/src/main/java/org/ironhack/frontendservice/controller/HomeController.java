package org.ironhack.frontendservice.controller;

import org.ironhack.frontendservice.dto.SpeciesRequestDTO;
import org.ironhack.frontendservice.dto.SpeciesResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/species")
public class HomeController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String listSpecies(Model model) {
        ResponseEntity<SpeciesResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);
        model.addAttribute("speciesList", Arrays.asList(response.getBody()));
        return "species/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("species", new SpeciesRequestDTO());
        model.addAttribute("action", "create");
        return "species/form";
    }

    @PostMapping("/create")
    public String createSpecies(@ModelAttribute("species") SpeciesRequestDTO dto) {
        HttpEntity<SpeciesRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/species", request, SpeciesResponseDTO.class);
        return "redirect:/species";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SpeciesResponseDTO species = restTemplate.getForObject(
                gatewayBaseUrl + "/api/species/" + id, SpeciesResponseDTO.class);

        // Convertimos ResponseDTO a RequestDTO para el formulario
        SpeciesRequestDTO formDto = new SpeciesRequestDTO();
        formDto.setCommonName(species.getCommonName());
        formDto.setScientificName(species.getScientificName());
        formDto.setConservationStatus(species.getConservationStatus());

        model.addAttribute("species", formDto);
        model.addAttribute("speciesId", id);
        model.addAttribute("action", "edit");
        return "species/form";
    }

    @PostMapping("/update/{id}")
    public String updateSpecies(@PathVariable Long id, @ModelAttribute("species") SpeciesRequestDTO dto) {
        HttpEntity<SpeciesRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(
                gatewayBaseUrl + "/api/species/" + id,
                HttpMethod.PUT,
                request,
                Void.class
        );
        return "redirect:/species";
    }

    @PostMapping("/delete/{id}")
    public String deleteSpecies(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/species/" + id);
        return "redirect:/species";
    }
}
