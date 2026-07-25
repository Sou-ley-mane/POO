package com.gie.fleet.dto.vehicule;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record CreateVehiculeRequest(
        @NotBlank String immatriculation,
        String marque,
        String modele,
        Integer annee,
        String couleur,
        LocalDate dateMiseEnService
) {
}
