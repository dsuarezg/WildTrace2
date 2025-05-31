/*
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
@RequestMapping(“/sightings”)
public class SightingController {

    private final RestTemplate restTemplate;

    @Value(“${wildtrace.gateway.base-url}”)
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
*/
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

import java.util.*;

@Controller
@RequestMapping("/sightings")
public class SightingController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    @Value("${mapbox.token}")
    private String mapboxToken;



    /**
     * Constructs a SightingController with the specified RestTemplate for backend API communication.
     *
     * @param restTemplate the RestTemplate used to perform REST calls to the backend service
     */
    public SightingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /****
     * Handles GET requests to list all wildlife sightings.
     *
     * Retrieves all sightings from the backend API, along with species and zone data, and adds them to the model for display in the sightings list view.
     *
     * @return the name of the view to render the sightings list
     */
    @GetMapping
    public String listSightings(Model model) {
        SightingResponseDTO[] sightings = restTemplate.getForObject(gatewayBaseUrl + "/api/sightings", SightingResponseDTO[].class);
        model.addAttribute("sightingList", Arrays.asList(sightings));

        // Mapeamos los IDs a nombres para mostrar
        Map<Long, String> speciesNames = new HashMap<>();
        Map<Long, String> zoneNames = new HashMap<>();

        Arrays.stream(restTemplate.getForObject(gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class))
                .forEach(s -> speciesNames.put(s.getId(), s.getCommonName()));
        Arrays.stream(restTemplate.getForObject(gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class))
                .forEach(z -> zoneNames.put(z.getZoneId(), z.getZoneName()));

        model.addAttribute("speciesMap", speciesNames);
        model.addAttribute("zoneMap", zoneNames);

        Map<Long, String> zoneMapUrl = new HashMap<>();
        ZoneResponseDTO[] zones = restTemplate.getForObject(gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class);

        for (ZoneResponseDTO z : zones) {
            zoneMapUrl.put(z.getZoneId(), generateMapUrl(z.getLatitude(), z.getLongitude()));
        }
        model.addAttribute("zoneMapUrl", zoneMapUrl);


        return "sightings/list";
    }

    /****
     * Handles GET requests to display the form for creating a new sighting.
     *
     * Adds an empty sighting DTO and action indicator to the model, and loads species and zone lists for form selection.
     *
     * @return the name of the view for the sighting creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("sighting", new SightingRequestDTO());
        model.addAttribute("action", "create");
        loadSpeciesAndZones(model);
        return "sightings/form";
    }

    /**
     * Handles the creation of a new wildlife sighting by submitting the provided data to the backend API.
     *
     * Redirects to the sightings list view after successful creation.
     */
    @PostMapping("/create")
    public String createSighting(@ModelAttribute("sighting") SightingRequestDTO dto) {
        HttpEntity<SightingRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/sightings", request, SightingResponseDTO.class);
        return "redirect:/sightings";
    }

    /**
     * Displays the edit form for an existing sighting, pre-populated with the sighting's current data.
     *
     * Retrieves the sighting by its ID from the backend API, maps its details into a form DTO, and adds it to the model along with species and zone options for selection.
     *
     * @param id the ID of the sighting to edit
     * @return the view name for the sighting edit form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        SightingResponseDTO response = restTemplate.getForObject(gatewayBaseUrl + "/api/sightings/" + id, SightingResponseDTO.class);

        SightingRequestDTO form = new SightingRequestDTO();
        form.setSpeciesId(response.getSpeciesId());
        form.setZoneId(response.getZoneId());
        form.setDate(response.getDate());
        form.setObservedBy(response.getObservedBy());
        form.setMethod(response.getMethod());
        form.setNotes(response.getNotes());

        model.addAttribute("sighting", form);
        model.addAttribute("sightingId", id);
        model.addAttribute("action", "edit");
        loadSpeciesAndZones(model);
        return "sightings/form";
    }

    /**
     * Updates an existing sighting with the provided data and redirects to the sightings list.
     *
     * @param id the ID of the sighting to update
     * @param dto the updated sighting data
     * @return a redirect to the sightings list view
     */
    @PostMapping("/update/{id}")
    public String updateSighting(@PathVariable Long id, @ModelAttribute("sighting") SightingRequestDTO dto) {
        HttpEntity<SightingRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(gatewayBaseUrl + "/api/sightings/" + id, HttpMethod.PUT, request, Void.class);
        return "redirect:/sightings";
    }

    /**
     * Deletes a sighting by its ID and redirects to the sightings list view.
     *
     * @param id the ID of the sighting to delete
     * @return a redirect to the sightings list page
     */
    @PostMapping("/delete/{id}")
    public String deleteSighting(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/sightings/" + id);
        return "redirect:/sightings";
    }

    /**
     * Fetches species and zone data from the backend API and adds them to the model for use in forms.
     *
     * Adds the lists as model attributes "speciesList" and "zoneList".
     */
    private void loadSpeciesAndZones(Model model) {
        SpeciesResponseDTO[] species = restTemplate.getForObject(gatewayBaseUrl + "/api/species", SpeciesResponseDTO[].class);
        ZoneResponseDTO[] zones = restTemplate.getForObject(gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class);
        model.addAttribute("speciesList", Arrays.asList(species));
        model.addAttribute("zoneList", Arrays.asList(zones));
    }

    private String generateMapUrl(Double lat, Double lon) {
        return "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/"
                + "pin-s+ff0000(" + lon + "," + lat + "),"
                + "pin-s+0000ff(" + (lon+0.01) + "," + (lat+0.01) + ")/"
                + lon + "," + lat + ",14,0,0/800x600@2x"
                + "?access_token=" + mapboxToken;
    }

}
