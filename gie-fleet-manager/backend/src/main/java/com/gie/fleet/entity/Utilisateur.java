package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateur")
@Getter
@Setter
@NoArgsConstructor
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nullable pour SUPER_ADMIN, qui n'appartient à aucun GIE. */
    @Column(name = "gie_id")
    private Long gieId;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(nullable = false, unique = true, length = 20)
    private String telephone;

    @Column(name = "pin_hash", nullable = false)
    private String pinHash;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false, length = 20)
    private String statut = "ACTIF";

    @Column(name = "doit_changer_pin", nullable = false)
    private boolean doitChangerPin = true;

    @Column(name = "nombre_tentatives_echouees", nullable = false)
    private int nombreTentativesEchouees = 0;

    @Column(name = "date_blocage")
    private LocalDateTime dateBlocage;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
