package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.StatutVehicule;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gie_id", nullable = false)
    private Long gieId;

    @Column(nullable = false, length = 20)
    private String immatriculation;

    private String marque;

    private String modele;

    private Integer annee;

    private String couleur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutVehicule statut = StatutVehicule.EN_CIRCULATION;

    /** Affectation active unique : un seul chauffeur actif possible à un instant T. */
    @Column(name = "chauffeur_actuel_id")
    private Long chauffeurActuelId;

    @Column(name = "date_mise_en_service")
    private LocalDate dateMiseEnService;
}
