package com.gie.fleet.dto.utilisateur;

public record UtilisateurDto(
        Long id,
        String nom,
        String prenom,
        String telephone,
        String email,
        String role,
        String statut,
        boolean doitChangerPin
) {
}
