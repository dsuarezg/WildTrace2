package org.ironhack.frontendservice.controller;

import org.ironhack.frontendservice.dto.ZoneRequestDTO;
import org.ironhack.frontendservice.dto.ZoneResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
@RequestMapping("/zones")
public class ZoneController {

    private final RestTemplate restTemplate;

    @Value("${wildtrace.gateway.base-url}")
    private String gatewayBaseUrl;

    /**
     * Constructs a ZoneController with the specified RestTemplate for backend API communication.
     *
     * @param restTemplate the RestTemplate used to perform HTTP requests to the external gateway API
     */
    public ZoneController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /****
     * Handles GET requests to "/zones" by retrieving the list of zones from the backend API and adding it to the model for display.
     *
     * @param model the model to which the list of zones will be added as "zoneList"
     * @return the name of the view to render the zone list
     */
    @GetMapping
    public String listZones(Model model) {
        ResponseEntity<ZoneResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class);
        model.addAttribute("zoneList", Arrays.asList(response.getBody()));
        return "zones/list";
    }

    /**
     * Displays the form for creating a new zone.
     *
     * Adds a blank ZoneRequestDTO to the model for form binding and sets the action attribute to indicate creation mode.
     *
     * @return the view name for the zone creation form
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("zone", new ZoneRequestDTO());
        model.addAttribute("action", "create");
        return "zones/form";
    }

    /**
     * Handles the creation of a new zone by submitting the provided data to the backend API.
     *
     * Redirects to the zone list view after successfully creating the zone.
     */
    @PostMapping("/create")
    public String createZone(@ModelAttribute("zone") ZoneRequestDTO dto) {
        HttpEntity<ZoneRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/zones", request, ZoneResponseDTO.class);
        return "redirect:/zones";
    }

    /**
     * Displays the edit form for an existing zone by retrieving its details from the backend API.
     *
     * Populates the model with the zone's current data for form binding, the zone ID, and an action indicator.
     *
     * @param id the ID of the zone to edit
     * @param model the model to populate with zone data and form attributes
     * @return the view name for the zone edit form
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        ZoneResponseDTO zone = restTemplate.getForObject(
                gatewayBaseUrl + "/api/zones/" + id, ZoneResponseDTO.class);

        ZoneRequestDTO formDto = new ZoneRequestDTO();
        formDto.setZoneName(zone.getZoneName());
        formDto.setLatitude(zone.getLatitude());
        formDto.setLongitude(zone.getLongitude());

        model.addAttribute("zone", formDto);
        model.addAttribute("zoneId", id);
        model.addAttribute("action", "edit");
        return "zones/form";
    }

    /**
     * Updates an existing zone by sending the provided data to the backend API and redirects to the zone list view.
     *
     * @param id the ID of the zone to update
     * @param dto the updated zone data
     * @return a redirect instruction to the zone list page
     */
    @PostMapping("/update/{id}")
    public String updateZone(@PathVariable Long id, @ModelAttribute("zone") ZoneRequestDTO dto) {
        HttpEntity<ZoneRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.exchange(
                gatewayBaseUrl + "/api/zones/" + id,
                HttpMethod.PUT,
                request,
                Void.class
        );
        return "redirect:/zones";
    }

    /****
     * Handles POST requests to delete a zone by its ID and redirects to the list of zones.
     *
     * @param id the unique identifier of the zone to be deleted
     * @return a redirect string to the zones list page
     */
    @PostMapping("/delete/{id}")
    public String deleteZone(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/zones/" + id);
        return "redirect:/zones";
    }

    /**
     * Constructs a Mapbox static map URL centered at the specified latitude and longitude with a red pin marker.
     *
     * @param lat the latitude for the map center and marker
     * @param lon the longitude for the map center and marker
     * @return a URL string for a Mapbox static satellite map with a red pin at the given coordinates
     */
    private String generateMapUrl(Double lat, Double lon) {
        return "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/"
                + "pin-s+ff0000(" + lon + "," + lat + ")/"
                + lon + "," + lat + ",13/600x400?access_token=" + mapboxToken;
    }

    @Value("${mapbox.token}")
    private String mapboxToken;

}