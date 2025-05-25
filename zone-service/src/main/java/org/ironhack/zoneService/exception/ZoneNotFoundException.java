package org.ironhack.zoneService.exception;

public class ZoneNotFoundException extends RuntimeException {

    public ZoneNotFoundException(Long id) {
        super("Zone with ID " + id + " was not found");
    }

    public ZoneNotFoundException(String message) {
        super(message);
    }
}