package com.gie.fleet.service;

import com.gie.fleet.dto.dashboard.DashboardStatsDto;
import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.Versement;
import com.gie.fleet.entity.enums.StatutChauffeur;
import com.gie.fleet.entity.enums.StatutVehicule;
import com.gie.fleet.repository.ChauffeurRepository;
import com.gie.fleet.repository.VehiculeRepository;
import com.gie.fleet.repository.VersementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final VersementRepository versementRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ChauffeurRepository chauffeurRepository;

    public DashboardStatsDto calculer(Long gieId) {
        List<Versement> versements = versementRepository.findByGieId(gieId);
        LocalDate aujourdHui = LocalDate.now();
        LocalDate debutSemaine = aujourdHui.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate debutMois = aujourdHui.withDayOfMonth(1);

        BigDecimal collecteJour = sommeVerseeDepuis(versements, aujourdHui);
        BigDecimal collecteSemaine = sommeVerseeDepuis(versements, debutSemaine);
        BigDecimal collecteMois = sommeVerseeDepuis(versements, debutMois);

        // Impayés : ce qui reste effectivement dû par les chauffeurs (attendu - versé),
        // à ne pas confondre avec le champ "reste" du versement (bookkeeping des frais véhicule).
        BigDecimal totalImpayes = versements.stream()
                .map(v -> v.getMontantAttendu().subtract(v.getMontantVerse()))
                .filter(montant -> montant.compareTo(BigDecimal.ZERO) > 0)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long vehiculesActifs = vehiculeRepository.findByGieId(gieId).stream()
                .filter(v -> v.getStatut() == StatutVehicule.EN_CIRCULATION)
                .count();
        long chauffeursActifs = chauffeurRepository.findByGieId(gieId).stream()
                .filter(c -> c.getStatut() == StatutChauffeur.ACTIF)
                .count();

        Map<Long, BigDecimal> impayesParChauffeur = versements.stream()
                .filter(v -> v.getMontantAttendu().subtract(v.getMontantVerse()).compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(Versement::getChauffeurId,
                        Collectors.reducing(BigDecimal.ZERO,
                                v -> v.getMontantAttendu().subtract(v.getMontantVerse()), BigDecimal::add)));

        Map<Long, Long> nbPeriodesEnRetardParChauffeur = versements.stream()
                .filter(v -> v.getMontantAttendu().subtract(v.getMontantVerse()).compareTo(BigDecimal.ZERO) > 0)
                .collect(Collectors.groupingBy(Versement::getChauffeurId, Collectors.counting()));

        List<DashboardStatsDto.TopRetardDto> topRetards = impayesParChauffeur.entrySet().stream()
                .sorted(Map.Entry.<Long, BigDecimal>comparingByValue().reversed())
                .limit(10)
                .map(entry -> {
                    Chauffeur c = chauffeurRepository.findById(entry.getKey()).orElse(null);
                    String nomComplet = c != null ? c.getPrenom() + " " + c.getNom() : "Inconnu";
                    int nbRetards = nbPeriodesEnRetardParChauffeur.getOrDefault(entry.getKey(), 0L).intValue();
                    return new DashboardStatsDto.TopRetardDto(entry.getKey(), nomComplet, entry.getValue(), nbRetards);
                })
                .toList();

        return new DashboardStatsDto(collecteJour, collecteSemaine, collecteMois, totalImpayes,
                vehiculesActifs, chauffeursActifs, topRetards);
    }

    private BigDecimal sommeVerseeDepuis(List<Versement> versements, LocalDate depuis) {
        LocalDateTime seuil = depuis.atStartOfDay();
        return versements.stream()
                .filter(v -> v.getDatePaiement() != null && !v.getDatePaiement().isBefore(seuil))
                .map(Versement::getMontantVerse)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
