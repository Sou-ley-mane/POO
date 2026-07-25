package com.gie.fleet.repository;

import com.gie.fleet.entity.Versement;
import com.gie.fleet.entity.enums.StatutVersement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VersementRepository extends JpaRepository<Versement, Long> {

    List<Versement> findByGieId(Long gieId);

    List<Versement> findByGieIdAndPeriodeDebutLessThanEqualAndPeriodeFinGreaterThanEqual(
            Long gieId, LocalDate finRecherche, LocalDate debutRecherche);

    List<Versement> findByChauffeurIdOrderByPeriodeDebutDesc(Long chauffeurId);

    List<Versement> findByVehiculeIdOrderByPeriodeDebutDesc(Long vehiculeId);

    Optional<Versement> findByChauffeurIdAndPeriodeDebutAndPeriodeFin(Long chauffeurId, LocalDate debut, LocalDate fin);

    List<Versement> findByGieIdAndStatut(Long gieId, StatutVersement statut);

    Optional<Versement> findByIdAndGieId(Long id, Long gieId);
}
