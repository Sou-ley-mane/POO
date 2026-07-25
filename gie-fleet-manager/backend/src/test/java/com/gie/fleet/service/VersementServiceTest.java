package com.gie.fleet.service;

import com.gie.fleet.dto.versement.SaisieVersementRequest;
import com.gie.fleet.dto.versement.VersementDto;
import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.FraisVehicule;
import com.gie.fleet.entity.Versement;
import com.gie.fleet.entity.enums.Periodicite;
import com.gie.fleet.entity.enums.StatutFrais;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.repository.ChauffeurRepository;
import com.gie.fleet.repository.FraisVehiculeRepository;
import com.gie.fleet.repository.TypeFraisRepository;
import com.gie.fleet.repository.VehiculeRepository;
import com.gie.fleet.repository.VersementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersementServiceTest {

    @Mock
    private VersementRepository versementRepository;
    @Mock
    private ChauffeurRepository chauffeurRepository;
    @Mock
    private VehiculeRepository vehiculeRepository;
    @Mock
    private FraisVehiculeRepository fraisVehiculeRepository;
    @Mock
    private TypeFraisRepository typeFraisRepository;
    @Mock
    private PeriodeCalculatorService periodeCalculatorService;

    @InjectMocks
    private VersementService versementService;

    private Chauffeur chauffeur;

    @BeforeEach
    void setUp() {
        chauffeur = new Chauffeur();
        chauffeur.setId(1L);
        chauffeur.setGieId(10L);
        chauffeur.setNom("Diallo");
        chauffeur.setPrenom("Amadou");
        chauffeur.setVehiculeAffecteId(5L);
        chauffeur.setPeriodiciteVersement(Periodicite.HEBDOMADAIRE);
        chauffeur.setMontantAttenduParPeriode(new BigDecimal("50000"));
    }

    @Test
    void reste_egale_montant_attendu_moins_somme_des_frais_sans_deduire_le_montant_verse() {
        when(chauffeurRepository.findByIdAndGieId(1L, 10L)).thenReturn(Optional.of(chauffeur));
        var periode = new PeriodeCalculatorService.Periode(LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 26));
        when(periodeCalculatorService.periodeCourante(chauffeur)).thenReturn(periode);
        when(versementRepository.findByChauffeurIdAndPeriodeDebutAndPeriodeFin(1L, periode.debut(), periode.fin()))
                .thenReturn(Optional.empty());

        FraisVehicule bon = fraisDe(new BigDecimal("2000"));
        FraisVehicule assurance = fraisDe(new BigDecimal("3000"));
        when(fraisVehiculeRepository.findById(100L)).thenReturn(Optional.of(bon));
        when(fraisVehiculeRepository.findById(101L)).thenReturn(Optional.of(assurance));

        when(versementRepository.save(any(Versement.class))).thenAnswer(inv -> inv.getArgument(0));

        SaisieVersementRequest request = new SaisieVersementRequest(1L, null, new BigDecimal("10000"), null, List.of(100L, 101L));

        VersementDto dto = versementService.saisir(10L, 99L, request);

        // reste = 50000 - (2000 + 3000) = 45000, le montant versé (10000) n'intervient pas
        assertThat(dto.reste()).isEqualByComparingTo("45000");
        assertThat(dto.montantVerse()).isEqualByComparingTo("10000");
        // montant versé (10000) < montant attendu (50000) => statut PARTIEL
        assertThat(dto.statut()).isEqualTo("PARTIEL");
    }

    @Test
    void statut_solde_quand_montant_verse_atteint_le_montant_attendu() {
        when(chauffeurRepository.findByIdAndGieId(1L, 10L)).thenReturn(Optional.of(chauffeur));
        var periode = new PeriodeCalculatorService.Periode(LocalDate.of(2026, 7, 20), LocalDate.of(2026, 7, 26));
        when(periodeCalculatorService.periodeCourante(chauffeur)).thenReturn(periode);
        when(versementRepository.findByChauffeurIdAndPeriodeDebutAndPeriodeFin(1L, periode.debut(), periode.fin()))
                .thenReturn(Optional.empty());
        when(versementRepository.save(any(Versement.class))).thenAnswer(inv -> inv.getArgument(0));

        SaisieVersementRequest request = new SaisieVersementRequest(1L, null, new BigDecimal("50000"), null, null);

        VersementDto dto = versementService.saisir(10L, 99L, request);

        assertThat(dto.statut()).isEqualTo("SOLDE");
        assertThat(dto.reste()).isEqualByComparingTo("50000");
    }

    @Test
    void refuse_la_saisie_si_le_chauffeur_n_est_affecte_a_aucun_vehicule() {
        chauffeur.setVehiculeAffecteId(null);
        when(chauffeurRepository.findByIdAndGieId(1L, 10L)).thenReturn(Optional.of(chauffeur));

        SaisieVersementRequest request = new SaisieVersementRequest(1L, null, BigDecimal.TEN, null, null);

        assertThrows(BusinessException.class, () -> versementService.saisir(10L, 99L, request));
    }

    private FraisVehicule fraisDe(BigDecimal montant) {
        FraisVehicule f = new FraisVehicule();
        f.setVehiculeId(5L);
        f.setMontant(montant);
        f.setStatut(StatutFrais.EN_ATTENTE);
        return f;
    }
}
