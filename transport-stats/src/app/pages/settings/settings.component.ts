import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { MatSliderModule } from '@angular/material/slider';
import { MatSelectModule } from '@angular/material/select';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDividerModule } from '@angular/material/divider';
import { TransportService } from '../../services/transport.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [
    CommonModule, FormsModule,
    MatIconModule, MatButtonModule, MatSlideToggleModule, MatSliderModule,
    MatSelectModule, MatInputModule, MatFormFieldModule, MatSnackBarModule, MatDividerModule
  ],
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent {
  private service = inject(TransportService);
  private snackBar = inject(MatSnackBar);

  isDark = signal(false);

  // Settings state
  settings = signal({
    darkMode: false,
    autoRefresh: true,
    refreshInterval: 30,
    language: 'fr',
    notifications: true,
    emailAlerts: false,
    soundAlerts: false,
    defaultView: 'dashboard',
    chartAnimations: true,
    compactMode: false,
    showCO2: true,
    showSatisfaction: true,
    apiUrl: 'https://api.transport-stats.fr/v1',
    alertThresholdDelay: 10,
    alertThresholdOccupancy: 90
  });

  languages = [
    { value: 'fr', label: 'Français' },
    { value: 'en', label: 'English' },
    { value: 'de', label: 'Deutsch' },
    { value: 'es', label: 'Español' }
  ];

  indicators = [
    { icon: 'people',            color: '#1a73e8', label: 'Nombre de passagers',   enabled: true  },
    { icon: 'speed',             color: '#9c27b0', label: 'Vitesse moyenne',        enabled: true  },
    { icon: 'schedule',          color: '#ff9800', label: 'Retards moyens',         enabled: true  },
    { icon: 'event_seat',        color: '#00bcd4', label: "Taux d'occupation",     enabled: true  },
    { icon: 'eco',               color: '#4caf50', label: 'Émissions CO₂',          enabled: true  },
    { icon: 'sentiment_satisfied', color: '#00c896', label: 'Satisfaction clients', enabled: true  },
    { icon: 'warning',           color: '#f44336', label: 'Incidents actifs',       enabled: true  },
    { icon: 'attach_money',      color: '#ffc107', label: 'Recettes',               enabled: false }
  ];

  defaultViews = [
    { value: 'dashboard', label: 'Tableau de Bord' },
    { value: 'map',       label: 'Carte' },
    { value: 'reports',   label: 'Rapports' }
  ];

  toggleDark(): void {
    const s = this.settings();
    this.settings.set({ ...s, darkMode: !s.darkMode });
    this.service.toggleTheme();
  }

  saveSettings(): void {
    this.snackBar.open('Paramètres sauvegardés avec succès !', 'OK', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top',
      panelClass: ['snack-success']
    });
  }

  resetSettings(): void {
    this.snackBar.open('Paramètres réinitialisés', 'OK', {
      duration: 3000,
      horizontalPosition: 'end',
      verticalPosition: 'top'
    });
  }
}
