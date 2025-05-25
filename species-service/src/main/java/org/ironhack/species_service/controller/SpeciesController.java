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
     * Handles GET requests to retrieve a species by its unique ID.
     *
     * @param id the unique identifier of the species
     * @return a ResponseEntity containing the species data if found
     *
     * Returns HTTP 200 with the species data if the species exists; otherwise, returns HTTP 404 if not found.
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
     * Handles HTTP POST requests to create a new species.
     *
     * Accepts a validated species request payload and returns the created species data with HTTP status 201 (Created).
     * Responds with HTTP 400 if the input data is invalid or HTTP 409 if a duplicate species exists.
     *
     * @param dto the species data to create
     * @return a ResponseEntity containing the created species and HTTP status 201
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
     * Updates the details of an existing species by its ID.
     *
     * @param id the unique identifier of the species to update
     * @param dto the new species data to apply
     * @return a ResponseEntity containing the updated species information and HTTP 200 status
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



    /****
     * Deletes a species by its unique identifier.
     *
     * Removes the species with the given ID from the system. Returns HTTP 204 No Content if the deletion is successful.
     * If the species does not exist, a 404 Not Found response is returned via exception handling.
     *
     * @param id the unique identifier of the species to delete
     * @return HTTP 204 No Content if the species is deleted successfully
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
     * Handles SpeciesNotFoundException by returning an HTTP 404 Not Found response with the exception message as the response body.
     *
     * @param ex the exception indicating that the requested species was not found
     * @return a ResponseEntity containing the exception message and HTTP 404 status
     */

    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
