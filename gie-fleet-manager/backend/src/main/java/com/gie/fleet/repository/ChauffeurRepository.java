package com.gie.fleet.repository;

import com.gie.fleet.entity.Chauffeur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChauffeurRepository extends JpaRepository<Chauffeur, Long> {

    List<Chauffeur> findByGieId(Long gieId);

    Optional<Chauffeur> findByIdAndGieId(Long id, Long gieId);

    Optional<Chauffeur> findByVehiculeAffecteId(Long vehiculeId);
}
