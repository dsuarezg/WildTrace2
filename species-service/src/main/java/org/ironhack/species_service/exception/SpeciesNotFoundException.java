package org.ironhack.species_service.exception;

public class SpeciesNotFoundException extends RuntimeException {

    /**
     * Creates a SpeciesNotFoundException with a custom detail message.
     *
     * @param message the detail message describing the exception
     */
    public SpeciesNotFoundException(String message) {
        super(message);
    }


    /****
     * Creates an exception indicating that a species with the specified ID was not found.
     *
     * @param id the ID of the missing species
     */
    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
