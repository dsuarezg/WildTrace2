package org.ironhack.sightingService.exception;

public class SightingNotFoundException extends RuntimeException {


    /**
     * Creates an exception indicating that a species was not found, with a custom detail message.
     *
     * @param message the detail message for the exception
     */

    public SightingNotFoundException(String message) {
        super(message);
    }


    /****
     * Creates an exception for a missing species with the given ID.
     *
     * @param id the ID of the species that was not found
     */

    public SightingNotFoundException(Long id) {
        super("Species not found with ID: " + id);
    }

}
