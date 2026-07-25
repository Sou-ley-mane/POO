package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.ModePaiement;
import com.gie.fleet.entity.enums.StatutVersement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "versement")
@Getter
@Setter
@NoArgsConstructor
public class Versement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gie_id", nullable = false)
    private Long gieId;

    @Column(name = "chauffeur_id", nullable = false)
    private Long chauffeurId;

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;

    @Column(name = "periode_debut", nullable = false)
    private LocalDate periodeDebut;

    @Column(name = "periode_fin", nullable = false)
    private LocalDate periodeFin;

    /** Montant fixe défini pour cette période précise, sans report d'une période à l'autre. */
    @Column(name = "montant_attendu", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantAttendu;

    /** Informatif uniquement : n'entre pas dans le calcul du reste. */
    @Column(name = "montant_verse", nullable = false, precision = 12, scale = 2)
    private BigDecimal montantVerse = BigDecimal.ZERO;

    /** reste = montantAttendu - somme(listeFraisPeriode). Le montant versé n'est pas déduit. */
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal reste;

    @ManyToMany
    @JoinTable(
            name = "versement_frais",
            joinColumns = @JoinColumn(name = "versement_id"),
            inverseJoinColumns = @JoinColumn(name = "frais_vehicule_id")
    )
    private Set<FraisVehicule> listeFraisPeriode = new HashSet<>();

    @Column(name = "date_paiement")
    private LocalDateTime datePaiement;

    @Enumerated(EnumType.STRING)
    @Column(name = "mode_paiement", length = 20)
    private ModePaiement modePaiement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVersement statut = StatutVersement.IMPAYE;

    @Column(name = "saisi_par")
    private Long saisiPar;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
