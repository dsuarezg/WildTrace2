package org.ironhack.sightingService.repository;

import org.ironhack.sightingService.model.Sighting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SightingRepository extends JpaRepository<Sighting, Long> {
}
