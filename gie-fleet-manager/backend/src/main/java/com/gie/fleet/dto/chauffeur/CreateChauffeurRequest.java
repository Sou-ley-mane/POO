package com.gie.fleet.dto.chauffeur;

import com.gie.fleet.entity.enums.Periodicite;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateChauffeurRequest(
        @NotBlank String nom,
        @NotBlank String prenom,
        String telephone,
        String numeroPermis,
        String numeroCni,
        @NotNull Periodicite periodiciteVersement,
        @NotNull @PositiveOrZero BigDecimal montantAttenduParPeriode
) {
}
