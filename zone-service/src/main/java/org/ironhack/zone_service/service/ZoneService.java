package org.ironhack.zone_service.service;

import org.ironhack.zone_service.dto.ZoneRequestDTO;
import org.ironhack.zone_service.dto.ZoneResponseDTO;
import org.ironhack.zone_service.exception.ZoneNotFoundException;
import org.ironhack.zone_service.mapper.ZoneMapper;
import org.ironhack.zone_service.model.Zone;
import org.ironhack.zone_service.repository.ZoneRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<ZoneResponseDTO> getAll() {
        return zoneRepository.findAll().stream()
                .map(ZoneMapper::toResponseDTO)
                .toList();
    }

    public ZoneResponseDTO getById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException(id));
        return ZoneMapper.toResponseDTO(zone);
    }

    public ZoneResponseDTO save(ZoneRequestDTO dto) {
        Zone zone = ZoneMapper.toEntity(dto);
        return ZoneMapper.toResponseDTO(zoneRepository.save(zone));
    }

    public ZoneResponseDTO update(Long id, ZoneRequestDTO dto) {
        Zone found = zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException(id));

        found.setZoneName(dto.getZoneName());
        found.setLatitude(dto.getLatitude());
        found.setLongitude(dto.getLongitude());

        return ZoneMapper.toResponseDTO(zoneRepository.save(found));
    }

    public void delete(Long id) {
        Zone found = zoneRepository.findById(id)
                .orElseThrow(() -> new ZoneNotFoundException(id));
        zoneRepository.delete(found);
    }
}