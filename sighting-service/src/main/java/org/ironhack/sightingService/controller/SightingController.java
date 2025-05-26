package org.ironhack.sightingService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.ironhack.sightingService.dto.SightingRequestDTO;
import org.ironhack.sightingService.dto.SightingResponseDTO;
import org.ironhack.sightingService.exception.SightingNotFoundException;
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

    /**
     * Constructs a SightingController with the specified SightingService.
     *
     * @param sightingService the service used to manage sighting operations
     */
    public SightingController(SightingService sightingService) {
        this.sightingService = sightingService;
    }

    /**
     * Returns a list of all recorded species sightings.
     *
     * @return ResponseEntity with HTTP 200 status containing all SightingResponseDTO objects
     */
    @GetMapping("")
    @Operation(summary = "Retrieve all sightings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of sightings returned")
    })
    public ResponseEntity<List<SightingResponseDTO>> getAllSightings() {
        return ResponseEntity.ok(sightingService.getAll());
    }

    /****
     * Returns the sighting corresponding to the specified ID.
     *
     * @param id the unique identifier of the sighting
     * @return a ResponseEntity containing the sighting data if found
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

    /****
     * Creates a new species sighting using the provided request data.
     *
     * Accepts a validated sighting request and returns the created sighting with HTTP 201 status.
     * Returns HTTP 400 if the input data is invalid, or HTTP 404 if the referenced species or zone does not exist.
     *
     * @param dto the validated sighting request data
     * @return the created sighting with HTTP 201 status
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
     * Updates an existing sighting identified by its ID with new data.
     *
     * @param id the ID of the sighting to update
     * @param dto the new data for the sighting
     * @return the updated sighting in the response entity
     *
     * Returns HTTP 200 if the update is successful, HTTP 400 for invalid input, or HTTP 404 if the sighting is not found.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a sighting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sighting updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid update data"),
            @ApiResponse(responseCode = "404", description = "Sighting not found")
    })
    public ResponseEntity<SightingResponseDTO> updateSighting(@PathVariable Long id,
                                                              @RequestBody @Valid SightingRequestDTO dto) {
        return ResponseEntity.ok(sightingService.update(id, dto));
    }


    /****
     * Deletes a sighting by its unique identifier.
     *
     * Returns HTTP 204 No Content if the sighting is deleted successfully, or HTTP 404 if the sighting does not exist.
     *
     * @param id the unique identifier of the sighting to delete
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


    /**
     * Handles ZoneNotFoundException by returning a 404 Not Found response with the exception message.
     *
     * @param ex the ZoneNotFoundException thrown when a requested zone does not exist
     * @return a ResponseEntity containing the exception message and HTTP 404 status
     */
    @ExceptionHandler(ZoneNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(ZoneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles SpeciesNotFoundException by returning an HTTP 404 response with the exception message.
     *
     * @param ex the exception indicating the requested species was not found
     * @return a response entity containing the exception message and a 404 status code
     */
    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    /**
     * Handles SightingNotFoundException by returning an HTTP 404 response with the exception message.
     *
     * @param ex the exception thrown when a requested sighting is not found
     * @return a ResponseEntity containing the error message and HTTP 404 status
     */
    @ExceptionHandler(SightingNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SightingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
