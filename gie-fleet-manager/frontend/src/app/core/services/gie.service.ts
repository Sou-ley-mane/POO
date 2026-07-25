import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { GieDto } from '../models/models';

@Injectable({ providedIn: 'root' })
export class GieService {
  constructor(private readonly http: HttpClient) {}

  monGie() {
    return this.http.get<GieDto>(`${environment.apiUrl}/gie/me`);
  }

  mettreAJourTheme(theme: { couleurPrimaire: string; couleurSecondaire: string; couleurAccent: string; logoUrl?: string }) {
    return this.http.put<GieDto>(`${environment.apiUrl}/gie/theme`, theme);
  }
}
