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

    /****
     * Constructs a HomeController with the specified RestTemplate for communicating with the external species API.
     */
    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Handles GET requests to display a list of all species.
     *
     * Retrieves species data from the backend API, adds the list to the model under "speciesList", and returns the view for displaying the species list.
     *
     * @return the name of the Thymeleaf view for listing species
     */
    @GetMapping
    public String listSpecies(Model model) {
        ResponseEntity<SpeciesResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);
        model.addAttribute("speciesList", Arrays.asList(response.getBody()));
        return "species/list";
    }

    /**
     * Displays the form for creating a new species.
     *
     * Adds an empty species DTO and a creation action indicator to the model for form binding.
     *
     * @return the view name for the species creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("species", new SpeciesRequestDTO());
        model.addAttribute("action", "create");
        return "species/form";
    }

    /**
     * Handles the submission of the create species form and sends a request to create a new species via the backend API.
     *
     * Redirects to the species list page after successful creation.
     */
    @PostMapping("/create")
    public String createSpecies(@ModelAttribute("species") SpeciesRequestDTO dto) {
        HttpEntity<SpeciesRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/species", request, SpeciesResponseDTO.class);
        return "redirect:/species";
    }

    /**
     * Displays the form for editing an existing species by fetching its data from the backend service.
     *
     * Retrieves the species identified by the given ID, converts its data for form binding, and populates the model for rendering the edit form view.
     *
     * @param id the ID of the species to edit
     * @param model the model to populate with species data and form attributes
     * @return the name of the Thymeleaf view for the species edit form
     */
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

    /**
     * Updates an existing species by sending the provided data to the backend API.
     *
     * @param id the ID of the species to update
     * @param dto the updated species data
     * @return a redirect to the species list page after the update
     */
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

    /**
     * Deletes a species by its ID via the backend API and redirects to the species list page.
     *
     * @param id the unique identifier of the species to delete
     * @return a redirect instruction to the species list view
     */
    @PostMapping("/delete/{id}")
    public String deleteSpecies(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/species/" + id);
        return "redirect:/species";
    }
}
