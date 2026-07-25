import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChauffeurService } from '../../core/services/chauffeur.service';
import { VersementService } from '../../core/services/versement.service';
import { StatusBadgeComponent } from '../../shared/components/status-badge/status-badge.component';
import { ChauffeurDto, VersementGrilleLigneDto } from '../../core/models/models';

@Component({
  selector: 'app-versement-grille',
  standalone: true,
  imports: [CommonModule, FormsModule, StatusBadgeComponent],
  templateUrl: './versement-grille.component.html',
  styleUrl: './versement-grille.component.scss'
})
export class VersementGrilleComponent implements OnInit {
  lignes = signal<VersementGrilleLigneDto[]>([]);
  chauffeurs = signal<ChauffeurDto[]>([]);
  afficherFormulaire = signal(false);

  colonnesFrais = computed(() => {
    const colonnes = new Set<string>();
    for (const ligne of this.lignes()) {
      Object.keys(ligne.montantParTypeFrais).forEach((c) => colonnes.add(c));
    }
    return Array.from(colonnes);
  });

  saisie = {
    chauffeurId: null as number | null,
    montantVerse: 0,
    modePaiement: 'ESPECES' as 'ESPECES' | 'MOBILE_MONEY' | 'VIREMENT'
  };

  constructor(
    private readonly versementService: VersementService,
    private readonly chauffeurService: ChauffeurService
  ) {}

  ngOnInit(): void {
    this.charger();
    this.chauffeurService.lister().subscribe((c) => this.chauffeurs.set(c));
  }

  charger(): void {
    this.versementService.grille().subscribe((lignes) => this.lignes.set(lignes));
  }

  saisirVersement(): void {
    if (!this.saisie.chauffeurId) {
      return;
    }
    this.versementService
      .saisir({
        chauffeurId: this.saisie.chauffeurId,
        montantVerse: this.saisie.montantVerse,
        modePaiement: this.saisie.modePaiement
      })
      .subscribe(() => {
        this.afficherFormulaire.set(false);
        this.saisie = { chauffeurId: null, montantVerse: 0, modePaiement: 'ESPECES' };
        this.charger();
      });
  }
}
