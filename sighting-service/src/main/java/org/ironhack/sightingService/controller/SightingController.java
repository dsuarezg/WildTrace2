package org.ironhack.sightingService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.exception.SpeciesNotFoundException;
import org.ironhack.sightingService.exception.ZoneNotFoundException;
import org.ironhack.sightingService.service.SightingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sightings")
@Tag(name = "Sightings", description = "Operations related to species sightings")
public class SightingController {

    private final SightingService sightingService;

    public SightingController(SightingService sightingService) {
        this.sightingService = sightingService;
    }

    /**
     * Retrieves a list of all recorded sightings.
     *
     * @return a ResponseEntity with HTTP 200 and a list of SightingResponseDTO objects
     */
    @GetMapping("")
    @Operation(summary = "Retrieve all sightings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sightings returned")
    })
    public ResponseEntity<List<SightingResponseDTO>> getAllSightings() {
        return ResponseEntity.ok(sightingService.getAll());
    }

    /**
     * Retrieves a sighting record by its unique ID.
     *
     * @param id the unique identifier of the sighting
     * @return a ResponseEntity with the sighting data or HTTP 404 if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a sighting by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sighting found"),
            @ApiResponse(responseCode = "404", description = "Sighting not found")
    })
    public ResponseEntity<SightingResponseDTO> getSightingById(@PathVariable Long id) {
        return ResponseEntity.ok(sightingService.getById(id));
    }

    /**
     * Creates a new sighting record based on the provided data.
     *
     * @param dto the sighting data to create
     * @return a ResponseEntity with the created sighting and HTTP 201 status
     */
    @PostMapping("")
    @Operation(summary = "Create a new sighting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sighting created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Species or Zone not found")
    })
    public ResponseEntity<SightingResponseDTO> createSighting(@RequestBody @Valid SightingRequestDTO dto) {
        return ResponseEntity.status(201).body(sightingService.save(dto));
    }

    /**
     * Deletes a sighting by its unique identifier.
     *
     * @param id the unique identifier of the sighting to delete
     * @return HTTP 204 No Content if the deletion is successful
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sighting by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Sighting deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Sighting not found")
    })
    public ResponseEntity<Void> deleteSighting(@PathVariable Long id) {
        sightingService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ZoneNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(ZoneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
