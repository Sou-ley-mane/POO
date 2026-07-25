package com.gie.fleet.service;

import com.gie.fleet.dto.chauffeur.ChauffeurDto;
import com.gie.fleet.dto.chauffeur.CreateChauffeurRequest;
import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.HistoriqueAffectation;
import com.gie.fleet.entity.Vehicule;
import com.gie.fleet.entity.enums.StatutChauffeur;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.ChauffeurRepository;
import com.gie.fleet.repository.HistoriqueAffectationRepository;
import com.gie.fleet.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChauffeurService {

    private final ChauffeurRepository chauffeurRepository;
    private final VehiculeRepository vehiculeRepository;
    private final HistoriqueAffectationRepository historiqueAffectationRepository;

    @Transactional
    public ChauffeurDto creer(Long gieId, CreateChauffeurRequest request) {
        Chauffeur chauffeur = new Chauffeur();
        chauffeur.setGieId(gieId);
        chauffeur.setNom(request.nom());
        chauffeur.setPrenom(request.prenom());
        chauffeur.setTelephone(request.telephone());
        chauffeur.setNumeroPermis(request.numeroPermis());
        chauffeur.setNumeroCni(request.numeroCni());
        chauffeur.setPeriodiciteVersement(request.periodiciteVersement());
        chauffeur.setMontantAttenduParPeriode(request.montantAttenduParPeriode());
        return toDto(chauffeurRepository.save(chauffeur));
    }

    public List<ChauffeurDto> lister(Long gieId) {
        return chauffeurRepository.findByGieId(gieId).stream().map(this::toDto).toList();
    }

    public ChauffeurDto obtenir(Long gieId, Long id) {
        return toDto(getOrThrow(gieId, id));
    }

    @Transactional
    public ChauffeurDto changerStatut(Long gieId, Long id, StatutChauffeur statut) {
        Chauffeur chauffeur = getOrThrow(gieId, id);
        chauffeur.setStatut(statut);
        return toDto(chauffeurRepository.save(chauffeur));
    }

    /**
     * Affecte un chauffeur à un véhicule. Un véhicule n'a qu'un seul chauffeur actif à la fois,
     * et un chauffeur qu'un seul véhicule actif à la fois : toute nouvelle affectation clôture
     * automatiquement les affectations précédentes (des deux côtés) dans l'historique.
     */
    @Transactional
    public ChauffeurDto affecterVehicule(Long gieId, Long chauffeurId, Long vehiculeId) {
        Chauffeur chauffeur = getOrThrow(gieId, chauffeurId);
        Vehicule vehicule = vehiculeRepository.findByIdAndGieId(vehiculeId, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable : " + vehiculeId));

        LocalDateTime maintenant = LocalDateTime.now();

        // Clôture l'ancienne affectation du chauffeur (s'il conduisait déjà un autre véhicule)
        if (chauffeur.getVehiculeAffecteId() != null && !chauffeur.getVehiculeAffecteId().equals(vehiculeId)) {
            cloturerAffectationChauffeur(chauffeur.getId(), maintenant);
            vehiculeRepository.findById(chauffeur.getVehiculeAffecteId()).ifPresent(ancienVehicule -> {
                ancienVehicule.setChauffeurActuelId(null);
                vehiculeRepository.save(ancienVehicule);
            });
        }

        // Clôture l'ancienne affectation du véhicule (s'il avait déjà un autre chauffeur actif)
        if (vehicule.getChauffeurActuelId() != null && !vehicule.getChauffeurActuelId().equals(chauffeurId)) {
            cloturerAffectationVehicule(vehicule.getId(), maintenant);
            chauffeurRepository.findById(vehicule.getChauffeurActuelId()).ifPresent(ancienChauffeur -> {
                ancienChauffeur.setVehiculeAffecteId(null);
                chauffeurRepository.save(ancienChauffeur);
            });
        }

        HistoriqueAffectation historique = new HistoriqueAffectation();
        historique.setGieId(gieId);
        historique.setVehiculeId(vehiculeId);
        historique.setChauffeurId(chauffeurId);
        historique.setDateDebut(maintenant);
        historiqueAffectationRepository.save(historique);

        chauffeur.setVehiculeAffecteId(vehiculeId);
        vehicule.setChauffeurActuelId(chauffeurId);
        vehiculeRepository.save(vehicule);

        return toDto(chauffeurRepository.save(chauffeur));
    }

    @Transactional
    public ChauffeurDto retirerAffectation(Long gieId, Long chauffeurId) {
        Chauffeur chauffeur = getOrThrow(gieId, chauffeurId);
        if (chauffeur.getVehiculeAffecteId() == null) {
            throw new BusinessException("Ce chauffeur n'est affecté à aucun véhicule");
        }
        LocalDateTime maintenant = LocalDateTime.now();
        cloturerAffectationChauffeur(chauffeurId, maintenant);
        vehiculeRepository.findById(chauffeur.getVehiculeAffecteId()).ifPresent(vehicule -> {
            vehicule.setChauffeurActuelId(null);
            vehiculeRepository.save(vehicule);
        });
        chauffeur.setVehiculeAffecteId(null);
        return toDto(chauffeurRepository.save(chauffeur));
    }

    private void cloturerAffectationChauffeur(Long chauffeurId, LocalDateTime dateFin) {
        Optional<HistoriqueAffectation> actif = historiqueAffectationRepository.findByChauffeurIdAndDateFinIsNull(chauffeurId);
        actif.ifPresent(h -> {
            h.setDateFin(dateFin);
            historiqueAffectationRepository.save(h);
        });
    }

    private void cloturerAffectationVehicule(Long vehiculeId, LocalDateTime dateFin) {
        Optional<HistoriqueAffectation> actif = historiqueAffectationRepository.findByVehiculeIdAndDateFinIsNull(vehiculeId);
        actif.ifPresent(h -> {
            h.setDateFin(dateFin);
            historiqueAffectationRepository.save(h);
        });
    }

    private Chauffeur getOrThrow(Long gieId, Long id) {
        return chauffeurRepository.findByIdAndGieId(id, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Chauffeur introuvable : " + id));
    }

    private ChauffeurDto toDto(Chauffeur chauffeur) {
        String immat = null;
        if (chauffeur.getVehiculeAffecteId() != null) {
            immat = vehiculeRepository.findById(chauffeur.getVehiculeAffecteId())
                    .map(Vehicule::getImmatriculation).orElse(null);
        }
        return new ChauffeurDto(
                chauffeur.getId(), chauffeur.getNom(), chauffeur.getPrenom(), chauffeur.getTelephone(),
                chauffeur.getNumeroPermis(), chauffeur.getNumeroCni(), chauffeur.getDateInscription(),
                chauffeur.getStatut().name(), chauffeur.getVehiculeAffecteId(), immat,
                chauffeur.getPeriodiciteVersement().name(), chauffeur.getMontantAttenduParPeriode());
    }
}
