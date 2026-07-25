import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GieAdminService } from '../../../core/services/gie-admin.service';
import { CreateGieRequest, GieDto } from '../../../core/models/models';

@Component({
  selector: 'app-gie-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './gie-admin.component.html',
  styleUrl: './gie-admin.component.scss'
})
export class GieAdminComponent implements OnInit {
  gies = signal<GieDto[]>([]);
  afficherFormulaire = signal(false);
  erreur = signal<string | null>(null);

  nouveau: CreateGieRequest = {
    nom: '',
    sigle: '',
    adresse: '',
    telephone: '',
    email: '',
    telephoneAdmin: '',
    pinInitialAdmin: '',
    nomAdmin: '',
    prenomAdmin: ''
  };

  constructor(private readonly gieAdminService: GieAdminService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.gieAdminService.listerTous().subscribe((gies) => this.gies.set(gies));
  }

  creerGie(): void {
    this.erreur.set(null);
    this.gieAdminService.creer(this.nouveau).subscribe({
      next: () => {
        this.afficherFormulaire.set(false);
        this.nouveau = {
          nom: '', sigle: '', adresse: '', telephone: '', email: '',
          telephoneAdmin: '', pinInitialAdmin: '', nomAdmin: '', prenomAdmin: ''
        };
        this.charger();
      },
      error: (err) => this.erreur.set(err?.error?.message ?? 'Erreur lors de la création du GIE')
    });
  }

  suspendre(gie: GieDto): void {
    this.gieAdminService.suspendre(gie.id).subscribe(() => this.charger());
  }

  reactiver(gie: GieDto): void {
    this.gieAdminService.reactiver(gie.id).subscribe(() => this.charger());
  }
}
