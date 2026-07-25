import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { DashboardStatsDto } from '../models/models';

@Injectable({ providedIn: 'root' })
export class DashboardService {
  constructor(private readonly http: HttpClient) {}

  obtenirStats() {
    return this.http.get<DashboardStatsDto>(`${environment.apiUrl}/dashboard`);
  }
}
