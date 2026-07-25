package com.gie.fleet.service;

import com.gie.fleet.dto.utilisateur.CreateGestionnaireRequest;
import com.gie.fleet.dto.utilisateur.UtilisateurDto;
import com.gie.fleet.entity.Utilisateur;
import com.gie.fleet.entity.enums.Role;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Gestion des comptes de l'équipe d'un GIE par son ADMIN_GIE. Seuls les comptes GESTIONNAIRE
 * sont créés ici : les comptes ADMIN_GIE ne sont créés que par le SUPER_ADMIN, en même temps
 * que le GIE (voir GieService.creerGie).
 */
@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UtilisateurDto creerGestionnaire(Long gieId, CreateGestionnaireRequest request) {
        if (utilisateurRepository.existsByTelephone(request.telephone())) {
            throw new BusinessException("Ce numéro de téléphone est déjà utilisé");
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setGieId(gieId);
        utilisateur.setNom(request.nom());
        utilisateur.setPrenom(request.prenom());
        utilisateur.setTelephone(request.telephone());
        utilisateur.setEmail(request.email());
        utilisateur.setPinHash(passwordEncoder.encode(request.pinInitial()));
        utilisateur.setRole(Role.GESTIONNAIRE);
        utilisateur.setDoitChangerPin(true);

        return toDto(utilisateurRepository.save(utilisateur));
    }

    public List<UtilisateurDto> listerEquipe(Long gieId) {
        return utilisateurRepository.findByGieId(gieId).stream()
                .filter(u -> u.getRole() != Role.CHAUFFEUR)
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public UtilisateurDto changerStatut(Long gieId, Long id, String statut) {
        Utilisateur utilisateur = utilisateurRepository.findByIdAndGieId(id, gieId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
        if (utilisateur.getRole() == Role.ADMIN_GIE) {
            throw new BusinessException("Impossible de modifier le statut d'un compte ADMIN_GIE depuis cet écran");
        }
        utilisateur.setStatut(statut);
        return toDto(utilisateurRepository.save(utilisateur));
    }

    private UtilisateurDto toDto(Utilisateur utilisateur) {
        return new UtilisateurDto(
                utilisateur.getId(), utilisateur.getNom(), utilisateur.getPrenom(),
                utilisateur.getTelephone(), utilisateur.getEmail(), utilisateur.getRole().name(),
                utilisateur.getStatut(), utilisateur.isDoitChangerPin());
    }
}
