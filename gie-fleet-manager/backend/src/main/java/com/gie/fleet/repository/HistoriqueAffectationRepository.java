package com.gie.fleet.repository;

import com.gie.fleet.entity.HistoriqueAffectation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HistoriqueAffectationRepository extends JpaRepository<HistoriqueAffectation, Long> {

    List<HistoriqueAffectation> findByVehiculeIdOrderByDateDebutDesc(Long vehiculeId);

    List<HistoriqueAffectation> findByChauffeurIdOrderByDateDebutDesc(Long chauffeurId);

    Optional<HistoriqueAffectation> findByVehiculeIdAndDateFinIsNull(Long vehiculeId);

    Optional<HistoriqueAffectation> findByChauffeurIdAndDateFinIsNull(Long chauffeurId);
}
