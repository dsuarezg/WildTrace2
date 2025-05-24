package org.ironhack.species_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.ironhack.species_service.dto.SpeciesRequestDTO;
import org.ironhack.species_service.dto.SpeciesResponseDTO;
import org.ironhack.species_service.exception.SpeciesNotFoundException;
import org.ironhack.species_service.service.SpeciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/species")
@Tag(name = "Species", description = "Operations related to wildlife species")
public class SpeciesController {

    @Autowired
    private SpeciesService speciesService;

    /****
     * Retrieves a list of all species.
     *
     * @return a ResponseEntity containing a list of SpeciesResponseDTO objects with HTTP status 200
     */
    @GetMapping("")
    @Operation(summary = "Retrieve all species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of species returned")
    })
    public ResponseEntity<List<SpeciesResponseDTO>> getAllSpecies() {
        return ResponseEntity.ok(speciesService.getAll());
    }

    /**
     * Retrieves a species by its unique identifier.
     *
     * @param id the ID of the species to retrieve
     * @return a ResponseEntity containing the species data if found, or a 404 status if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a species by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species found"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<SpeciesResponseDTO> getSpeciesById(@PathVariable Long id) {
        return ResponseEntity.ok(speciesService.getById(id));
    }

    /**
     * Creates a new species using the provided data.
     *
     * @param dto the species data to create
     * @return a ResponseEntity containing the created species and HTTP 201 status
     */
    @PostMapping("")
    @Operation(summary = "Create a new species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Species created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - duplicate entry")
    })
    public ResponseEntity<SpeciesResponseDTO> createSpecies(@RequestBody @Valid SpeciesRequestDTO dto) {
        return ResponseEntity.status(201).body(speciesService.saveSpecies(dto));
    }

    /**
     * Updates an existing species with the provided data.
     *
     * @param id the ID of the species to update
     * @param dto the updated species data
     * @return the updated species information
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<SpeciesResponseDTO> updateSpecies(@PathVariable Long id, @RequestBody @Valid SpeciesRequestDTO dto) {
        return ResponseEntity.ok(speciesService.updateSpecies(id, dto));
    }

    /**
     * Deletes a species by its unique identifier.
     *
     * Removes the species with the specified ID from the system. Returns HTTP 204 if deletion is successful, or HTTP 404 if the species does not exist.
     *
     * @param id the unique identifier of the species to delete
     * @return a response entity with HTTP 204 on success or HTTP 404 if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a species by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Species deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<Void> deleteSpecies(@PathVariable Long id) {
        speciesService.deleteSpecies(id);
        return ResponseEntity.noContent().build();
    }

    /****
     * Handles `SpeciesNotFoundException` by returning a 404 Not Found response with the exception message.
     *
     * @param ex the exception thrown when a species is not found
     * @return a response entity with HTTP 404 status and the exception message as the body
     */
    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
