import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { forkJoin, Subscription } from 'rxjs';

import { TransportService } from '../services/transport.service';
import { KpiCardComponent } from '../shared/kpi-card/kpi-card.component';
import { BarChartComponent } from '../charts/bar-chart/bar-chart.component';
import { LineChartComponent } from '../charts/line-chart/line-chart.component';
import { DoughnutChartComponent } from '../charts/doughnut-chart/doughnut-chart.component';
import {
  KpiCard, TransportStats, TransportChartData,
  RoutePerformance, AlertItem
} from '../models/transport.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule, MatIconModule, MatButtonModule,
    MatTabsModule, MatSelectModule, MatProgressBarModule, MatTooltipModule,
    KpiCardComponent, BarChartComponent, LineChartComponent, DoughnutChartComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  private service = inject(TransportService);
  private sub = new Subscription();

  // Data signals
  kpis = signal<KpiCard[]>([]);
  transportStats = signal<TransportStats[]>([]);
  weeklyData = signal<TransportChartData | null>(null);
  monthlyData = signal<TransportChartData | null>(null);
  modalShareData = signal<TransportChartData | null>(null);
  co2Data = signal<TransportChartData | null>(null);
  delayTrendData = signal<TransportChartData | null>(null);
  satisfactionData = signal<TransportChartData | null>(null);
  routes = signal<RoutePerformance[]>([]);
  alerts = signal<AlertItem[]>([]);

  loading = signal(true);
  selectedPeriod = signal('week');
  lastRefresh = new Date();

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  loadData(): void {
    this.loading.set(true);
    this.sub.add(
      forkJoin({
        kpis: this.service.getKpiCards(),
        stats: this.service.getTransportStats(),
        weekly: this.service.getWeeklyPassengers(),
        monthly: this.service.getMonthlyPassengers(),
        modal: this.service.getModalShare(),
        co2: this.service.getCO2Emissions(),
        delay: this.service.getDelayTrend(),
        satisfaction: this.service.getSatisfactionTrend(),
        routes: this.service.getRoutePerformance(),
        alerts: this.service.getAlerts()
      }).subscribe(res => {
        this.kpis.set(res.kpis);
        this.transportStats.set(res.stats);
        this.weeklyData.set(res.weekly);
        this.monthlyData.set(res.monthly);
        this.modalShareData.set(res.modal);
        this.co2Data.set(res.co2);
        this.delayTrendData.set(res.delay);
        this.satisfactionData.set(res.satisfaction);
        this.routes.set(res.routes);
        this.alerts.set(res.alerts);
        this.loading.set(false);
        this.lastRefresh = new Date();
      })
    );
  }

  refresh(): void {
    this.loadData();
  }

  getTransportIcon(type: string): string {
    const icons: Record<string, string> = {
      bus: 'directions_bus', train: 'train', taxi: 'local_taxi',
      metro: 'subway', velo: 'pedal_bike'
    };
    return icons[type] ?? 'directions_transit';
  }

  getTransportColor(type: string): string {
    const colors: Record<string, string> = {
      bus: '#2196f3', train: '#9c27b0', taxi: '#ffc107',
      metro: '#f44336', velo: '#4caf50'
    };
    return colors[type] ?? '#607d8b';
  }

  getTrendIcon(trend: string): string {
    if (trend === 'up') return 'trending_up';
    if (trend === 'down') return 'trending_down';
    return 'trending_flat';
  }

  getPunctualityClass(v: number): string {
    if (v >= 90) return 'success';
    if (v >= 75) return 'warning';
    return 'danger';
  }

  alertIcon(type: string): string {
    const m: Record<string, string> = {
      danger: 'error', warning: 'warning', info: 'info', success: 'check_circle'
    };
    return m[type] ?? 'info';
  }

  alertClass(type: string): string {
    return `alert-item alert--${type}`;
  }

  formatTime(d: Date): string {
    const mins = Math.floor((Date.now() - d.getTime()) / 60000);
    if (mins < 60) return `Il y a ${mins} min`;
    const h = Math.floor(mins / 60);
    return `Il y a ${h}h`;
  }

  get activeAlerts(): AlertItem[] {
    return this.alerts().filter(a => !a.resolved);
  }
}
