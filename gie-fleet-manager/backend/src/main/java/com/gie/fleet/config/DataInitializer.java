package com.gie.fleet.config;

import com.gie.fleet.entity.Utilisateur;
import com.gie.fleet.entity.enums.Role;
import com.gie.fleet.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crée un compte SUPER_ADMIN par défaut au premier démarrage si aucun n'existe,
 * pour amorcer la plateforme (création des GIE). Le PIN doit être changé à la première connexion.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.super-admin.telephone:+000000000}")
    private String superAdminTelephone;

    @Value("${app.super-admin.pin-initial:0000}")
    private String superAdminPinInitial;

    @Override
    public void run(String... args) {
        boolean superAdminExiste = utilisateurRepository.findByTelephone(superAdminTelephone).isPresent();
        if (superAdminExiste) {
            return;
        }

        Utilisateur superAdmin = new Utilisateur();
        superAdmin.setNom("Super");
        superAdmin.setPrenom("Admin");
        superAdmin.setTelephone(superAdminTelephone);
        superAdmin.setPinHash(passwordEncoder.encode(superAdminPinInitial));
        superAdmin.setRole(Role.SUPER_ADMIN);
        superAdmin.setDoitChangerPin(true);
        utilisateurRepository.save(superAdmin);

        log.warn("Compte SUPER_ADMIN initial créé (téléphone: {}). Changez le PIN dès la première connexion.",
                superAdminTelephone);
    }
}
