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

    public ZoneController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String listZones(Model model) {
        ResponseEntity<ZoneResponseDTO[]> response = restTemplate.getForEntity(
                gatewayBaseUrl + "/api/zones", ZoneResponseDTO[].class);
        model.addAttribute("zoneList", Arrays.asList(response.getBody()));
        return "zones/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("zone", new ZoneRequestDTO());
        model.addAttribute("action", "create");
        return "zones/form";
    }

    @PostMapping("/create")
    public String createZone(@ModelAttribute("zone") ZoneRequestDTO dto) {
        HttpEntity<ZoneRequestDTO> request = new HttpEntity<>(dto);
        restTemplate.postForEntity(gatewayBaseUrl + "/api/zones", request, ZoneResponseDTO.class);
        return "redirect:/zones";
    }

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

    @PostMapping("/delete/{id}")
    public String deleteZone(@PathVariable Long id) {
        restTemplate.delete(gatewayBaseUrl + "/api/zones/" + id);
        return "redirect:/zones";
    }
}
