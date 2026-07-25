package com.gie.fleet.dto.versement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record VersementDto(
        Long id,
        Long chauffeurId,
        String chauffeurNomComplet,
        Long vehiculeId,
        String vehiculeImmatriculation,
        LocalDate periodeDebut,
        LocalDate periodeFin,
        BigDecimal montantAttendu,
        BigDecimal montantVerse,
        List<LigneFraisDto> listeFraisPeriode,
        BigDecimal reste,
        LocalDateTime datePaiement,
        String modePaiement,
        String statut
) {
    public record LigneFraisDto(Long fraisVehiculeId, String libelleTypeFrais, BigDecimal montant) {
    }
}
