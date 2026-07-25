import { NgClass } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-status-badge',
  standalone: true,
  imports: [NgClass],
  template: `<span class="badge" [ngClass]="cssClass()">{{ label() }}</span>`,
  styles: [':host { display: inline-block; }']
})
export class StatusBadgeComponent {
  @Input({ required: true }) statut!: 'SOLDE' | 'PARTIEL' | 'IMPAYE' | string;

  label(): string {
    switch (this.statut) {
      case 'SOLDE':
        return 'Soldé';
      case 'PARTIEL':
        return 'Partiel';
      case 'IMPAYE':
        return 'Impayé';
      default:
        return this.statut;
    }
  }

  cssClass(): string {
    switch (this.statut) {
      case 'SOLDE':
        return 'badge-solde';
      case 'PARTIEL':
        return 'badge-partiel';
      case 'IMPAYE':
        return 'badge-impaye';
      default:
        return '';
    }
  }
}
