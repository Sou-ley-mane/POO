package com.gie.fleet.dto.versement;

import com.gie.fleet.entity.enums.ModePaiement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record SaisieVersementRequest(
        @NotNull Long chauffeurId,
        /** Début de la période concernée ; si null, la période en cours est utilisée. */
        LocalDate periodeDebut,
        @NotNull @PositiveOrZero BigDecimal montantVerse,
        ModePaiement modePaiement,
        /** Ids des FraisVehicule applicables à cette période (Bon, Assurance, Vignette...). */
        List<Long> fraisAppliquesIds
) {
}
