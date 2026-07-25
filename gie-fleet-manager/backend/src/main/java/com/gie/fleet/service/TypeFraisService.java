package com.gie.fleet.service;

import com.gie.fleet.dto.typefrais.CreateTypeFraisRequest;
import com.gie.fleet.dto.typefrais.TypeFraisDto;
import com.gie.fleet.entity.TypeFrais;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.TypeFraisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeFraisService {

    private final TypeFraisRepository typeFraisRepository;

    @Transactional
    public TypeFraisDto creer(Long gieId, CreateTypeFraisRequest request) {
        TypeFrais typeFrais = new TypeFrais();
        typeFrais.setGieId(gieId);
        typeFrais.setLibelle(request.libelle());
        typeFrais.setMontant(request.montant());
        typeFrais.setPeriodicite(request.periodicite());
        return toDto(typeFraisRepository.save(typeFrais));
    }

    public List<TypeFraisDto> lister(Long gieId) {
        return typeFraisRepository.findByGieId(gieId).stream().map(this::toDto).toList();
    }

    @Transactional
    public TypeFraisDto activerDesactiver(Long gieId, Long id, boolean actif) {
        TypeFrais typeFrais = typeFraisRepository.findByIdAndGieId(id, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Type de frais introuvable : " + id));
        typeFrais.setActif(actif);
        return toDto(typeFraisRepository.save(typeFrais));
    }

    private TypeFraisDto toDto(TypeFrais typeFrais) {
        return new TypeFraisDto(typeFrais.getId(), typeFrais.getLibelle(), typeFrais.getMontant(),
                typeFrais.getPeriodicite().name(), typeFrais.isActif());
    }
}
