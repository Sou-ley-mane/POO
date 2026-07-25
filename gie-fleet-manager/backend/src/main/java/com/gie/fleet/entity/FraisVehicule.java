package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.StatutFrais;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "frais_vehicule")
@Getter
@Setter
@NoArgsConstructor
public class FraisVehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicule_id", nullable = false)
    private Long vehiculeId;

    @Column(name = "type_frais_id", nullable = false)
    private Long typeFraisId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutFrais statut = StatutFrais.EN_ATTENTE;
}
