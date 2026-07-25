package com.gie.fleet.dto.typefrais;

import java.math.BigDecimal;

public record TypeFraisDto(
        Long id,
        String libelle,
        BigDecimal montant,
        String periodicite,
        boolean actif
) {
}
