package org.ironhack.speciesService.exception;

public class SpeciesNotFoundException extends RuntimeException {


    /**
     * Creates a SpeciesNotFoundException with a custom error message.
     *
     * @param message the error message describing the exception
     */

    public SpeciesNotFoundException(String message) {
        super(message);
    }


    /**
     * Constructs a SpeciesNotFoundException with a message specifying the missing species ID.
     *
     * @param id the unique identifier of the species that was not found
     */

    public SpeciesNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
