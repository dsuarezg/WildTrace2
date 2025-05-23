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

    // GET ALL
    @GetMapping("")
    @Operation(summary = "Retrieve all species")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of species returned")
    })
    public ResponseEntity<List<SpeciesResponseDTO>> getAllSpecies() {
        return ResponseEntity.ok(speciesService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a species by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Species found"),
            @ApiResponse(responseCode = "404", description = "Species not found")
    })
    public ResponseEntity<SpeciesResponseDTO> getSpeciesById(@PathVariable Long id) {
        return ResponseEntity.ok(speciesService.getById(id));
    }

    // CREATE
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

    // UPDATE
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

    // DELETE
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

    @ExceptionHandler(SpeciesNotFoundException.class)
    public ResponseEntity<String> handleSpeciesNotFound(SpeciesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
