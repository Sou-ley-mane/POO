import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { VehiculeService } from '../../../core/services/vehicule.service';
import { VehiculeDto } from '../../../core/models/models';

@Component({
  selector: 'app-vehicule-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vehicule-list.component.html',
  styleUrl: './vehicule-list.component.scss'
})
export class VehiculeListComponent implements OnInit {
  vehicules = signal<VehiculeDto[]>([]);
  afficherFormulaire = signal(false);

  nouveau = {
    immatriculation: '',
    marque: '',
    modele: '',
    annee: null as number | null,
    couleur: ''
  };

  constructor(private readonly vehiculeService: VehiculeService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.vehiculeService.lister().subscribe((vehicules) => this.vehicules.set(vehicules));
  }

  creerVehicule(): void {
    this.vehiculeService.creer(this.nouveau).subscribe(() => {
      this.afficherFormulaire.set(false);
      this.nouveau = { immatriculation: '', marque: '', modele: '', annee: null, couleur: '' };
      this.charger();
    });
  }
}
