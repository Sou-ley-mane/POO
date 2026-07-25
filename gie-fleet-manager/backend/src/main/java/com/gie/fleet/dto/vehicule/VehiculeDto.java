package com.gie.fleet.dto.vehicule;

import java.time.LocalDate;

public record VehiculeDto(
        Long id,
        String immatriculation,
        String marque,
        String modele,
        Integer annee,
        String couleur,
        String statut,
        Long chauffeurActuelId,
        String chauffeurActuelNomComplet,
        LocalDate dateMiseEnService
) {
}
