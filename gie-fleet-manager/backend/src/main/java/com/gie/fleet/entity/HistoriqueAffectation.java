package com.gie.fleet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historique_affectation")
@Getter
@Setter
@NoArgsConstructor
public class HistoriqueAffectation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gie_id", nullable = false)
    private Long gieId;

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;

    @Column(name = "chauffeur_id", nullable = false)
    private Long chauffeurId;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut = LocalDateTime.now();

    @Column(name = "date_fin")
    private LocalDateTime dateFin;
}
