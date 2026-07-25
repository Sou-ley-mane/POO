package com.gie.fleet.service;

import com.gie.fleet.entity.FraisVehicule;
import com.gie.fleet.entity.TypeFrais;
import com.gie.fleet.entity.enums.StatutFrais;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.FraisVehiculeRepository;
import com.gie.fleet.repository.TypeFraisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FraisVehiculeService {

    private final FraisVehiculeRepository fraisVehiculeRepository;
    private final TypeFraisRepository typeFraisRepository;

    @Transactional
    public FraisVehicule creer(Long gieId, Long vehiculeId, Long typeFraisId, LocalDate dateEcheance) {
        TypeFrais typeFrais = typeFraisRepository.findByIdAndGieId(typeFraisId, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Type de frais introuvable : " + typeFraisId));

        FraisVehicule frais = new FraisVehicule();
        frais.setVehiculeId(vehiculeId);
        frais.setTypeFraisId(typeFraisId);
        frais.setMontant(typeFrais.getMontant());
        frais.setDateEcheance(dateEcheance);
        frais.setStatut(StatutFrais.EN_ATTENTE);
        return fraisVehiculeRepository.save(frais);
    }

    public List<FraisVehicule> listerParVehicule(Long vehiculeId) {
        return fraisVehiculeRepository.findByVehiculeId(vehiculeId);
    }

    public List<FraisVehicule> listerParPeriode(Long vehiculeId, LocalDate debut, LocalDate fin) {
        return fraisVehiculeRepository.findByVehiculeIdAndDateEcheanceBetween(vehiculeId, debut, fin);
    }
}
