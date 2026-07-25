import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import { GieService } from '../../../core/services/gie.service';
import { ThemeService } from '../../../core/services/theme.service';
import { GieDto, TypeFraisDto } from '../../../core/models/models';

@Component({
  selector: 'app-parametres-gie',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './parametres-gie.component.html',
  styleUrl: './parametres-gie.component.scss'
})
export class ParametresGieComponent implements OnInit {
  gie = signal<GieDto | null>(null);
  typesFrais = signal<TypeFraisDto[]>([]);
  afficherFormulaireFrais = signal(false);

  theme = { couleurPrimaire: '#0d6efd', couleurSecondaire: '#6c757d', couleurAccent: '#ffc107' };
  nouveauTypeFrais = { libelle: '', montant: 0, periodicite: 'MENSUELLE' };

  constructor(
    private readonly gieService: GieService,
    private readonly themeService: ThemeService,
    private readonly http: HttpClient
  ) {}

  ngOnInit(): void {
    this.gieService.monGie().subscribe((gie) => {
      this.gie.set(gie);
      this.theme = {
        couleurPrimaire: gie.couleurPrimaire,
        couleurSecondaire: gie.couleurSecondaire,
        couleurAccent: gie.couleurAccent
      };
    });
    this.chargerTypesFrais();
  }

  chargerTypesFrais(): void {
    this.http.get<TypeFraisDto[]>(`${environment.apiUrl}/types-frais`).subscribe((types) => this.typesFrais.set(types));
  }

  enregistrerTheme(): void {
    this.gieService.mettreAJourTheme(this.theme).subscribe((gie) => {
      this.gie.set(gie);
      this.themeService.appliquer(gie);
    });
  }

  creerTypeFrais(): void {
    this.http.post<TypeFraisDto>(`${environment.apiUrl}/types-frais`, this.nouveauTypeFrais).subscribe(() => {
      this.afficherFormulaireFrais.set(false);
      this.nouveauTypeFrais = { libelle: '', montant: 0, periodicite: 'MENSUELLE' };
      this.chargerTypesFrais();
    });
  }

  activerDesactiver(id: number, actif: boolean): void {
    this.http.patch(`${environment.apiUrl}/types-frais/${id}/actif?actif=${!actif}`, {}).subscribe(() => this.chargerTypesFrais());
  }
}
