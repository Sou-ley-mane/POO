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
  selector: 'app-bar-chart',
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
export class BarChartComponent implements OnInit, OnChanges, OnDestroy {
  @ViewChild('chartCanvas', { static: true }) canvasRef!: ElementRef<HTMLCanvasElement>;
  @Input({ required: true }) data!: TransportChartData;
  @Input() stacked = false;
  @Input() horizontal = false;
  @Input() showLegend = true;
  @Input() aspectRatio: number | null = null;

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
      type: this.horizontal ? 'bar' : 'bar',
      data: {
        labels: this.data.labels,
        datasets: this.data.datasets.map(ds => ({
          label: ds.label,
          data: ds.data,
          backgroundColor: ds.backgroundColor,
          borderColor: ds.borderColor,
          borderWidth: 1.5,
          borderRadius: 6,
          borderSkipped: false
        }))
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        indexAxis: this.horizontal ? 'y' : 'x',
        plugins: {
          legend: {
            display: this.showLegend,
            position: 'top',
            align: 'end',
            labels: {
              color: textColor,
              font: { family: 'Inter', size: 11, weight: '500' },
              boxWidth: 12,
              boxHeight: 12,
              borderRadius: 3,
              padding: 16
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
                const v = ctx.parsed.y ?? ctx.parsed.x;
                return ` ${ctx.dataset.label}: ${v >= 1000 ? (v / 1000).toFixed(0) + 'k' : v}`;
              }
            }
          }
        },
        scales: {
          x: {
            stacked: this.stacked,
            grid: { color: gridColor, lineWidth: 1 },
            ticks: { color: textColor, font: { family: 'Inter', size: 11 }, maxRotation: 0 },
            border: { display: false }
          },
          y: {
            stacked: this.stacked,
            grid: { color: gridColor, lineWidth: 1 },
            ticks: {
              color: textColor,
              font: { family: 'Inter', size: 11 },
              callback: val => typeof val === 'number' && val >= 1000 ? (val / 1000).toFixed(0) + 'k' : val
            },
            border: { display: false }
          }
        },
        animation: { duration: 800, easing: 'easeInOutQuart' }
      }
    };

    this.chart = new Chart(this.canvasRef.nativeElement, config);
  }
}
