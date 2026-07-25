package com.gie.fleet.dto.gie;

public record GieDto(
        Long id,
        String nom,
        String sigle,
        String logoUrl,
        String couleurPrimaire,
        String couleurSecondaire,
        String couleurAccent,
        String adresse,
        String telephone,
        String email,
        String statut
) {
}
