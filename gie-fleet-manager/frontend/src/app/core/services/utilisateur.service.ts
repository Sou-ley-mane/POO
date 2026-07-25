import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { CreateGestionnaireRequest, UtilisateurDto } from '../models/models';

/** Gestion de l'équipe (gestionnaires) du GIE connecté, réservée à l'ADMIN_GIE. */
@Injectable({ providedIn: 'root' })
export class UtilisateurService {
  constructor(private readonly http: HttpClient) {}

  lister() {
    return this.http.get<UtilisateurDto[]>(`${environment.apiUrl}/utilisateurs`);
  }

  creerGestionnaire(request: CreateGestionnaireRequest) {
    return this.http.post<UtilisateurDto>(`${environment.apiUrl}/utilisateurs`, request);
  }

  changerStatut(id: number, statut: string) {
    return this.http.patch<UtilisateurDto>(`${environment.apiUrl}/utilisateurs/${id}/statut?statut=${statut}`, {});
  }
}
