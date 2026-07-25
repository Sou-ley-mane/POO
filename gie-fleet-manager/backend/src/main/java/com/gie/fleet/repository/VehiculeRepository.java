package com.gie.fleet.repository;

import com.gie.fleet.entity.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {

    List<Vehicule> findByGieId(Long gieId);

    Optional<Vehicule> findByIdAndGieId(Long id, Long gieId);

    boolean existsByGieIdAndImmatriculation(Long gieId, String immatriculation);
}
