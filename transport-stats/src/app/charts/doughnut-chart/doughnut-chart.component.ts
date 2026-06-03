import {
  Component, Input, OnInit, OnChanges, SimpleChanges,
  ViewChild, ElementRef, OnDestroy, inject
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chart, ChartConfiguration, registerables } from 'chart.js';
import { TransportChartData } from '../../models/transport.model';
import { TransportService } from '../../services/transport.service';
import { Subscription } from 'rxjs';

Chart.register(...registerables);

@Component({
  selector: 'app-doughnut-chart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="chart-wrapper">
      <canvas #chartCanvas></canvas>
      <!-- Center label -->
      <div class="chart-center" *ngIf="centerLabel">
        <span class="center-value">{{ centerValue }}</span>
        <span class="center-label">{{ centerLabel }}</span>
      </div>
    </div>
  `,
  styles: [`
    .chart-wrapper {
      position: relative;
      width: 100%;
      height: 100%;
      min-height: 260px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    canvas { display: block; max-height: 280px; }
    .chart-center {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      text-align: center;
      pointer-events: none;
    }
    .center-value {
      display: block;
      font-size: 1.6rem;
      font-weight: 800;
      color: var(--text-primary);
      line-height: 1;
    }
    .center-label {
      display: block;
      font-size: 0.7rem;
      font-weight: 600;
      color: var(--text-muted);
      text-transform: uppercase;
      letter-spacing: 0.06em;
      margin-top: 4px;
    }
  `]
})
export class DoughnutChartComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild('chartCanvas', { static: true }) canvasRef!: ElementRef<HTMLCanvasElement>;
  @Input({ required: true }) data!: TransportChartData;
  @Input() centerLabel = '';
  @Input() centerValue = '';
  @Input() showLegend = true;
  @Input() cutout = '72%';

  private chart: Chart | null = null;
  private transportService = inject(TransportService);
  private sub = new Subscription();

  ngOnInit(): void {
    this.buildChart();
    this.sub.add(
      this.transportService.isDark$.subscribe(() => {
        this.chart?.destroy();
        this.buildChart();
      })
    );
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['data'] && !changes['data'].firstChange) {
      this.chart?.destroy();
      this.buildChart();
    }
  }

  ngOnDestroy(): void {
    this.chart?.destroy();
    this.sub.unsubscribe();
  }

  private buildChart(): void {
    const isDark = this.transportService.theme().isDark;
    const textColor = isDark ? 'rgba(232,237,245,0.7)' : 'rgba(26,31,54,0.65)';

    const config: ChartConfiguration = {
      type: 'doughnut',
      data: {
        labels: this.data.labels,
        datasets: this.data.datasets.map(ds => ({
          label: ds.label,
          data: ds.data,
          backgroundColor: ds.backgroundColor,
          borderColor: isDark ? '#111827' : '#ffffff',
          borderWidth: 3,
          hoverBorderWidth: 4,
          hoverOffset: 8
        }))
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        cutout: this.cutout,
        plugins: {
          legend: {
            display: this.showLegend,
            position: 'right',
            labels: {
              color: textColor,
              font: { family: 'Inter', size: 11, weight: '500' },
              boxWidth: 12,
              boxHeight: 12,
              borderRadius: 3,
              padding: 12,
              generateLabels: chart => {
                const data = chart.data;
                if (!data.labels || !data.datasets.length) return [];
                const ds = data.datasets[0];
                const total = (ds.data as number[]).reduce((a, b) => a + (b as number), 0);
                return (data.labels as string[]).map((label, i) => ({
                  text: `${label} (${((ds.data[i] as number) / total * 100).toFixed(1)}%)`,
                  fillStyle: (ds.backgroundColor as string[])[i],
                  strokeStyle: (ds.backgroundColor as string[])[i],
                  fontColor: textColor,
                  index: i,
                  hidden: false,
                  lineWidth: 0
                }));
              }
            }
          },
          tooltip: {
            backgroundColor: isDark ? '#1e2d42' : 'rgba(10,15,30,0.92)',
            titleColor: '#fff',
            bodyColor: 'rgba(255,255,255,0.8)',
            padding: 12,
            cornerRadius: 8,
            titleFont: { family: 'Inter', size: 12, weight: '600' },
            bodyFont: { family: 'Inter', size: 11 },
            callbacks: {
              label: ctx => {
                const v = ctx.parsed;
                const total = (ctx.dataset.data as number[]).reduce((a, b) => a + b, 0);
                const pct = (v / total * 100).toFixed(1);
                return ` ${ctx.label}: ${v}% (${pct}% du total)`;
              }
            }
          }
        },
        animation: { duration: 1000, easing: 'easeInOutQuart' }
      }
    };

    this.chart = new Chart(this.canvasRef.nativeElement, config);
  }
}
