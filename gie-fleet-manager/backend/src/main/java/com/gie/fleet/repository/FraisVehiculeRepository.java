package com.gie.fleet.repository;

import com.gie.fleet.entity.FraisVehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FraisVehiculeRepository extends JpaRepository<FraisVehicule, Long> {

    List<FraisVehicule> findByVehiculeId(Long vehiculeId);

    List<FraisVehicule> findByVehiculeIdAndDateEcheanceBetween(Long vehiculeId, LocalDate debut, LocalDate fin);
}
