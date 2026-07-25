package com.gie.fleet.dto.versement;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Reproduit la vue "grille" du tableau Excel existant :
 * Véhicule | Chauffeur | Bon | Assurance | ... | Montant versé | Reste.
 * Les colonnes de frais sont dynamiques (paramétrées par GIE), d'où la Map.
 */
public record VersementGrilleLigneDto(
        Long versementId,
        String vehiculeImmatriculation,
        String chauffeurNomComplet,
        Map<String, BigDecimal> montantParTypeFrais,
        BigDecimal montantAttendu,
        BigDecimal montantVerse,
        BigDecimal reste,
        String statut
) {
}
