import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UtilisateurService } from '../../../core/services/utilisateur.service';
import { CreateGestionnaireRequest, UtilisateurDto } from '../../../core/models/models';

@Component({
  selector: 'app-equipe-gie',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './equipe-gie.component.html',
  styleUrl: './equipe-gie.component.scss'
})
export class EquipeGieComponent implements OnInit {
  equipe = signal<UtilisateurDto[]>([]);
  afficherFormulaire = signal(false);
  erreur = signal<string | null>(null);

  nouveau: CreateGestionnaireRequest = {
    nom: '',
    prenom: '',
    telephone: '',
    email: '',
    pinInitial: ''
  };

  constructor(private readonly utilisateurService: UtilisateurService) {}

  ngOnInit(): void {
    this.charger();
  }

  charger(): void {
    this.utilisateurService.lister().subscribe((equipe) => this.equipe.set(equipe));
  }

  creerGestionnaire(): void {
    this.erreur.set(null);
    this.utilisateurService.creerGestionnaire(this.nouveau).subscribe({
      next: () => {
        this.afficherFormulaire.set(false);
        this.nouveau = { nom: '', prenom: '', telephone: '', email: '', pinInitial: '' };
        this.charger();
      },
      error: (err) => this.erreur.set(err?.error?.message ?? 'Erreur lors de la création du compte')
    });
  }

  changerStatut(utilisateur: UtilisateurDto, statut: string): void {
    this.utilisateurService.changerStatut(utilisateur.id, statut).subscribe(() => this.charger());
  }
}
