package com.gie.fleet.service;

import com.gie.fleet.dto.gie.CreateGieRequest;
import com.gie.fleet.dto.gie.GieDto;
import com.gie.fleet.dto.gie.UpdateThemeRequest;
import com.gie.fleet.entity.Gie;
import com.gie.fleet.entity.Utilisateur;
import com.gie.fleet.entity.enums.Role;
import com.gie.fleet.exception.BusinessException;
import com.gie.fleet.exception.ResourceNotFoundException;
import com.gie.fleet.repository.GieRepository;
import com.gie.fleet.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GieService {

    private final GieRepository gieRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public GieDto creerGie(CreateGieRequest request) {
        if (utilisateurRepository.existsByTelephone(request.telephoneAdmin())) {
            throw new BusinessException("Ce numéro de téléphone administrateur est déjà utilisé");
        }

        Gie gie = new Gie();
        gie.setNom(request.nom());
        gie.setSigle(request.sigle());
        gie.setAdresse(request.adresse());
        gie.setTelephone(request.telephone());
        gie.setEmail(request.email());
        gie = gieRepository.save(gie);

        Utilisateur admin = new Utilisateur();
        admin.setGieId(gie.getId());
        admin.setNom(request.nomAdmin());
        admin.setPrenom(request.prenomAdmin());
        admin.setTelephone(request.telephoneAdmin());
        admin.setPinHash(passwordEncoder.encode(request.pinInitialAdmin()));
        admin.setRole(Role.ADMIN_GIE);
        admin.setDoitChangerPin(true);
        utilisateurRepository.save(admin);

        return toDto(gie);
    }

    public List<GieDto> listerTous() {
        return gieRepository.findAll().stream().map(this::toDto).toList();
    }

    public GieDto obtenir(Long id) {
        return toDto(getOrThrow(id));
    }

    @Transactional
    public GieDto mettreAJourTheme(Long gieId, UpdateThemeRequest request) {
        Gie gie = getOrThrow(gieId);
        gie.setCouleurPrimaire(request.couleurPrimaire());
        gie.setCouleurSecondaire(request.couleurSecondaire());
        gie.setCouleurAccent(request.couleurAccent());
        if (request.logoUrl() != null) {
            gie.setLogoUrl(request.logoUrl());
        }
        return toDto(gieRepository.save(gie));
    }

    @Transactional
    public void suspendre(Long gieId) {
        Gie gie = getOrThrow(gieId);
        gie.setStatut(com.gie.fleet.entity.enums.StatutGie.SUSPENDU);
        gieRepository.save(gie);
    }

    @Transactional
    public void reactiver(Long gieId) {
        Gie gie = getOrThrow(gieId);
        gie.setStatut(com.gie.fleet.entity.enums.StatutGie.ACTIF);
        gieRepository.save(gie);
    }

    private Gie getOrThrow(Long id) {
        return gieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GIE introuvable : " + id));
    }

    private GieDto toDto(Gie gie) {
        return new GieDto(
                gie.getId(), gie.getNom(), gie.getSigle(), gie.getLogoUrl(),
                gie.getCouleurPrimaire(), gie.getCouleurSecondaire(), gie.getCouleurAccent(),
                gie.getAdresse(), gie.getTelephone(), gie.getEmail(), gie.getStatut().name());
    }
}
