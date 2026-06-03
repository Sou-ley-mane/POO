import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NavItem } from '../../models/transport.model';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, MatIconModule, MatTooltipModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  @Input() collapsed = false;
  @Output() toggleCollapse = new EventEmitter<boolean>();

  navItems: NavItem[] = [
    { path: '/dashboard', label: 'Tableau de Bord', icon: 'dashboard' },
    { path: '/map',       label: 'Carte en Direct', icon: 'map',       badge: 3 },
    { path: '/reports',   label: 'Rapports',         icon: 'bar_chart' },
    { path: '/settings',  label: 'Paramètres',       icon: 'settings' }
  ];

  transportTypes = [
    { icon: 'directions_bus', label: 'Bus',    color: '#2196f3', active: true  },
    { icon: 'subway',         label: 'Métro',  color: '#f44336', active: true  },
    { icon: 'train',          label: 'Train',  color: '#9c27b0', active: true  },
    { icon: 'local_taxi',     label: 'Taxi',   color: '#ffc107', active: false },
    { icon: 'pedal_bike',     label: 'Vélos',  color: '#4caf50', active: true  }
  ];

  toggle(): void {
    this.toggleCollapse.emit(!this.collapsed);
  }

  toggleTransport(item: { active: boolean }): void {
    item.active = !item.active;
  }
}
