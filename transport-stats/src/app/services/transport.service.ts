import { Injectable, signal } from '@angular/core';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { delay, map } from 'rxjs/operators';
import {
  TransportStats,
  KpiCard,
  TransportChartData,
  DailyStats,
  MonthlyStats,
  DelayStats,
  RoutePerformance,
  AlertItem,
  ThemeConfig
} from '../models/transport.model';

@Injectable({ providedIn: 'root' })
export class TransportService {

  // ─── Theme State ──────────────────────────────────────────────────────────
  private _theme = signal<ThemeConfig>({
    isDark: false,
    primaryColor: '#1a73e8',
    accentColor: '#00c896'
  });

  readonly theme = this._theme.asReadonly();

  private themeSubject = new BehaviorSubject<boolean>(false);
  isDark$ = this.themeSubject.asObservable();

  toggleTheme(): void {
    const current = this._theme();
    const isDark = !current.isDark;
    this._theme.set({ ...current, isDark });
    this.themeSubject.next(isDark);
    if (isDark) {
      document.body.classList.add('dark-theme');
    } else {
      document.body.classList.remove('dark-theme');
    }
  }

  // ─── Mock Transport Stats ─────────────────────────────────────────────────
  getTransportStats(): Observable<TransportStats[]> {
    const stats: TransportStats[] = [
      {
        id: 'bus-001',
        type: 'bus',
        label: 'Bus',
        icon: 'directions_bus',
        color: '#2196f3',
        passagers: 142_580,
        vitesseMoyenne: 22.4,
        retards: 4.2,
        tauxOccupation: 68,
        emissionsCO2: 89,
        satisfaction: 7.2,
        timestamp: new Date()
      },
      {
        id: 'train-001',
        type: 'train',
        label: 'Train',
        icon: 'train',
        color: '#9c27b0',
        passagers: 89_340,
        vitesseMoyenne: 140.6,
        retards: 2.8,
        tauxOccupation: 74,
        emissionsCO2: 41,
        satisfaction: 8.1,
        timestamp: new Date()
      },
      {
        id: 'taxi-001',
        type: 'taxi',
        label: 'Taxi',
        icon: 'local_taxi',
        color: '#ffc107',
        passagers: 28_760,
        vitesseMoyenne: 31.2,
        retards: 6.5,
        tauxOccupation: 55,
        emissionsCO2: 142,
        satisfaction: 7.8,
        timestamp: new Date()
      },
      {
        id: 'metro-001',
        type: 'metro',
        label: 'Métro',
        icon: 'subway',
        color: '#f44336',
        passagers: 215_890,
        vitesseMoyenne: 38.0,
        retards: 1.4,
        tauxOccupation: 82,
        emissionsCO2: 11,
        satisfaction: 7.9,
        timestamp: new Date()
      },
      {
        id: 'velo-001',
        type: 'velo',
        label: 'Vélos',
        icon: 'pedal_bike',
        color: '#4caf50',
        passagers: 34_210,
        vitesseMoyenne: 14.8,
        retards: 0,
        tauxOccupation: 100,
        emissionsCO2: 0,
        satisfaction: 9.1,
        timestamp: new Date()
      }
    ];
    return of(stats).pipe(delay(300));
  }

  // ─── KPI Cards ────────────────────────────────────────────────────────────
  getKpiCards(): Observable<KpiCard[]> {
    const kpis: KpiCard[] = [
      {
        id: 'kpi-passagers',
        title: 'Passagers Aujourd\'hui',
        value: 510_780,
        unit: '',
        icon: 'people',
        trend: 'up',
        trendValue: 5.3,
        trendLabel: 'vs hier',
        variant: 'primary',
        description: 'Total tous modes confondus',
        animate: true
      },
      {
        id: 'kpi-satisfaction',
        title: 'Satisfaction Moyenne',
        value: 8.0,
        unit: '/10',
        icon: 'sentiment_satisfied',
        trend: 'up',
        trendValue: 0.3,
        trendLabel: 'ce mois',
        variant: 'success',
        description: 'Score moyen pondéré',
        animate: true
      },
      {
        id: 'kpi-retards',
        title: 'Retard Moyen',
        value: 3.0,
        unit: ' min',
        icon: 'schedule',
        trend: 'down',
        trendValue: 0.5,
        trendLabel: 'vs semaine dernière',
        variant: 'warning',
        description: 'Tous modes confondus',
        animate: true
      },
      {
        id: 'kpi-co2',
        title: 'Émissions CO₂',
        value: 57,
        unit: 'g/km/pax',
        icon: 'eco',
        trend: 'down',
        trendValue: 4.2,
        trendLabel: 'vs an dernier',
        variant: 'success',
        description: 'Moyenne pondérée',
        animate: true
      },
      {
        id: 'kpi-occupation',
        title: 'Taux d\'Occupation',
        value: 75.8,
        unit: '%',
        icon: 'event_seat',
        trend: 'up',
        trendValue: 2.1,
        trendLabel: 'ce mois',
        variant: 'info',
        description: 'Capacité utilisée',
        animate: true
      },
      {
        id: 'kpi-incidents',
        title: 'Incidents Actifs',
        value: 7,
        unit: '',
        icon: 'warning',
        trend: 'down',
        trendValue: 3,
        trendLabel: 'vs hier',
        variant: 'danger',
        description: 'Perturbations en cours',
        animate: true
      }
    ];
    return of(kpis).pipe(delay(200));
  }

