import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'tableau-de-bord' },
  {
    path: 'connexion',
    loadComponent: () => import('./features/auth/login/login.component').then((m) => m.LoginComponent)
  },
  {
    path: 'tableau-de-bord',
    canActivate: [authGuard],
    loadComponent: () => import('./features/dashboard/dashboard.component').then((m) => m.DashboardComponent)
  },
  {
    path: 'vehicules',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/vehicules/vehicule-list/vehicule-list.component').then((m) => m.VehiculeListComponent)
  },
  {
    path: 'chauffeurs',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/chauffeurs/chauffeur-list/chauffeur-list.component').then((m) => m.ChauffeurListComponent)
  },
  {
    path: 'versements',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./features/versements/versement-grille.component').then((m) => m.VersementGrilleComponent)
  },
  {
    path: 'parametres',
    canActivate: [authGuard, roleGuard(['ADMIN_GIE', 'SUPER_ADMIN'])],
    loadComponent: () =>
      import('./features/admin/parametres-gie/parametres-gie.component').then((m) => m.ParametresGieComponent)
  },
  {
    path: 'administration-gie',
    canActivate: [authGuard, roleGuard(['SUPER_ADMIN'])],
    loadComponent: () =>
      import('./features/super-admin/gie-admin/gie-admin.component').then((m) => m.GieAdminComponent)
  },
  { path: '**', redirectTo: 'tableau-de-bord' }
];
