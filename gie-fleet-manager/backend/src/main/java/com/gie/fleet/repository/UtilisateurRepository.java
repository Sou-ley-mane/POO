package com.gie.fleet.repository;

import com.gie.fleet.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    Optional<Utilisateur> findByTelephone(String telephone);

    boolean existsByTelephone(String telephone);

    List<Utilisateur> findByGieId(Long gieId);

    Optional<Utilisateur> findByIdAndGieId(Long id, Long gieId);
}
