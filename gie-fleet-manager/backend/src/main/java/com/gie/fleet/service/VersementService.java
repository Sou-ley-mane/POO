package com.gie.fleet.service;

import com.gie.fleet.dto.versement.SaisieVersementRequest;
import com.gie.fleet.dto.versement.VersementDto;
import com.gie.fleet.dto.versement.VersementGrilleLigneDto;
import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.FraisVehicule;
import com.gie.fleet.entity.TypeFrais;
import com.gie.fleet.entity.Vehicule;
import com.gie.fleet.entity.Versement;
import com.gie.fleet.entity.enums.StatutVersement;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.ChauffeurRepository;
import com.gie.fleet.repository.FraisVehiculeRepository;
import com.gie.fleet.repository.TypeFraisRepository;
import com.gie.fleet.repository.VehiculeRepository;
import com.gie.fleet.repository.VersementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class VersementService {

    private final VersementRepository versementRepository;
    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final FraisVehiculeRepository fraisVehiculeRepository;
    private final TypeFraisRepository typeFraisRepository;
    private final PeriodeCalculatorService periodeCalculatorService;

    /**
     * Saisit ou complète un versement pour la période concernée. Le montant versé est cumulé
     * sur la période (plusieurs saisies possibles) et reste purement informatif : il n'entre
     * jamais dans le calcul du reste. Le reste = montant attendu - somme des frais de la période.
     */
    @Transactional
    public VersementDto saisir(Long gieId, Long saisiParUserId, SaisieVersementRequest request) {
        Chauffeur chauffeur = chauffeurRepository.findByIdAndGieId(request.chauffeurId(), gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable : " + request.chauffeurId()));

        if (chauffeur.getVehiculeAffecteId() == null) {
            throw new BusinessException("Ce chauffeur n'est affecté à aucun véhicule");
        }

        PeriodeCalculatorService.Periode periode = request.periodeDebut() != null
                ? periodeCalculatorService.periodePourDate(chauffeur, request.periodeDebut())
                : periodeCalculatorService.periodeCourante(chauffeur);

        Versement versement = versementRepository
                .findByChauffeurIdAndPeriodeDebutAndPeriodeFin(chauffeur.getId(), periode.debut(), periode.fin())
                .orElseGet(() -> {
                    Versement v = new Versement();
                    v.setGieId(gieId);
                    v.setChauffeurId(chauffeur.getId());
                    v.setVehiculeId(chauffeur.getVehiculeAffecteId());
                    v.setPeriodeDebut(periode.debut());
                    v.setPeriodeFin(periode.fin());
                    // Montant fixe pour cette période précise, indépendant des autres périodes.
                    v.setMontantAttendu(chauffeur.getMontantAttenduParPeriode());
                    return v;
                });

        if (request.fraisAppliquesIds() != null && !request.fraisAppliquesIds().isEmpty()) {
            Set<FraisVehicule> frais = new LinkedHashSet<>();
            for (Long fraisId : request.fraisAppliquesIds()) {
                FraisVehicule f = fraisVehiculeRepository.findById(fraisId)
                        .orElseThrow(() -> new ResourceNotFoundException("Frais introuvable : " + fraisId));
                if (!f.getVehiculeId().equals(chauffeur.getVehiculeAffecteId())) {
                    throw new BusinessException("Ce frais ne concerne pas le véhicule affecté à ce chauffeur");
                }
                frais.add(f);
            }
            versement.setListeFraisPeriode(frais);
        }

        versement.setMontantVerse(versement.getMontantVerse().add(request.montantVerse()));
        if (request.modePaiement() != null) {
            versement.setModePaiement(request.modePaiement());
        }
        versement.setDatePaiement(LocalDateTime.now());

        recalculer(versement);

        return toDto(versementRepository.save(versement));
    }

    /** reste = montant attendu - somme des frais de la période (le montant versé n'est pas déduit). */
    private void recalculer(Versement versement) {
        BigDecimal sommeFrais = versement.getListeFraisPeriode().stream()
                .map(FraisVehicule::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        versement.setReste(versement.getMontantAttendu().subtract(sommeFrais));

        // Le statut reflète le paiement réel (montant versé vs montant attendu), indépendamment
        // du "reste" qui est une donnée de trésorerie liée aux frais du véhicule.
        if (versement.getMontantVerse().compareTo(versement.getMontantAttendu()) >= 0) {
            versement.setStatut(StatutVersement.SOLDE);
        } else if (versement.getMontantVerse().compareTo(BigDecimal.ZERO) > 0) {
            versement.setStatut(StatutVersement.PARTIEL);
        } else {
            versement.setStatut(StatutVersement.IMPAYE);
        }
    }

    public List<VersementDto> historiqueParChauffeur(Long chauffeurId) {
        return versementRepository.findByChauffeurIdOrderByPeriodeDebutDesc(chauffeurId).stream().map(this::toDto).toList();
    }

    public List<VersementDto> historiqueParVehicule(Long vehiculeId) {
        return versementRepository.findByVehiculeIdOrderByPeriodeDebutDesc(vehiculeId).stream().map(this::toDto).toList();
    }

    /** Vue "grille" reproduisant le tableau Excel : Véhicule | Chauffeur | Bon | Assurance | ... | Montant versé | Reste. */
    public List<VersementGrilleLigneDto> grille(Long gieId) {
        Map<Long, String> libelleTypeFraisParId = typeFraisRepository.findByGieId(gieId).stream()
                .collect(java.util.stream.Collectors.toMap(TypeFrais::getId, TypeFrais::getLibelle));

        List<Versement> versements = versementRepository.findByGieId(gieId);
        return versements.stream().map(v -> {
            Chauffeur chauffeur = chauffeurRepository.findById(v.getChauffeurId()).orElse(null);
            Vehicule vehicule = vehiculeRepository.findById(v.getVehiculeId()).orElse(null);

            Map<String, BigDecimal> montantParTypeFrais = new HashMap<>();
            for (FraisVehicule f : v.getListeFraisPeriode()) {
                String libelle = libelleTypeFraisParId.getOrDefault(f.getTypeFraisId(), "Autre");
                montantParTypeFrais.merge(libelle, f.getMontant(), BigDecimal::add);
            }

            return new VersementGrilleLigneDto(
                    v.getId(),
                    vehicule != null ? vehicule.getImmatriculation() : null,
                    chauffeur != null ? chauffeur.getPrenom() + " " + chauffeur.getNom() : null,
                    montantParTypeFrais,
                    v.getMontantAttendu(),
                    v.getMontantVerse(),
                    v.getReste(),
                    v.getStatut().name());
        }).toList();
    }

    private VersementDto toDto(Versement versement) {
        Chauffeur chauffeur = chauffeurRepository.findById(versement.getChauffeurId()).orElse(null);
        Vehicule vehicule = vehiculeRepository.findById(versement.getVehiculeId()).orElse(null);

        List<VersementDto.LigneFraisDto> lignes = versement.getListeFraisPeriode().stream()
                .map(f -> new VersementDto.LigneFraisDto(
                        f.getId(),
                        typeFraisRepository.findById(f.getTypeFraisId()).map(TypeFrais::getLibelle).orElse("Autre"),
                        f.getMontant()))
                .toList();

        return new VersementDto(
                versement.getId(),
                versement.getChauffeurId(),
                chauffeur != null ? chauffeur.getPrenom() + " " + chauffeur.getNom() : null,
                versement.getVehiculeId(),
                vehicule != null ? vehicule.getImmatriculation() : null,
                versement.getPeriodeDebut(),
                versement.getPeriodeFin(),
                versement.getMontantAttendu(),
                versement.getMontantVerse(),
                lignes,
                versement.getReste(),
                versement.getDatePaiement(),
                versement.getModePaiement() != null ? versement.getModePaiement().name() : null,
                versement.getStatut().name());
    }
}
