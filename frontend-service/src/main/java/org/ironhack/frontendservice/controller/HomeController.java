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
     * Initializes the HomeController with a RestTemplate for backend API communication.
     *
     * @param restTemplate the RestTemplate used to interact with the external species API
     */
    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
         * Handles GET requests to display all species.
         *
         * Fetches the list of species from the backend API, adds it to the model as "speciesList", and returns the view for displaying the species list.
         *
         * @return the name of the view for listing species
         */
    @GetMapping
    public String listSpecies(Model model) {
        ResponseEntity<SpeciesResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);
        model.addAttribute("speciesList", Arrays.asList(response.getBody()));
        return "species/list";
    }

    /**
     * Handles GET requests to display the form for creating a new species.
     *
     * Adds an empty SpeciesRequestDTO and an action indicator to the model for form binding in the creation view.
     *
     * @return the name of the view for the species creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("species", new SpeciesRequestDTO());
        model.addAttribute("action", "create");
        return "species/form";
    }

    /**
     * Processes the submission of the new species form and creates a species via the backend API.
     *
     * After successfully creating the species, redirects to the species list page.
     *
     * @param dto the data for the species to be created
     * @return a redirect to the species list view
     */
    @PostMapping("/create")
    public String createSpecies(@ModelAttribute("species") SpeciesRequestDTO dto) {
        HttpEntity<SpeciesRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/species", request, SpeciesResponseDTO.class);
        return "redirect:/species";
    }

    /**
     * Handles GET requests to display the edit form for a species.
     *
     * Fetches the species data by ID from the backend API, prepares it for form binding, and populates the model with the species information and form attributes for editing.
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
     * Handles POST requests to update an existing species with new data.
     *
     * Sends the updated species information to the backend API and redirects to the species list page upon completion.
     *
     * @param id the unique identifier of the species to update
     * @param dto the new data for the species
     * @return a redirect string to the species list view
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
     * Handles POST requests to delete a species by its ID and redirects to the species list page.
     *
     * @param id the ID of the species to be deleted
     * @return a redirect to the species list view after deletion
     */
    @PostMapping("/delete/{id}")
    public String deleteSpecies(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/species/" + id);
        return "redirect:/species";
    }
}
