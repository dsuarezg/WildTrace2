package org.ironhack.zoneService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Zone information returned by the API")
public class ZoneResponseDTO {

    @Schema(description = "Unique identifier of the zone", example = "1")
    private Long zoneId;

    @Schema(description = "Name of the zone", example = "Parque Nacional de la Sierra de Guadarrama")
    private String zoneName;

    @Schema(description = "Latitude of the zone center", example = "40.785")
    private Double latitude;

    @Schema(description = "Longitude of the zone center", example = "-3.739")
    private Double longitude;

    @Schema(description = "URL to a map preview of the zone", example = "https://maps.example.com/preview?lat=40.785&lon=-3.739")
    private String mapImageUrl;
}
