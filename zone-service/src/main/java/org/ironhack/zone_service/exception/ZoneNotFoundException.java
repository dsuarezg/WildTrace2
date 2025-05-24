package org.ironhack.zone_service.exception;

public class ZoneNotFoundException extends RuntimeException {

    public ZoneNotFoundException(Long id) {
        super("Zone with ID " + id + " was not found");
    }

    public ZoneNotFoundException(String message) {
        super(message);
    }
}