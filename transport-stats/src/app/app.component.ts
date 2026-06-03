import { Component, OnInit, OnDestroy, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { TransportService } from './services/transport.service';
import { SidebarComponent } from './shared/sidebar/sidebar.component';
import { NavbarComponent } from './shared/navbar/navbar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, SidebarComponent, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit, OnDestroy {
  private transportService = inject(TransportService);

  sidebarCollapsed = signal(false);
  isDark = signal(false);

  private sub = new Subscription();

  ngOnInit(): void {
    this.sub.add(
      this.transportService.isDark$.subscribe(dark => this.isDark.set(dark))
    );
  }

  ngOnDestroy(): void {
    this.sub.unsubscribe();
  }

  onSidebarToggle(collapsed: boolean): void {
    this.sidebarCollapsed.set(collapsed);
  }

  onThemeToggle(): void {
    this.transportService.toggleTheme();
  }
}
