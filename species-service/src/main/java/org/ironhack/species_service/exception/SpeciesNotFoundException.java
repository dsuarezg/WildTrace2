package org.ironhack.species_service.exception;

public class SpeciesNotFoundException extends RuntimeException {
    public SpeciesNotFoundException(String message) {
        super(message);
    }

    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
