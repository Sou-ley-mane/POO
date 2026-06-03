import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },
  {
    path: 'dashboard',
    loadComponent: () =>
      import('./dashboard/dashboard.component').then(m => m.DashboardComponent),
    title: 'Tableau de Bord — TransportStats'
  },
  {
    path: 'map',
    loadComponent: () =>
      import('./pages/map/map.component').then(m => m.MapComponent),
    title: 'Carte — TransportStats'
  },
  {
    path: 'reports',
    loadComponent: () =>
      import('./pages/reports/reports.component').then(m => m.ReportsComponent),
    title: 'Rapports — TransportStats'
  },
  {
    path: 'settings',
    loadComponent: () =>
      import('./pages/settings/settings.component').then(m => m.SettingsComponent),
    title: 'Paramètres — TransportStats'
  },
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];
