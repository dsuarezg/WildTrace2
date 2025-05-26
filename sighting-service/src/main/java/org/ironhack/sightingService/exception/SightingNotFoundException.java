package org.ironhack.sightingService.exception;

public class SightingNotFoundException extends RuntimeException {


    /**
     * Constructs a SightingNotFoundException with a custom error message.
     *
     * @param message the detail message explaining why the sighting was not found
     */

    public SightingNotFoundException(String message) {
        super(message);
    }


    /**
     * Constructs a SightingNotFoundException indicating that a sighting with the specified ID was not found.
     *
     * @param id the unique identifier of the missing sighting
     */

    public SightingNotFoundException(Long id) {
        super("Sighting not found with ID: " + id);
    }

}
