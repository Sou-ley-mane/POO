import {
  Component, Input, OnInit, OnChanges, SimpleChanges,
  ChangeDetectionStrategy, signal, computed
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { trigger, state, style, animate, transition } from '@angular/animations';
import { KpiCard } from '../../models/transport.model';

@Component({
  selector: 'app-kpi-card',
  standalone: true,
  imports: [CommonModule, MatIconModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('cardEnter', [
      transition(':enter', [
        style({ opacity: 0, transform: 'translateY(20px)' }),
        animate('500ms cubic-bezier(0.35, 0, 0.25, 1)',
          style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ]),
    trigger('valueChange', [
      transition('* => *', [
        style({ opacity: 0, transform: 'translateY(8px)' }),
        animate('300ms ease', style({ opacity: 1, transform: 'translateY(0)' }))
      ])
    ])
  ],
  template: `
    <div class="kpi-card" [@cardEnter] [class]="'kpi-card--' + kpi.variant">
      <!-- Icon area -->
      <div class="kpi-icon-wrap">
        <mat-icon>{{ kpi.icon }}</mat-icon>
        <div class="kpi-icon-glow"></div>
      </div>

      <!-- Content -->
      <div class="kpi-body">
        <p class="kpi-title">{{ kpi.title }}</p>
        <div class="kpi-value-row">
          <span class="kpi-value" [@valueChange]="displayValue()">
            {{ formatValue(kpi.value) }}
          </span>
          <span class="kpi-unit" *ngIf="kpi.unit">{{ kpi.unit }}</span>
        </div>
        <p class="kpi-description" *ngIf="kpi.description">{{ kpi.description }}</p>
      </div>

      <!-- Trend badge -->
      <div class="kpi-trend" [class]="trendClass()">
        <mat-icon class="trend-icon">{{ trendIcon() }}</mat-icon>
        <span class="trend-value">{{ kpi.trendValue }}%</span>
        <span class="trend-label">{{ kpi.trendLabel }}</span>
      </div>

      <!-- Decorative bar -->
      <div class="kpi-bar"></div>
    </div>
  `,
  styles: [`
    .kpi-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--border-radius);
      padding: 20px;
      display: flex;
      flex-direction: column;
      gap: 12px;
      position: relative;
      overflow: hidden;
      cursor: default;
      transition: box-shadow var(--transition-base), transform var(--transition-base);
      box-shadow: var(--shadow-card);

      &:hover {
        transform: translateY(-2px);
        box-shadow: var(--shadow-md);
      }
    }

    .kpi-bar {
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 3px;
      border-radius: 0 0 var(--border-radius) var(--border-radius);
    }

    .kpi-card--primary   .kpi-bar { background: var(--color-primary); }
    .kpi-card--success   .kpi-bar { background: var(--color-accent); }
    .kpi-card--warning   .kpi-bar { background: var(--color-warning); }
    .kpi-card--danger    .kpi-bar { background: var(--color-warn); }
    .kpi-card--info      .kpi-bar { background: #00bcd4; }

    .kpi-card--primary   .kpi-icon-wrap { background: rgba(26,115,232,0.12); color: var(--color-primary); }
    .kpi-card--success   .kpi-icon-wrap { background: rgba(0,200,150,0.12); color: var(--color-accent); }
    .kpi-card--warning   .kpi-icon-wrap { background: rgba(255,152,0,0.12); color: var(--color-warning); }
    .kpi-card--danger    .kpi-icon-wrap { background: rgba(244,67,54,0.12); color: var(--color-warn); }
    .kpi-card--info      .kpi-icon-wrap { background: rgba(0,188,212,0.12); color: #00bcd4; }

    .kpi-card--primary   .kpi-icon-glow { background: radial-gradient(circle, rgba(26,115,232,0.2), transparent 70%); }
    .kpi-card--success   .kpi-icon-glow { background: radial-gradient(circle, rgba(0,200,150,0.2), transparent 70%); }
    .kpi-card--warning   .kpi-icon-glow { background: radial-gradient(circle, rgba(255,152,0,0.2), transparent 70%); }
    .kpi-card--danger    .kpi-icon-glow { background: radial-gradient(circle, rgba(244,67,54,0.2), transparent 70%); }
    .kpi-card--info      .kpi-icon-glow { background: radial-gradient(circle, rgba(0,188,212,0.2), transparent 70%); }

    .kpi-icon-wrap {
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;

      mat-icon { font-size: 24px; width: 24px; height: 24px; }
    }

    .kpi-icon-glow {
      position: absolute;
      inset: -10px;
      border-radius: 50%;
      pointer-events: none;
    }

    .kpi-body { flex: 1; }

    .kpi-title {
      font-size: 0.78rem;
      font-weight: 600;
      letter-spacing: 0.03em;
      color: var(--text-secondary);
      text-transform: uppercase;
      margin: 0 0 6px;
    }

    .kpi-value-row {
      display: flex;
      align-items: baseline;
      gap: 4px;
    }

    .kpi-value {
      font-size: 1.9rem;
      font-weight: 800;
      color: var(--text-primary);
      line-height: 1;
      letter-spacing: -0.02em;
      font-variant-numeric: tabular-nums;
    }

    .kpi-unit {
      font-size: 0.85rem;
      font-weight: 500;
      color: var(--text-secondary);
    }

    .kpi-description {
      font-size: 0.75rem;
      color: var(--text-muted);
      margin: 4px 0 0;
    }

    .kpi-trend {
      display: flex;
      align-items: center;
      gap: 4px;
      padding: 5px 10px;
      border-radius: 20px;
      font-size: 0.75rem;
      font-weight: 600;
      width: fit-content;
      align-self: flex-start;
    }

    .trend-icon { font-size: 14px !important; width: 14px !important; height: 14px !important; }
    .trend-label { color: var(--text-muted); font-weight: 400; margin-left: 2px; }

    .trend-up {
      background: rgba(0,200,150,0.1);
      color: #00a87a;
    }
    .trend-down-good {
      background: rgba(0,200,150,0.1);
      color: #00a87a;
    }
    .trend-down-bad {
      background: rgba(244,67,54,0.1);
      color: #d32f2f;
    }
    .trend-neutral {
      background: rgba(90,100,115,0.1);
      color: var(--text-secondary);
    }
  `]
})
export class KpiCardComponent implements OnChanges {
  @Input({ required: true }) kpi!: KpiCard;
  @Input() isGoodWhenDown = false;

  displayValue = signal('');

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['kpi']) {
      this.displayValue.set(this.formatValue(this.kpi.value));
    }
  }

  formatValue(val: number | string): string {
    if (typeof val === 'number') {
      if (val >= 1_000_000) return (val / 1_000_000).toFixed(1) + 'M';
      if (val >= 1_000) return (val / 1_000).toFixed(0) + 'k';
      return val % 1 !== 0 ? val.toFixed(1) : val.toString();
    }
    return val;
  }

  trendClass(): string {
    const { trend, id } = this.kpi;
    const goodWhenDown = ['kpi-retards', 'kpi-co2', 'kpi-incidents'].includes(id);
    if (trend === 'neutral') return 'kpi-trend trend-neutral';
    if (trend === 'up') return goodWhenDown ? 'kpi-trend trend-down-bad' : 'kpi-trend trend-up';
    // down
    return goodWhenDown ? 'kpi-trend trend-down-good' : 'kpi-trend trend-down-bad';
  }

  trendIcon(): string {
    if (this.kpi.trend === 'up') return 'trending_up';
    if (this.kpi.trend === 'down') return 'trending_down';
    return 'trending_flat';
  }
}
