import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { VehiculeDto } from '../models/models';

@Injectable({ providedIn: 'root' })
export class VehiculeService {
  constructor(private readonly http: HttpClient) {}

  lister() {
    return this.http.get<VehiculeDto[]>(`${environment.apiUrl}/vehicules`);
  }

  obtenir(id: number) {
    return this.http.get<VehiculeDto>(`${environment.apiUrl}/vehicules/${id}`);
  }

  creer(request: Partial<VehiculeDto>) {
    return this.http.post<VehiculeDto>(`${environment.apiUrl}/vehicules`, request);
  }

  changerStatut(id: number, statut: string) {
    return this.http.patch<VehiculeDto>(`${environment.apiUrl}/vehicules/${id}/statut?statut=${statut}`, {});
  }
}
