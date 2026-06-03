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
  selector: 'app-line-chart',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="chart-wrapper">
      <canvas #chartCanvas></canvas>
    </div>
  `,
  styles: [`
    .chart-wrapper {
      position: relative;
      width: 100%;
      height: 100%;
      min-height: 260px;
    }
    canvas { display: block; }
  `]
})
export class LineChartComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild('chartCanvas', { static: true }) canvasRef!: ElementRef<HTMLCanvasElement>;
  @Input({ required: true }) data!: TransportChartData;
  @Input() smooth = true;
  @Input() showPoints = false;
  @Input() showLegend = true;
  @Input() fill = false;

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
    const gridColor = isDark ? 'rgba(255,255,255,0.06)' : 'rgba(0,0,0,0.06)';

    const config: ChartConfiguration = {
      type: 'line',
      data: {
        labels: this.data.labels,
        datasets: this.data.datasets.map(ds => ({
          label: ds.label,
          data: ds.data,
          borderColor: ds.borderColor,
          backgroundColor: ds.backgroundColor ?? (ds.borderColor as string | undefined),
          fill: ds.fill ?? this.fill,
          tension: this.smooth ? 0.4 : 0,
          pointRadius: this.showPoints ? 4 : 0,
          pointHoverRadius: 5,
          pointBackgroundColor: ds.borderColor,
          borderWidth: 2.5
        }))
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: { mode: 'index', intersect: false },
        plugins: {
          legend: {
            display: this.showLegend,
            position: 'top',
            align: 'end',
            labels: {
              color: textColor,
              font: { family: 'Inter', size: 11, weight: '500' },
              boxWidth: 24,
              boxHeight: 3,
              borderRadius: 2,
              padding: 16,
              usePointStyle: true,
              pointStyle: 'line'
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
                const v = ctx.parsed.y;
                return ` ${ctx.dataset.label}: ${v >= 1000 ? (v / 1000).toFixed(0) + 'k' : v}`;
              }
            }
          }
        },
        scales: {
          x: {
            grid: { display: false },
            ticks: {
              color: textColor,
              font: { family: 'Inter', size: 10 },
              maxTicksLimit: 12,
              maxRotation: 0
            },
            border: { display: false }
          },
          y: {
            grid: { color: gridColor, lineWidth: 1 },
            ticks: {
              color: textColor,
              font: { family: 'Inter', size: 11 },
              callback: val => typeof val === 'number' && val >= 1000 ? (val / 1000).toFixed(0) + 'k' : val
            },
            border: { display: false }
          }
        },
        animation: { duration: 1000, easing: 'easeInOutQuart' }
      }
    };

    this.chart = new Chart(this.canvasRef.nativeElement, config);
  }
}