  // ─── Monthly Passengers Chart ─────────────────────────────────────────────
  getMonthlyPassengers(): Observable<TransportChartData> {
    const labels = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin',
                    'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc'];
    const data: TransportChartData = {
      labels,
      datasets: [
        {
          label: 'Bus',
          data: [128_000, 132_000, 145_000, 139_000, 148_000, 155_000,
                 143_000, 121_000, 150_000, 158_000, 145_000, 142_000],
          color: '#2196f3',
          backgroundColor: 'rgba(33,150,243,0.15)',
          borderColor: '#2196f3',
          fill: true
        },
        {
          label: 'Métro',
          data: [195_000, 201_000, 218_000, 205_000, 222_000, 230_000,
                 210_000, 185_000, 225_000, 235_000, 218_000, 215_000],
          color: '#f44336',
          backgroundColor: 'rgba(244,67,54,0.15)',
          borderColor: '#f44336',
          fill: true
        },
        {
          label: 'Train',
          data: [78_000, 82_000, 90_000, 85_000, 91_000, 95_000,
                 87_000, 72_000, 92_000, 97_000, 88_000, 89_000],
          color: '#9c27b0',
          backgroundColor: 'rgba(156,39,176,0.15)',
          borderColor: '#9c27b0',
          fill: true
        }
      ]
    };
    return of(data).pipe(delay(400));
  }

  // ─── Weekly Passengers (Bar) ──────────────────────────────────────────────
  getWeeklyPassengers(): Observable<TransportChartData> {
    const labels = ['Lundi', 'Mardi', 'Mercredi', 'Jeudi', 'Vendredi', 'Samedi', 'Dimanche'];
    const data: TransportChartData = {
      labels,
      datasets: [
        {
          label: 'Bus',
          data: [145_000, 152_000, 148_000, 155_000, 163_000, 98_000, 72_000],
          backgroundColor: 'rgba(33,150,243,0.85)',
          borderColor: '#2196f3'
        },
        {
          label: 'Métro',
          data: [218_000, 225_000, 220_000, 228_000, 240_000, 145_000, 108_000],
          backgroundColor: 'rgba(244,67,54,0.85)',
          borderColor: '#f44336'
        },
        {
          label: 'Train',
          data: [88_000, 92_000, 87_000, 93_000, 98_000, 62_000, 45_000],
          backgroundColor: 'rgba(156,39,176,0.85)',
          borderColor: '#9c27b0'
        },
        {
          label: 'Taxi',
          data: [28_000, 31_000, 27_000, 32_000, 38_000, 42_000, 35_000],
          backgroundColor: 'rgba(255,193,7,0.85)',
          borderColor: '#ffc107'
        },
        {
          label: 'Vélos',
          data: [32_000, 35_000, 38_000, 36_000, 40_000, 28_000, 22_000],
          backgroundColor: 'rgba(76,175,80,0.85)',
          borderColor: '#4caf50'
        }
      ]
    };
    return of(data).pipe(delay(350));
  }

  // ─── Modal Share by Type (Doughnut) ──────────────────────────────────────
  getModalShare(): Observable<TransportChartData> {
    const data: TransportChartData = {
      labels: ['Bus', 'Métro', 'Train', 'Taxi', 'Vélos'],
      datasets: [{
        label: 'Part modale',
        data: [27.9, 42.3, 17.5, 5.6, 6.7],
        backgroundColor: [
          'rgba(33,150,243,0.85)',
          'rgba(244,67,54,0.85)',
          'rgba(156,39,176,0.85)',
          'rgba(255,193,7,0.85)',
          'rgba(76,175,80,0.85)'
        ],
        borderColor: ['#2196f3', '#f44336', '#9c27b0', '#ffc107', '#4caf50']
      }]
    };
    return of(data).pipe(delay(250));
  }

  // ─── CO2 Emissions (Doughnut) ─────────────────────────────────────────────
  getCO2Emissions(): Observable<TransportChartData> {
    const data: TransportChartData = {
      labels: ['Bus', 'Train', 'Taxi', 'Métro', 'Vélos'],
      datasets: [{
        label: 'Émissions CO₂ (g/km/pax)',
        data: [89, 41, 142, 11, 0],
        backgroundColor: [
          'rgba(33,150,243,0.85)',
          'rgba(156,39,176,0.85)',
          'rgba(255,193,7,0.85)',
          'rgba(244,67,54,0.85)',
          'rgba(76,175,80,0.85)'
        ],
        borderColor: ['#2196f3', '#9c27b0', '#ffc107', '#f44336', '#4caf50']
      }]
    };
    return of(data).pipe(delay(300));
  }

  // ─── Radar: Performance by Transport ─────────────────────────────────────
  getPerformanceRadar(): Observable<TransportChartData> {
    const data: TransportChartData = {
      labels: ['Ponctualité', 'Satisfaction', 'Occupation', 'Écologie', 'Couverture', 'Fréquence'],
      datasets: [
        {
          label: 'Bus',
          data: [72, 72, 68, 45, 88, 80],
          backgroundColor: 'rgba(33,150,243,0.15)',
          borderColor: '#2196f3'
        },
        {
          label: 'Métro',
          data: [93, 79, 82, 89, 75, 95],
          backgroundColor: 'rgba(244,67,54,0.15)',
          borderColor: '#f44336'
        },
        {
          label: 'Train',
          data: [85, 81, 74, 72, 60, 60],
          backgroundColor: 'rgba(156,39,176,0.15)',
          borderColor: '#9c27b0'
        },
        {
          label: 'Vélos',
          data: [100, 91, 100, 100, 55, 70],
          backgroundColor: 'rgba(76,175,80,0.15)',
          borderColor: '#4caf50'
        }
      ]
    };
    return of(data).pipe(delay(300));
  }

  // ─── Delay Trend (Line) ───────────────────────────────────────────────────
  getDelayTrend(): Observable<TransportChartData> {
    const labels = Array.from({ length: 30 }, (_, i) => {
      const d = new Date();
      d.setDate(d.getDate() - (29 - i));
      return d.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit' });
    });
    const data: TransportChartData = {
      labels,
      datasets: [
        {
          label: 'Bus (min)',
          data: Array.from({ length: 30 }, () => +(3.5 + Math.random() * 3).toFixed(1)),
          borderColor: '#2196f3',
          backgroundColor: 'rgba(33,150,243,0.08)',
          fill: true
        },
        {
          label: 'Train (min)',
          data: Array.from({ length: 30 }, () => +(1.5 + Math.random() * 2.5).toFixed(1)),
          borderColor: '#9c27b0',
          backgroundColor: 'rgba(156,39,176,0.08)',
          fill: true
        },
        {
          label: 'Métro (min)',
          data: Array.from({ length: 30 }, () => +(0.5 + Math.random() * 1.5).toFixed(1)),
          borderColor: '#f44336',
          backgroundColor: 'rgba(244,67,54,0.08)',
          fill: true
        }
      ]
    };
    return of(data).pipe(delay(400));
  }

  // ─── Satisfaction Trend ───────────────────────────────────────────────────
  getSatisfactionTrend(): Observable<TransportChartData> {
    const labels = ['Jan', 'Fév', 'Mar', 'Avr', 'Mai', 'Juin',
                    'Juil', 'Août', 'Sep', 'Oct', 'Nov', 'Déc'];
    const data: TransportChartData = {
      labels,
      datasets: [
        {
          label: 'Bus',
          data: [6.8, 6.9, 7.0, 7.1, 7.0, 7.2, 7.1, 7.0, 7.2, 7.3, 7.1, 7.2],
          borderColor: '#2196f3',
          backgroundColor: 'rgba(33,150,243,0.1)',
          fill: false
        },
        {
          label: 'Train',
          data: [7.8, 7.9, 8.0, 8.0, 8.1, 8.0, 7.9, 7.8, 8.0, 8.1, 8.0, 8.1],
          borderColor: '#9c27b0',
          backgroundColor: 'rgba(156,39,176,0.1)',
          fill: false
        },
        {
          label: 'Métro',
          data: [7.5, 7.6, 7.7, 7.8, 7.8, 7.9, 7.8, 7.7, 7.9, 8.0, 7.9, 7.9],
          borderColor: '#f44336',
          backgroundColor: 'rgba(244,67,54,0.1)',
          fill: false
        },
        {
          label: 'Vélos',
          data: [8.8, 8.9, 9.0, 9.0, 9.1, 9.2, 9.2, 9.1, 9.1, 9.0, 9.0, 9.1],
          borderColor: '#4caf50',
          backgroundColor: 'rgba(76,175,80,0.1)',
          fill: false
        }
      ]
    };
    return of(data).pipe(delay(350));
  }

  // ─── Route Performance ────────────────────────────────────────────────────
  getRoutePerformance(): Observable<RoutePerformance[]> {
    const routes: RoutePerformance[] = [
      { route: 'Ligne 1 – Château de Vincennes ↔ La Défense', transport: 'metro', avgPassagers: 45_200, avgOccupancy: 88, punctuality: 96, satisfaction: 8.2, trend: 'up', trendPct: 2.1 },
      { route: 'RER A – Cergy ↔ Marne-la-Vallée', transport: 'train', avgPassagers: 32_800, avgOccupancy: 79, punctuality: 87, satisfaction: 7.8, trend: 'down', trendPct: 0.8 },
      { route: 'Bus 38 – Gare du Nord ↔ Place d\'Italie', transport: 'bus', avgPassagers: 18_400, avgOccupancy: 71, punctuality: 74, satisfaction: 7.1, trend: 'neutral', trendPct: 0.2 },
      { route: 'Ligne 13 – Châtillon ↔ Saint-Denis', transport: 'metro', avgPassagers: 41_600, avgOccupancy: 91, punctuality: 89, satisfaction: 7.5, trend: 'up', trendPct: 1.5 },
      { route: 'Vélib\'+ – Paris Centre', transport: 'velo', avgPassagers: 8_200, avgOccupancy: 100, punctuality: 100, satisfaction: 9.0, trend: 'up', trendPct: 8.3 },
      { route: 'TGV Paris – Lyon', transport: 'train', avgPassagers: 12_400, avgOccupancy: 82, punctuality: 92, satisfaction: 8.5, trend: 'up', trendPct: 1.2 },
      { route: 'Bus 91 – Montrouge ↔ Gare Montparnasse', transport: 'bus', avgPassagers: 14_800, avgOccupancy: 65, punctuality: 68, satisfaction: 6.9, trend: 'down', trendPct: 1.3 }
    ];
    return of(routes).pipe(delay(400));
  }

  // ─── Alerts ───────────────────────────────────────────────────────────────
  getAlerts(): Observable<AlertItem[]> {
    const alerts: AlertItem[] = [
      { id: 'a1', type: 'danger', message: 'Perturbation grave Ligne 13 – travaux d\'urgence entre Châtillon et Montrouge', transport: 'metro', timestamp: new Date(Date.now() - 15 * 60 * 1000), resolved: false },
      { id: 'a2', type: 'warning', message: 'Retards importants RER B – 12 min en moyenne – incident signalé', transport: 'train', timestamp: new Date(Date.now() - 42 * 60 * 1000), resolved: false },
      { id: 'a3', type: 'warning', message: 'Taux d\'occupation Bus 38 > 95% – pic de trafic inhabituel', transport: 'bus', timestamp: new Date(Date.now() - 1.5 * 60 * 60 * 1000), resolved: false },
      { id: 'a4', type: 'info', message: 'Maintenance préventive Ligne 4 – service normal maintenu via déviation', transport: 'metro', timestamp: new Date(Date.now() - 3 * 60 * 60 * 1000), resolved: false },
      { id: 'a5', type: 'success', message: 'Incident Ligne 1 résolu – reprise normale du trafic', transport: 'metro', timestamp: new Date(Date.now() - 4 * 60 * 60 * 1000), resolved: true },
      { id: 'a6', type: 'info', message: 'Nouveau déploiement Vélib\' – 120 vélos supplémentaires secteur 11e', transport: 'velo', timestamp: new Date(Date.now() - 6 * 60 * 60 * 1000), resolved: false }
    ];
    return of(alerts).pipe(delay(200));
  }

  // ─── Daily Stats ──────────────────────────────────────────────────────────
  getDailyStats(): Observable<DailyStats[]> {
    const stats: DailyStats[] = Array.from({ length: 7 }, (_, i) => {
      const d = new Date();
      d.setDate(d.getDate() - (6 - i));
      return {
        date: d.toLocaleDateString('fr-FR', { weekday: 'short', day: '2-digit', month: '2-digit' }),
        totalPassagers: Math.floor(480_000 + Math.random() * 60_000),
        avgDelay: +(2 + Math.random() * 4).toFixed(1),
        avgSatisfaction: +(7.2 + Math.random() * 1.2).toFixed(1),
        co2Total: Math.floor(50 + Math.random() * 15),
        occupancyRate: Math.floor(68 + Math.random() * 15)
      };
    });
    return of(stats).pipe(delay(300));
  }
}
