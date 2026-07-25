package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.StatutGie;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "gie")
@Getter
@Setter
@NoArgsConstructor
public class Gie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nom;

    @Column(nullable = false, length = 30)
    private String sigle;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "couleur_primaire", nullable = false, length = 7)
    private String couleurPrimaire = "#0d6efd";

    @Column(name = "couleur_secondaire", nullable = false, length = 7)
    private String couleurSecondaire = "#6c757d";

    @Column(name = "couleur_accent", nullable = false, length = 7)
    private String couleurAccent = "#ffc107";

    private String adresse;

    private String telephone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutGie statut = StatutGie.ACTIF;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
}
