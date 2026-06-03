import { Component, Input, Output, EventEmitter, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule, RouterLink,
    MatIconModule, MatButtonModule, MatTooltipModule, MatMenuModule, MatBadgeModule, MatDividerModule
  ],
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent {
  @Input() isDark = false;
  @Input() sidebarCollapsed = false;
  @Output() themeToggle = new EventEmitter<void>();
  @Output() menuToggle = new EventEmitter<void>();

  currentTime = new Date();
  notificationCount = 3;

  notifications = [
    { icon: 'warning', color: '#f44336', text: 'Perturbation Ligne 13', time: '15 min' },
    { icon: 'schedule', color: '#ff9800', text: 'Retards RER B — 12 min', time: '42 min' },
    { icon: 'info',     color: '#2196f3', text: 'Maintenance Ligne 4', time: '3h' }
  ];

  constructor() {
    setInterval(() => this.currentTime = new Date(), 30000);
  }
}
