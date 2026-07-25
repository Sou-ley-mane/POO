package com.gie.fleet.service;

import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.enums.Periodicite;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Calcule les bornes de la période de versement courante d'un chauffeur, selon sa périodicité.
 * MENSUELLE s'aligne sur le mois calendaire. Les autres périodicités (hebdomadaire, bimensuelle,
 * trimensuelle) s'alignent sur des blocs de durée fixe ancrés à la date d'inscription du chauffeur,
 * pour que chaque chauffeur ait ses propres échéances cohérentes dans le temps.
 */
@Service
public class PeriodeCalculatorService {

    public record Periode(LocalDate debut, LocalDate fin) {
    }

    public Periode periodeCourante(Chauffeur chauffeur) {
        return periodePourDate(chauffeur, LocalDate.now());
    }

    public Periode periodePourDate(Chauffeur chauffeur, LocalDate reference) {
        Periodicite periodicite = chauffeur.getPeriodiciteVersement();

        if (periodicite == Periodicite.MENSUELLE) {
            LocalDate debut = reference.withDayOfMonth(1);
            return new Periode(debut, periodicite.calculerFinPeriode(debut));
        }

        LocalDate ancre = chauffeur.getDateInscription();
        long joursEcoules = ChronoUnit.DAYS.between(ancre, reference);
        long numeroBloc = Math.floorDiv(joursEcoules, periodicite.getNombreJours());
        LocalDate debut = ancre.plusDays(numeroBloc * periodicite.getNombreJours());
        return new Periode(debut, periodicite.calculerFinPeriode(debut));
    }
}
