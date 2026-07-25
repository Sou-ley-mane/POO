package com.gie.fleet.security;

import com.gie.fleet.entity.Utilisateur;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class SecurityUser implements UserDetails {

    private final Long id;
    private final Long gieId;
    private final String telephone;
    private final String pinHash;
    private final String role;
    private final boolean actif;

    public SecurityUser(Utilisateur utilisateur) {
        this.id = utilisateur.getId();
        this.gieId = utilisateur.getGieId();
        this.telephone = utilisateur.getTelephone();
        this.pinHash = utilisateur.getPinHash();
        this.role = utilisateur.getRole().name();
        this.actif = "ACTIF".equals(utilisateur.getStatut());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() {
        return pinHash;
    }

    @Override
    public String getUsername() {
        return telephone;
    }

    @Override
    public boolean isEnabled() {
        return actif;
    }
}
