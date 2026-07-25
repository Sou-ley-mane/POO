package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.Periodicite;
import com.gie.fleet.entity.enums.StatutChauffeur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "chauffeur")
@Getter
@Setter
@NoArgsConstructor
public class Chauffeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gie_id", nullable = false)
    private Long gieId;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    private String telephone;

    @Column(name = "numero_permis")
    private String numeroPermis;

    @Column(name = "numero_cni")
    private String numeroCni;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutChauffeur statut = StatutChauffeur.ACTIF;

    @Column(name = "vehicule_affecte_id")
    private Long vehiculeAffecteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "periodicite_versement", nullable = false, length = 20)
    private Periodicite periodiciteVersement = Periodicite.HEBDOMADAIRE;

    @Column(name = "montant_attendu_par_periode", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantAttenduParPeriode = BigDecimal.ZERO;
}
