import { CommonModule } from '@angular/common';
import { Component, OnInit, signal } from '@angular/core';
import { AuthService } from '../../core/services/auth.service';
import { DashboardService } from '../../core/services/dashboard.service';
import { GieService } from '../../core/services/gie.service';
import { ThemeService } from '../../core/services/theme.service';
import { DashboardStatsDto } from '../../core/models/models';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  stats = signal<DashboardStatsDto | null>(null);

  constructor(
    private readonly dashboardService: DashboardService,
    private readonly gieService: GieService,
    private readonly themeService: ThemeService,
    private readonly authService: AuthService
  ) {}

  ngOnInit(): void {
    // Le SUPER_ADMIN n'appartient à aucun GIE : ni thème ni statistiques de GIE à charger.
    if (this.authService.role() === 'SUPER_ADMIN') {
      return;
    }
    this.gieService.monGie().subscribe((gie) => this.themeService.appliquer(gie));
    this.dashboardService.obtenirStats().subscribe((stats) => this.stats.set(stats));
  }
}
