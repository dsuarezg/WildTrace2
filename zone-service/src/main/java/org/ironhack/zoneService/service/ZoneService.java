package org.ironhack.zoneService.service;

import org.ironhack.zoneService.dto.ZoneRequestDTO;
import org.ironhack.zoneService.dto.ZoneResponseDTO;
import org.ironhack.zoneService.exception.ZoneNotFoundException;
import org.ironhack.zoneService.mapper.ZoneMapper;
import org.ironhack.zoneService.model.Zone;
import org.ironhack.zoneService.repository.ZoneRepository;
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