package org.ironhack.zoneService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.dto.ZoneResponseDTO;
import org.ironhack.zoneService.exception.ZoneNotFoundException;
import org.ironhack.zoneService.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@Tag(name = "Zones", description = "Operations related to natural zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all zones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zones retrieved successfully")
    })
    public ResponseEntity<List<ZoneResponseDTO>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a zone by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zone found"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    public ResponseEntity<ZoneResponseDTO> getZoneById(@PathVariable Long id) {
        return ResponseEntity.ok(zoneService.getById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new zone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Zone created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<ZoneResponseDTO> createZone(@RequestBody @Valid ZoneRequestDTO dto) {
        return ResponseEntity.status(201).body(zoneService.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing zone")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Zone updated successfully"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    public ResponseEntity<ZoneResponseDTO> updateZone(@PathVariable Long id,
                                                      @RequestBody @Valid ZoneRequestDTO dto) {
        return ResponseEntity.ok(zoneService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a zone by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Zone deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Zone not found")
    })
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ZoneNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(ZoneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
