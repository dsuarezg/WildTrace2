package org.ironhack.sightingService.exception;

public class ZoneNotFoundException extends RuntimeException {

    /**
     * Constructs a ZoneNotFoundException with a message indicating that a zone with the specified ID was not found.
     *
     * @param id the ID of the zone that could not be found
     */
    public ZoneNotFoundException(Long id) {
        super("Zone with ID " + id + " was not found");
    }

    /**
     * Constructs a new ZoneNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public ZoneNotFoundException(String message) {
        super(message);
    }
}