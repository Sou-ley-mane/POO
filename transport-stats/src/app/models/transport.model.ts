// ─── Transport Types ──────────────────────────────────────────────────────────

export type TransportType = 'bus' | 'train' | 'taxi' | 'metro' | 'velo';

export type TrendDirection = 'up' | 'down' | 'neutral';

export type KpiVariant = 'primary' | 'success' | 'warning' | 'danger' | 'info';

// ─── Core Interfaces ─────────────────────────────────────────────────────────

export interface TransportStats {
  id: string;
  type: TransportType;
  label: string;
  icon: string;
  color: string;
  passagers: number;
  vitesseMoyenne: number;   // km/h
  retards: number;          // minutes de retard moyen
  tauxOccupation: number;   // %
  emissionsCO2: number;     // g/km par passager
  satisfaction: number;     // 0-10
  timestamp: Date;
}

export interface KpiCard {
  id: string;
  title: string;
  value: number | string;
  unit?: string;
  icon: string;
  trend: TrendDirection;
  trendValue: number;
  trendLabel: string;
  variant: KpiVariant;
  description?: string;
  animate?: boolean;
}

export interface TimeSeriesPoint {
  label: string;
  value: number;
}

export interface ChartDataset {
  label: string;
  data: number[];
  color?: string;
  backgroundColor?: string | string[];
  borderColor?: string | string[];
  fill?: boolean;
}

export interface TransportChartData {
  labels: string[];
  datasets: ChartDataset[];
}

export interface DailyStats {
  date: string;
  totalPassagers: number;
  avgDelay: number;
  avgSatisfaction: number;
  co2Total: number;
  occupancyRate: number;
}

export interface MonthlyStats {
  month: string;
  bus: number;
  train: number;
  taxi: number;
  metro: number;
  velo: number;
}

export interface DelayStats {
  transport: string;
  onTime: number;
  minor: number;   // < 5 min
  moderate: number; // 5-15 min
  major: number;    // > 15 min
}

export interface SatisfactionScore {
  category: string;
  score: number;
  responses: number;
}

export interface RoutePerformance {
  route: string;
  transport: TransportType;
  avgPassagers: number;
  avgOccupancy: number;
  punctuality: number;
  satisfaction: number;
  trend: TrendDirection;
  trendPct: number;
}

export interface AlertItem {
  id: string;
  type: 'warning' | 'danger' | 'info' | 'success';
  message: string;
  transport: TransportType;
  timestamp: Date;
  resolved: boolean;
}

export interface NavItem {
  path: string;
  label: string;
  icon: string;
  badge?: number;
}

export interface ThemeConfig {
  isDark: boolean;
  primaryColor: string;
  accentColor: string;
}
