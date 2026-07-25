package com.gie.fleet.security;

import com.gie.fleet.entity.Utilisateur;
import com.gie.fleet.entity.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private SecurityUser user;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-key-for-jwt-signing-must-be-long-enough-256-bits", 60);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(42L);
        utilisateur.setGieId(7L);
        utilisateur.setTelephone("+221770000000");
        utilisateur.setPinHash("hash");
        utilisateur.setRole(Role.GESTIONNAIRE);
        utilisateur.setStatut("ACTIF");
        user = new SecurityUser(utilisateur);
    }

    @Test
    void genere_un_token_valide_contenant_les_claims_du_tenant() {
        String token = jwtService.generateToken(user);

        assertThat(jwtService.isValid(token)).isTrue();
        assertThat(jwtService.extractTelephone(token)).isEqualTo("+221770000000");
        assertThat(jwtService.extractUserId(token)).isEqualTo(42L);
        assertThat(jwtService.extractGieId(token)).isEqualTo(7L);
        assertThat(jwtService.extractRole(token)).isEqualTo("GESTIONNAIRE");
    }

    @Test
    void rejette_un_token_invalide() {
        assertThat(jwtService.isValid("token.invalide.xyz")).isFalse();
    }
}
