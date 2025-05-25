package org.ironhack.species_service.exception;

public class SpeciesNotFoundException extends RuntimeException {
    /**
     * Constructs a new SpeciesNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the exception
     */
    public SpeciesNotFoundException(String message) {
        super(message);
    }

    /****
     * Constructs a SpeciesNotFoundException with a message indicating that a species with the specified ID was not found.
     *
     * @param id the ID of the species that could not be found
     */
    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
