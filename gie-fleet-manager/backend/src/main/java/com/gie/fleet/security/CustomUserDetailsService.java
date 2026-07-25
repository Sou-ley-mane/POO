package com.gie.fleet.security;

import com.gie.fleet.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        return utilisateurRepository.findByTelephone(telephone)
                .map(SecurityUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur inconnu : " + telephone));
    }
}
