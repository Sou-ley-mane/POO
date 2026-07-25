package com.gie.fleet.dto.chauffeur;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ChauffeurDto(
        Long id,
        String nom,
        String prenom,
        String telephone,
        String numeroPermis,
        String numeroCni,
        LocalDate dateInscription,
        String statut,
        Long vehiculeAffecteId,
        String vehiculeAffecteImmatriculation,
        String periodiciteVersement,
        BigDecimal montantAttenduParPeriode
) {
}
