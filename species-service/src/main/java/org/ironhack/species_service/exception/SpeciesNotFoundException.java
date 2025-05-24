package org.ironhack.species_service.exception;

public class SpeciesNotFoundException extends RuntimeException {
    /****
     * Creates a new exception indicating that a species was not found, with a custom detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public SpeciesNotFoundException(String message) {
        super(message);
    }

    /****
     * Creates an exception indicating that a species with the specified ID was not found.
     *
     * @param id the unique identifier of the missing species
     */
    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
