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

    /**
     * Handles GET requests to retrieve all species.
     *
     * @return a ResponseEntity with HTTP 200 status containing a list of all species as SpeciesResponseDTO objects
     */
    @GetMapping("")
    @Operation(summary = "Retrieve all species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of species returned")
    })
    public ResponseEntity<List<SpeciesResponseDTO>> getAllSpecies() {
        return ResponseEntity.ok(speciesService.getAll());
    }

    /****
     * Handles HTTP GET requests to retrieve a species by its unique ID.
     *
     * @param id the unique identifier of the species
     * @return a ResponseEntity containing the species data if found
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
     * Handles HTTP POST requests to create a new species from the provided validated data.
     *
     * @param dto the validated species data to create
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

    /****
     * Updates the details of an existing species identified by its ID.
     *
     * @param id the unique identifier of the species to update
     * @param dto the validated data for updating the species
     * @return a ResponseEntity containing the updated species information
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
     * Deletes a species identified by its unique ID.
     *
     * Removes the species with the given ID from the system. Returns HTTP 204 if the deletion is successful, or HTTP 404 if the species does not exist.
     *
     * @param id the unique identifier of the species to delete
     * @return HTTP 204 if deletion is successful; HTTP 404 if the species is not found
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

    /**
     * Handles cases where a species is not found by returning a 404 Not Found response with the error message.
     *
     * @param ex the exception indicating the species was not found
     * @return a ResponseEntity containing the exception message and HTTP 404 status
     */
    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
