package org.ironhack.sightingService.exception;

public class SightingNotFoundException extends RuntimeException {


    /**
     * Constructs a SightingNotFoundException with a custom detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */

    public SightingNotFoundException(String message) {
        super(message);
    }


    /**
     * Constructs a SightingNotFoundException with a message indicating that a sighting with the specified ID was not found.
     *
     * @param id the ID of the sighting that could not be located
     */

    public SightingNotFoundException(Long id) {
        super("Sighting not found with ID: " + id);
    }

}
