package com.gie.fleet.service;

import com.gie.fleet.dto.vehicule.CreateVehiculeRequest;
import com.gie.fleet.dto.vehicule.VehiculeDto;
import com.gie.fleet.entity.Chauffeur;
import com.gie.fleet.entity.Vehicule;
import com.gie.fleet.entity.enums.StatutVehicule;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.ChauffeurRepository;
import com.gie.fleet.repository.VehiculeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final ChauffeurRepository chauffeurRepository;

    @Transactional
    public VehiculeDto creer(Long gieId, CreateVehiculeRequest request) {
        if (vehiculeRepository.existsByGieIdAndImmatriculation(gieId, request.immatriculation())) {
            throw new BusinessException("Un véhicule avec cette immatriculation existe déjà pour ce GIE");
        }
        Vehicule vehicule = new Vehicule();
        vehicule.setGieId(gieId);
        vehicule.setImmatriculation(request.immatriculation());
        vehicule.setMarque(request.marque());
        vehicule.setModele(request.modele());
        vehicule.setAnnee(request.annee());
        vehicule.setCouleur(request.couleur());
        vehicule.setDateMiseEnService(request.dateMiseEnService());
        return toDto(vehiculeRepository.save(vehicule));
    }

    public List<VehiculeDto> lister(Long gieId) {
        return vehiculeRepository.findByGieId(gieId).stream().map(this::toDto).toList();
    }

    public VehiculeDto obtenir(Long gieId, Long id) {
        return toDto(getOrThrow(gieId, id));
    }

    @Transactional
    public VehiculeDto changerStatut(Long gieId, Long id, StatutVehicule statut) {
        Vehicule vehicule = getOrThrow(gieId, id);
        vehicule.setStatut(statut);
        return toDto(vehiculeRepository.save(vehicule));
    }

    private Vehicule getOrThrow(Long gieId, Long id) {
        return vehiculeRepository.findByIdAndGieId(id, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable : " + id));
    }

    private VehiculeDto toDto(Vehicule vehicule) {
        String chauffeurNomComplet = null;
        if (vehicule.getChauffeurActuelId() != null) {
            Optional<Chauffeur> chauffeur = chauffeurRepository.findById(vehicule.getChauffeurActuelId());
            chauffeurNomComplet = chauffeur.map(c -> c.getPrenom() + " " + c.getNom()).orElse(null);
        }
        return new VehiculeDto(
                vehicule.getId(), vehicule.getImmatriculation(), vehicule.getMarque(), vehicule.getModele(),
                vehicule.getAnnee(), vehicule.getCouleur(), vehicule.getStatut().name(),
                vehicule.getChauffeurActuelId(), chauffeurNomComplet, vehicule.getDateMiseEnService());
    }
}
