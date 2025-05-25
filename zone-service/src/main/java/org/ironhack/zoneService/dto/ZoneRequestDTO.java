package org.ironhack.zoneService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Payload to create or update a zone")
public class ZoneRequestDTO {

    @NotBlank
    @Schema(description = "Name of the zone", example = "Reserva Natural del Valle del Lozoya")
    private String zoneName;

    @NotNull
    @Schema(description = "Latitude of the zone center", example = "40.980")
    private Double latitude;

    @NotNull
    @Schema(description = "Longitude of the zone center", example = "-3.670")
    private Double longitude;
}
