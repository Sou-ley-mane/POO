package com.gie.fleet.dto.typefrais;

import com.gie.fleet.entity.enums.Periodicite;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateTypeFraisRequest(
        @NotBlank String libelle,
        @NotNull @PositiveOrZero BigDecimal montant,
        @NotNull Periodicite periodicite
) {
}
