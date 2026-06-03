import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material/sort';
import { MatChipsModule } from '@angular/material/chips';
import { TransportService } from '../../services/transport.service';
import { BarChartComponent } from '../../charts/bar-chart/bar-chart.component';
import { LineChartComponent } from '../../charts/line-chart/line-chart.component';
import { DoughnutChartComponent } from '../../charts/doughnut-chart/doughnut-chart.component';
import { TransportChartData, AlertItem } from '../../models/transport.model';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [
    CommonModule, MatIconModule, MatButtonModule,
    MatTabsModule, MatTableModule, MatSortModule, MatChipsModule,
    BarChartComponent, LineChartComponent, DoughnutChartComponent
  ],
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {
  private service = inject(TransportService);

  weeklyData = signal<TransportChartData | null>(null);
  monthlyData = signal<TransportChartData | null>(null);
  radarData = signal<TransportChartData | null>(null);
  delayData = signal<TransportChartData | null>(null);
  satData = signal<TransportChartData | null>(null);
  alerts = signal<AlertItem[]>([]);
  loading = signal(true);

  criteria = [
    { icon: 'access_time',    label: 'Ponctualité',         score: 8.7 },
    { icon: 'clean_hands',    label: 'Propreté',            score: 7.4 },
    { icon: 'event_seat',     label: 'Confort',             score: 7.1 },
    { icon: 'security',       label: 'Sécurité',            score: 8.9 },
    { icon: 'directions',     label: 'Accessibilité',       score: 8.2 },
    { icon: 'support_agent',  label: 'Service client',      score: 7.0 },
    { icon: 'wifi',           label: 'Connectivité',        score: 6.5 },
    { icon: 'euro',           label: 'Rapport qualité/prix',score: 7.8 }
  ];

  reportTypes = [
    { icon: 'people',   label: 'Fréquentation',  value: '510k passagers/j',  trend: '+5.3%', up: true  },
    { icon: 'schedule', label: 'Ponctualité',     value: '87.4%',              trend: '+1.2%', up: true  },
    { icon: 'eco',      label: 'CO₂ économisé',   value: '2 840 T/mois',       trend: '+8.1%', up: true  },
    { icon: 'star',     label: 'Satisfaction',    value: '8.0 / 10',           trend: '+0.3',  up: true  }
  ];

  ngOnInit(): void {
    forkJoin({
      weekly: this.service.getWeeklyPassengers(),
      monthly: this.service.getMonthlyPassengers(),
      radar: this.service.getPerformanceRadar(),
      delay: this.service.getDelayTrend(),
      sat: this.service.getSatisfactionTrend(),
      alerts: this.service.getAlerts()
    }).subscribe(res => {
      this.weeklyData.set(res.weekly);
      this.monthlyData.set(res.monthly);
      this.radarData.set(res.radar);
      this.delayData.set(res.delay);
      this.satData.set(res.sat);
      this.alerts.set(res.alerts);
      this.loading.set(false);
    });
  }

  alertIcon(type: string): string {
    const m: Record<string,string> = { danger: 'error', warning: 'warning', info: 'info', success: 'check_circle' };
    return m[type] ?? 'info';
  }

  formatTime(d: Date): string {
    const mins = Math.floor((Date.now() - d.getTime()) / 60000);
    if (mins < 60) return `${mins} min`;
    return `${Math.floor(mins / 60)}h`;
  }
}
