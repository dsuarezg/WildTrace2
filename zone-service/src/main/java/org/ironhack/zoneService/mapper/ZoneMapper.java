package org.ironhack.zoneService.mapper;

import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.dto.ZoneResponseDTO;
import org.ironhack.zoneService.model.Zone;

public class ZoneMapper {

    public static ZoneResponseDTO toResponseDTO(Zone zone) {
        ZoneResponseDTO dto = new ZoneResponseDTO();
        dto.setZoneId(zone.getZoneId());
        dto.setZoneName(zone.getZoneName());
        dto.setLatitude(zone.getLatitude());
        dto.setLongitude(zone.getLongitude());

        //TODO external api to fetch image url
        //dto.setMapImageUrl(generateMapImageUrl(zone.getLatitude(), zone.getLongitude()));
        return dto;
    }

    public static Zone toEntity(ZoneRequestDTO dto) {
        Zone zone = new Zone();
        zone.setZoneName(dto.getZoneName());
        zone.setLatitude(dto.getLatitude());
        zone.setLongitude(dto.getLongitude());
        return zone;
    }

/*    private static String generateMapImageUrl(Double lat, Double lon) {
        return String.format("https://maps.example.com/preview?lat=%.5f&lon=%.5f", lat, lon);
    }*/
}