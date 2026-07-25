import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ChauffeurService } from '../../../core/services/chauffeur.service';
import { VehiculeService } from '../../../core/services/vehicule.service';
import { ChauffeurDto, VehiculeDto } from '../../../core/models/models';

@Component({
  selector: 'app-chauffeur-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chauffeur-list.component.html',
  styleUrl: './chauffeur-list.component.scss'
})
export class ChauffeurListComponent implements OnInit {
  chauffeurs = signal<ChauffeurDto[]>([]);
  vehicules = signal<VehiculeDto[]>([]);
  afficherFormulaire = signal(false);

  nouveau = {
    nom: '',
    prenom: '',
    telephone: '',
    periodiciteVersement: 'HEBDOMADAIRE',
    montantAttenduParPeriode: 0
  };

  vehiculeChoisiParChauffeur: Record<number, number> = {};

  constructor(
    private readonly chauffeurService: ChauffeurService,
    private readonly vehiculeService: VehiculeService
  ) {}

  ngOnInit(): void {
    this.charger();
    this.vehiculeService.lister().subscribe((vehicules) => this.vehicules.set(vehicules));
  }

  charger(): void {
    this.chauffeurService.lister().subscribe((chauffeurs) => this.chauffeurs.set(chauffeurs));
  }

  creerChauffeur(): void {
    this.chauffeurService.creer(this.nouveau).subscribe(() => {
      this.afficherFormulaire.set(false);
      this.nouveau = { nom: '', prenom: '', telephone: '', periodiciteVersement: 'HEBDOMADAIRE', montantAttenduParPeriode: 0 };
      this.charger();
    });
  }

  affecter(chauffeurId: number): void {
    const vehiculeId = this.vehiculeChoisiParChauffeur[chauffeurId];
    if (!vehiculeId) {
      return;
    }
    this.chauffeurService.affecter(chauffeurId, vehiculeId).subscribe(() => this.charger());
  }

  retirerAffectation(chauffeurId: number): void {
    this.chauffeurService.retirerAffectation(chauffeurId).subscribe(() => this.charger());
  }
}
