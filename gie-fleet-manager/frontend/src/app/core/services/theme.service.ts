import { Injectable } from '@angular/core';
import { GieDto } from '../models/models';

/**
 * Applique le thème (couleurs, logo) du GIE connecté à toute l'interface via des variables CSS,
 * façon "marque blanche". Appelé au chargement du dashboard une fois le GIE de l'utilisateur récupéré.
 */
@Injectable({ providedIn: 'root' })
export class ThemeService {
  appliquer(gie: GieDto): void {
    const root = document.documentElement.style;
    root.setProperty('--color-primary', gie.couleurPrimaire);
    root.setProperty('--color-secondary', gie.couleurSecondaire);
    root.setProperty('--color-accent', gie.couleurAccent);
  }

  reinitialiser(): void {
    const root = document.documentElement.style;
    root.removeProperty('--color-primary');
    root.removeProperty('--color-secondary');
    root.removeProperty('--color-accent');
  }
}
