import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { SaisieVersementRequest, VersementGrilleLigneDto } from '../models/models';

@Injectable({ providedIn: 'root' })
export class VersementService {
  constructor(private readonly http: HttpClient) {}

  grille() {
    return this.http.get<VersementGrilleLigneDto[]>(`${environment.apiUrl}/versements/grille`);
  }

  saisir(request: SaisieVersementRequest) {
    return this.http.post(`${environment.apiUrl}/versements`, request);
  }

  historiqueParChauffeur(chauffeurId: number) {
    return this.http.get(`${environment.apiUrl}/versements/chauffeur/${chauffeurId}`);
  }
}
