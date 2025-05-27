package org.ironhack.frontendservice.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * DTO utilizado en el formulario para crear o actualizar un avistamiento.
 */
@Data
public class SightingRequestDTO {
    private Long zoneId;
    private Long speciesId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private String observedBy;
    private String method;
    private String notes;
}
