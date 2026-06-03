import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-map',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule],
  template: `
    <div class="page-container">
      <div class="page-header">
        <h2>Carte en Direct</h2>
        <p>Visualisation géographique du réseau de transport en temps réel</p>
      </div>

      <div class="map-placeholder">
        <div class="map-overlay">
          <!-- Fake map grid -->
          <svg class="map-grid" viewBox="0 0 800 500" preserveAspectRatio="xMidYMid slice">
            <!-- Background -->
            <rect width="800" height="500" fill="var(--map-bg, #0d1b2a)"/>

            <!-- Grid lines -->
            <g stroke="rgba(26,115,232,0.08)" stroke-width="1">
              <line x1="0" y1="100" x2="800" y2="100"/>
              <line x1="0" y1="200" x2="800" y2="200"/>
              <line x1="0" y1="300" x2="800" y2="300"/>
              <line x1="0" y1="400" x2="800" y2="400"/>
              <line x1="100" y1="0" x2="100" y2="500"/>
              <line x1="200" y1="0" x2="200" y2="500"/>
              <line x1="300" y1="0" x2="300" y2="500"/>
              <line x1="400" y1="0" x2="400" y2="500"/>
              <line x1="500" y1="0" x2="500" y2="500"/>
              <line x1="600" y1="0" x2="600" y2="500"/>
              <line x1="700" y1="0" x2="700" y2="500"/>
            </g>

            <!-- Fake metro lines -->
            <g fill="none" stroke-width="3" stroke-linecap="round">
              <path d="M100,400 Q200,350 300,300 T500,250 T700,200" stroke="#f44336" opacity="0.8"/>
              <path d="M50,200 Q200,180 400,200 T750,220" stroke="#2196f3" opacity="0.8"/>
              <path d="M150,50 Q200,150 250,250 T300,420" stroke="#9c27b0" opacity="0.8"/>
              <path d="M400,50 Q450,200 500,300 T550,450" stroke="#4caf50" opacity="0.8"/>
            </g>

            <!-- Station dots -->
            <g>
              <circle cx="300" cy="300" r="7" fill="#f44336" opacity="0.9"/>
              <circle cx="400" cy="200" r="7" fill="#2196f3" opacity="0.9"/>
              <circle cx="500" cy="250" r="7" fill="#f44336" opacity="0.9"/>
              <circle cx="250" cy="180" r="5" fill="#2196f3" opacity="0.7"/>
              <circle cx="600" cy="220" r="5" fill="#2196f3" opacity="0.7"/>
              <circle cx="350" cy="350" r="5" fill="#9c27b0" opacity="0.7"/>
              <!-- Animated pulse -->
              <circle cx="300" cy="300" r="14" fill="none" stroke="#f44336" stroke-width="2" opacity="0.4">
                <animate attributeName="r" values="8;20;8" dur="2s" repeatCount="indefinite"/>
                <animate attributeName="opacity" values="0.6;0;0.6" dur="2s" repeatCount="indefinite"/>
              </circle>
              <circle cx="400" cy="200" r="14" fill="none" stroke="#2196f3" stroke-width="2" opacity="0.4">
                <animate attributeName="r" values="8;20;8" dur="2.5s" repeatCount="indefinite"/>
                <animate attributeName="opacity" values="0.6;0;0.6" dur="2.5s" repeatCount="indefinite"/>
              </circle>
            </g>

            <!-- Labels -->
            <g fill="rgba(255,255,255,0.5)" font-family="Inter, sans-serif" font-size="11">
              <text x="290" y="285">Châtelet</text>
              <text x="390" y="185">Gare du Nord</text>
              <text x="490" y="235">République</text>
            </g>
          </svg>

          <!-- Map controls overlay -->
          <div class="map-controls">
            <button mat-mini-fab color="primary" matTooltip="Zoom +">
              <mat-icon>add</mat-icon>
            </button>
            <button mat-mini-fab color="primary" matTooltip="Zoom -">
              <mat-icon>remove</mat-icon>
            </button>
            <button mat-mini-fab color="primary" matTooltip="Centrer">
              <mat-icon>my_location</mat-icon>
            </button>
          </div>

          <!-- Legend -->
          <div class="map-legend">
            <h4>Légende</h4>
            <div class="legend-item"><span class="leg-dot" style="background:#f44336"></span>Métro</div>
            <div class="legend-item"><span class="leg-dot" style="background:#2196f3"></span>Bus RER</div>
            <div class="legend-item"><span class="leg-dot" style="background:#9c27b0"></span>Train</div>
            <div class="legend-item"><span class="leg-dot" style="background:#4caf50"></span>Vélos</div>
            <div class="legend-item">
              <span class="leg-pulse"></span>Incident actif
            </div>
          </div>

          <!-- Coming soon banner -->
          <div class="coming-soon">
            <mat-icon>map</mat-icon>
            <h3>Carte Interactive</h3>
            <p>Intégration Leaflet/Mapbox en cours de développement</p>
            <p class="sub">Les données de géolocalisation temps réel seront affichées ici</p>
          </div>
        </div>
      </div>

      <!-- Stats below map -->
      <div class="map-stats">
        <div class="map-stat-card" *ngFor="let s of mapStats">
          <mat-icon [style.color]="s.color">{{ s.icon }}</mat-icon>
          <div>
            <span class="stat-value">{{ s.value }}</span>
            <span class="stat-label">{{ s.label }}</span>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .map-placeholder {
      height: 500px;
      border-radius: var(--border-radius);
      overflow: hidden;
      border: 1px solid var(--border-color);
      position: relative;
      margin-bottom: 24px;
      background: #0d1b2a;
    }
    .map-overlay { position: relative; width: 100%; height: 100%; }
    .map-grid { width: 100%; height: 100%; display: block; }
    .map-controls {
      position: absolute;
      right: 16px;
      top: 16px;
      display: flex;
      flex-direction: column;
      gap: 8px;
    }
    .map-legend {
      position: absolute;
      left: 16px;
      bottom: 16px;
      background: rgba(8,13,26,0.85);
      backdrop-filter: blur(8px);
      border: 1px solid rgba(255,255,255,0.1);
      border-radius: 10px;
      padding: 12px 16px;
      color: rgba(255,255,255,0.8);
      font-size: 0.8rem;
      h4 { color: white; font-size: 0.75rem; text-transform: uppercase; letter-spacing: 0.08em; margin-bottom: 8px; }
    }
    .legend-item { display: flex; align-items: center; gap: 8px; margin-bottom: 5px; }
    .leg-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
    .leg-pulse {
      width: 10px; height: 10px; border-radius: 50%;
      background: rgba(244,67,54,0.6); flex-shrink: 0;
      box-shadow: 0 0 8px rgba(244,67,54,0.8);
      animation: pulse 2s infinite;
    }
    .coming-soon {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      text-align: center;
      color: white;
      background: rgba(8,13,26,0.7);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(255,255,255,0.1);
      border-radius: 16px;
      padding: 32px 48px;
      mat-icon { font-size: 48px; width: 48px; height: 48px; color: var(--color-primary); margin-bottom: 12px; }
      h3 { color: white; font-size: 1.3rem; margin-bottom: 8px; }
      p { color: rgba(255,255,255,0.6); font-size: 0.85rem; margin: 0; }
      .sub { font-size: 0.75rem; margin-top: 6px; }
    }
    .map-stats {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
      gap: 16px;
    }
    .map-stat-card {
      background: var(--bg-card);
      border: 1px solid var(--border-color);
      border-radius: var(--border-radius);
      padding: 16px;
      display: flex;
      align-items: center;
      gap: 12px;
      box-shadow: var(--shadow-card);
      mat-icon { font-size: 28px; width: 28px; height: 28px; }
      div { display: flex; flex-direction: column; }
      .stat-value { font-size: 1.2rem; font-weight: 700; color: var(--text-primary); }
      .stat-label { font-size: 0.75rem; color: var(--text-muted); }
    }
  `]
})
export class MapComponent {
  mapStats = [
    { icon: 'directions_bus', color: '#2196f3', value: '1 842',  label: 'Véhicules actifs' },
    { icon: 'subway',         color: '#f44336', value: '14',     label: 'Lignes en service' },
    { icon: 'warning',        color: '#ff9800', value: '3',      label: 'Incidents signalés' },
    { icon: 'people',         color: '#4caf50', value: '28 420', label: 'Passagers en transit' },
    { icon: 'speed',          color: '#9c27b0', value: '89%',    label: 'Ponctualité globale' }
  ];
}
