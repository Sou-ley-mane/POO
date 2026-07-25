import { HttpClient } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { Router } from '@angular/router';
import { tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { LoginRequest, LoginResponse } from '../models/models';

const STORAGE_KEY = 'gie_fleet_session';

interface Session {
  token: string;
  userId: number;
  gieId: number | null;
  nom: string;
  prenom: string;
  role: LoginResponse['role'];
  doitChangerPin: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly sessionSignal = signal<Session | null>(this.readStoredSession());

  readonly session = this.sessionSignal.asReadonly();
  readonly isAuthenticated = computed(() => this.sessionSignal() !== null);
  readonly role = computed(() => this.sessionSignal()?.role ?? null);

  constructor(
    private readonly http: HttpClient,
    private readonly router: Router
  ) {}

  login(request: LoginRequest) {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request).pipe(
      tap((response) => this.startSession(response))
    );
  }

  changerPin(ancienPin: string, nouveauPin: string) {
    return this.http.post<void>(`${environment.apiUrl}/auth/changer-pin`, { ancienPin, nouveauPin });
  }

  logout(): void {
    localStorage.removeItem(STORAGE_KEY);
    this.sessionSignal.set(null);
    this.router.navigate(['/connexion']);
  }

  getToken(): string | null {
    return this.sessionSignal()?.token ?? null;
  }

  private startSession(response: LoginResponse): void {
    const session: Session = {
      token: response.token,
      userId: response.userId,
      gieId: response.gieId,
      nom: response.nom,
      prenom: response.prenom,
      role: response.role,
      doitChangerPin: response.doitChangerPin
    };
    localStorage.setItem(STORAGE_KEY, JSON.stringify(session));
    this.sessionSignal.set(session);
  }

  private readStoredSession(): Session | null {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? (JSON.parse(raw) as Session) : null;
  }
}
