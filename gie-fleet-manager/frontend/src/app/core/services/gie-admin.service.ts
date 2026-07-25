import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { CreateGieRequest, GieDto } from '../models/models';

/** Endpoints réservés au SUPER_ADMIN pour administrer l'ensemble des GIE de la plateforme. */
@Injectable({ providedIn: 'root' })
export class GieAdminService {
  constructor(private readonly http: HttpClient) {}

  listerTous() {
    return this.http.get<GieDto[]>(`${environment.apiUrl}/super-admin/gie`);
  }

  creer(request: CreateGieRequest) {
    return this.http.post<GieDto>(`${environment.apiUrl}/super-admin/gie`, request);
  }

  suspendre(gieId: number) {
    return this.http.post<void>(`${environment.apiUrl}/super-admin/gie/${gieId}/suspendre`, {});
  }

  reactiver(gieId: number) {
    return this.http.post<void>(`${environment.apiUrl}/super-admin/gie/${gieId}/reactiver`, {});
  }
}
