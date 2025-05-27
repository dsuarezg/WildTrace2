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

@Controller
@RequestMapping("/species")
public class SpeciesController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    /**
     * Constructs a SpeciesController with the specified RestTemplate for API communication.
     *
     * @param restTemplate the RestTemplate used to interact with the external species API
     */
    public SpeciesController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Handles GET requests to display a list of species.
     *
     * Retrieves all species from the backend API, adds them to the model as "speciesList", and returns the view for listing species.
     *
     * @param model the model to which the species list is added
     * @return the name of the view displaying the species list
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
     * Adds an empty SpeciesRequestDTO to the model for form binding and sets the action attribute to "create".
     *
     * @return the view name for the species creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("species", new SpeciesRequestDTO());
        model.addAttribute("action", "create");
        return "species/form";
    }

    /****
     * Handles the submission of the species creation form and creates a new species via the backend API.
     *
     * @return a redirect to the species list view after successful creation
     */
    @PostMapping("/create")
    public String createSpecies(@ModelAttribute("species") SpeciesRequestDTO dto) {
        HttpEntity<SpeciesRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/species", request, SpeciesResponseDTO.class);
        return "redirect:/species";
    }

    /**
     * Displays the edit form for an existing species by fetching its data and populating the form fields.
     *
     * @param id the ID of the species to edit
     * @param model the model to which form data and attributes are added
     * @return the view name for the species edit form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SpeciesResponseDTO species = restTemplate.getForObject(
                gatewayBaseUrl + "/api/species/" + id, SpeciesResponseDTO.class);

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
     * @return a redirect to the species list view
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
     * Deletes a species with the specified ID by sending a DELETE request to the backend API.
     *
     * @param id the ID of the species to delete
     * @return a redirect to the species list view
     */
    @PostMapping("/delete/{id}")
    public String deleteSpecies(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/species/" + id);
        return "redirect:/species";
    }
}
