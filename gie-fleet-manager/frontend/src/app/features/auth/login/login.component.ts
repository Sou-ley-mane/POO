import { CommonModule } from '@angular/common';
import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  telephone = '';
  pin = '';
  enCours = signal(false);
  erreur = signal<string | null>(null);

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  seConnecter(): void {
    this.erreur.set(null);
    this.enCours.set(true);
    this.authService.login({ telephone: this.telephone, pin: this.pin }).subscribe({
      next: () => {
        this.enCours.set(false);
        this.router.navigate(['/tableau-de-bord']);
      },
      error: (err) => {
        this.enCours.set(false);
        this.erreur.set(err?.error?.message ?? 'Téléphone ou PIN incorrect');
      }
    });
  }
}
