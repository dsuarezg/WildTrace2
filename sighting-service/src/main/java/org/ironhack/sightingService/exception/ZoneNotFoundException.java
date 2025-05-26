package org.ironhack.sightingService.exception;

public class ZoneNotFoundException extends RuntimeException {

    /**
     * Creates an exception indicating that a zone with the specified ID was not found.
     *
     * @param id the identifier of the missing zone
     */
    public ZoneNotFoundException(Long id) {
        super("Zone with ID " + id + " was not found");
    }

    /**
     * Creates a ZoneNotFoundException with a custom detail message.
     *
     * @param message the detail message describing the exception
     */
    public ZoneNotFoundException(String message) {
        super(message);
    }
}