package org.ironhack.speciesService.exception;

public class SpeciesNotFoundException extends RuntimeException {


    /****
     * Creates a SpeciesNotFoundException with a custom error message.
     *
     * @param message the error message describing the exception
     */

    public SpeciesNotFoundException(String message) {
        super(message);
    }


    /**
     * Creates a SpeciesNotFoundException with an error message indicating that the species with the specified ID was not found.
     *
     * @param id the unique identifier of the species that was not found
     */

    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
