package com.gie.fleet.service;

import com.gie.fleet.dto.auth.ChangePinRequest;
import com.gie.fleet.dto.auth.LoginRequest;
import com.gie.fleet.dto.auth.LoginResponse;
import com.gie.fleet.entity.Utilisateur;
import com.gie.fleet.exception.AuthenticationLockedException;
import com.gie.fleet.exception.InvalidCredentialsException;
import com.gie.fleet.repository.UtilisateurRepository;
import com.gie.fleet.security.JwtService;
import com.gie.fleet.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.security.max-failed-attempts}")
    private int maxFailedAttempts;

    @Value("${app.security.lock-duration-minutes}")
    private long lockDurationMinutes;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(request.telephone())
                .orElseThrow(() -> new InvalidCredentialsException("Téléphone ou PIN incorrect"));

        if (utilisateur.getDateBlocage() != null && utilisateur.getDateBlocage().isAfter(LocalDateTime.now())) {
            String heure = utilisateur.getDateBlocage().format(DateTimeFormatter.ofPattern("HH:mm"));
            throw new AuthenticationLockedException(
                    "Compte temporairement bloqué suite à trop de tentatives échouées. Réessayez après " + heure + ".",
                    utilisateur.getDateBlocage());
        }

        if (!passwordEncoder.matches(request.pin(), utilisateur.getPinHash())) {
            enregistrerTentativeEchouee(utilisateur);
            throw new InvalidCredentialsException("Téléphone ou PIN incorrect");
        }

        utilisateur.setNombreTentativesEchouees(0);
        utilisateur.setDateBlocage(null);
        utilisateurRepository.save(utilisateur);

        SecurityUser securityUser = new SecurityUser(utilisateur);
        String token = jwtService.generateToken(securityUser);

        return new LoginResponse(
                token,
                utilisateur.getId(),
                utilisateur.getGieId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getRole().name(),
                utilisateur.isDoitChangerPin());
    }

    private void enregistrerTentativeEchouee(Utilisateur utilisateur) {
        int tentatives = utilisateur.getNombreTentativesEchouees() + 1;
        utilisateur.setNombreTentativesEchouees(tentatives);
        if (tentatives >= maxFailedAttempts) {
            utilisateur.setDateBlocage(LocalDateTime.now().plusMinutes(lockDurationMinutes));
        }
        utilisateurRepository.save(utilisateur);
    }

    @Transactional
    public void changerPin(Long userId, ChangePinRequest request) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(request.ancienPin(), utilisateur.getPinHash())) {
            throw new InvalidCredentialsException("Ancien PIN incorrect");
        }

        utilisateur.setPinHash(passwordEncoder.encode(request.nouveauPin()));
        utilisateur.setDoitChangerPin(false);
        utilisateurRepository.save(utilisateur);
    }

    /** Déblocage manuel par l'Admin GIE après le seuil de tentatives échouées. */
    @Transactional
    public void debloquerCompte(Long userId) {
        Utilisateur utilisateur = utilisateurRepository.findById(userId)
                .orElseThrow(() -> new InvalidCredentialsException("Utilisateur introuvable"));
        utilisateur.setNombreTentativesEchouees(0);
        utilisateur.setDateBlocage(null);
        utilisateurRepository.save(utilisateur);
    }
}
