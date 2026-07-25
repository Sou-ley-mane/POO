package com.gie.fleet.entity;

import com.gie.fleet.entity.enums.Periodicite;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "type_frais")
@Getter
@Setter
@NoArgsConstructor
public class TypeFrais {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gie_id", nullable = false)
    private Long gieId;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Periodicite periodicite = Periodicite.MENSUELLE;

    @Column(nullable = false)
    private boolean actif = true;
}
