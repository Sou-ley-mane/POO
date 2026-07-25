import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { ChauffeurDto } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ChauffeurService {
  constructor(private readonly http: HttpClient) {}

  lister() {
    return this.http.get<ChauffeurDto[]>(`${environment.apiUrl}/chauffeurs`);
  }

  obtenir(id: number) {
    return this.http.get<ChauffeurDto>(`${environment.apiUrl}/chauffeurs/${id}`);
  }

  creer(request: Partial<ChauffeurDto>) {
    return this.http.post<ChauffeurDto>(`${environment.apiUrl}/chauffeurs`, request);
  }

  affecter(chauffeurId: number, vehiculeId: number) {
    return this.http.post<ChauffeurDto>(`${environment.apiUrl}/chauffeurs/${chauffeurId}/affectation`, { vehiculeId });
  }

  retirerAffectation(chauffeurId: number) {
    return this.http.delete<ChauffeurDto>(`${environment.apiUrl}/chauffeurs/${chauffeurId}/affectation`);
  }

  changerStatut(id: number, statut: string) {
    return this.http.patch<ChauffeurDto>(`${environment.apiUrl}/chauffeurs/${id}/statut?statut=${statut}`, {});
  }
}
