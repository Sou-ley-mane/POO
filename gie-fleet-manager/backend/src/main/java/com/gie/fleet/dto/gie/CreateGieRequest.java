package com.gie.fleet.dto.gie;

import jakarta.validation.constraints.NotBlank;

public record CreateGieRequest(
        @NotBlank String nom,
        @NotBlank String sigle,
        String adresse,
        String telephone,
        String email,
        @NotBlank String telephoneAdmin,
        @NotBlank String pinInitialAdmin,
        @NotBlank String nomAdmin,
        @NotBlank String prenomAdmin
) {
}
