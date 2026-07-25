package com.gie.fleet.dto.utilisateur;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateGestionnaireRequest(
        @NotBlank String nom,
        @NotBlank String prenom,
        @NotBlank String telephone,
        @Email String email,
        @NotBlank @Pattern(regexp = "\\d{4}", message = "Le PIN doit contenir exactement 4 chiffres") String pinInitial
) {
}
