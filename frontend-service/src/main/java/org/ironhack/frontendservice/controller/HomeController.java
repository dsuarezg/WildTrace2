package org.ironhack.frontendservice.controller;

import org.ironhack.frontendservice.dto.SpeciesRequestDTO;
import org.ironhack.frontendservice.dto.SpeciesResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/")
    public String home() {
        return "index"; // templates/index.html
    }

    @GetMapping("/species")
    public String listSpecies(Model model) {
        ResponseEntity<SpeciesResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);

        List<SpeciesResponseDTO> speciesList = Arrays.asList(response.getBody());
        model.addAttribute("speciesList", speciesList);

        return "species/list"; // templates/species/list.html
    }

    @GetMapping("/species/new")
    public String newSpeciesForm(Model model) {
        model.addAttribute("species", new SpeciesResponseDTO());
        model.addAttribute("formAction", "/species");
        model.addAttribute("title", "Nueva Especie");
        return "species/form";
    }

    @GetMapping("/species/edit/{id}")
    public String editSpeciesForm(@PathVariable Long id, Model model) {
        SpeciesResponseDTO dto = restTemplate.getForObject(gatewayBaseUrl + "/api/species/" + id, SpeciesResponseDTO.class);
        model.addAttribute("species", dto);
        model.addAttribute("formAction", "/species/" + id);
        model.addAttribute("title", "Editar Especie");
        return "species/form";
    }

    @PostMapping("/species")
    public String createSpecies(@ModelAttribute SpeciesResponseDTO species) {
        restTemplate.postForEntity(gatewayBaseUrl + "/api/species", species, Void.class);
        return "redirect:/species";
    }

    @PostMapping("/species/{id}")
    public String updateSpecies(@PathVariable Long id, @ModelAttribute SpeciesResponseDTO species) {
        restTemplate.put(gatewayBaseUrl + "/api/species/" + id, species);
        return "redirect:/species";
    }

    @GetMapping("/species/delete/{id}")
    public String deleteSpecies(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/species/" + id);
        return "redirect:/species";
    }


}
