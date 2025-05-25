package org.ironhack.speciesService.exception;

public class SpeciesNotFoundException extends RuntimeException {


    /**
     * Constructs a SpeciesNotFoundException with a custom detail message.
     *
     * @param message the detail message explaining the exception
     */

    public SpeciesNotFoundException(String message) {
        super(message);
    }


    /****
         * Constructs a SpeciesNotFoundException with a message indicating that a species with the specified ID was not found.
         *
         * @param id the unique identifier of the missing species
         */

    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
