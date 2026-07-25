package com.gie.fleet.entity.enums;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public enum Periodicite {
    HEBDOMADAIRE(7),
    BIMENSUELLE(14),
    TRIMENSUELLE(21),
    MENSUELLE(30);

    private final int nombreJours;

    Periodicite(int nombreJours) {
        this.nombreJours = nombreJours;
    }

    /**
     * MENSUELLE s'aligne sur le mois calendaire plutôt que sur un bloc fixe de 30 jours,
     * pour rester cohérent avec le tableau Excel existant (semaine 1, 2, 3, mois).
     */
    public LocalDate calculerFinPeriode(LocalDate debut) {
        if (this == MENSUELLE) {
            return debut.plusMonths(1).minusDays(1);
        }
        return debut.plusDays(nombreJours - 1L);
    }

    public long dureeEnJours(LocalDate debut) {
        return ChronoUnit.DAYS.between(debut, calculerFinPeriode(debut)) + 1;
    }

    public int getNombreJours() {
        return nombreJours;
    }
}
