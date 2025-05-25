package org.ironhack.sightingService.exception;

public class SightingNotFoundException extends RuntimeException {


    /**
     * Creates a SightingNotFoundException with a custom detail message.
     *
     * @param message the detail message describing why the sighting was not found
     */

    public SightingNotFoundException(String message) {
        super(message);
    }


    /**
     * Creates a SightingNotFoundException for a missing sighting with the specified ID.
     *
     * @param id the unique identifier of the sighting that was not found
     */

    public SightingNotFoundException(Long id) {
        super("Sighting not found with ID: " + id);
    }

}
