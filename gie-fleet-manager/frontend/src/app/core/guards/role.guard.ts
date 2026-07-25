import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const roleGuard = (rolesAutorises: string[]): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const role = authService.role();
    if (role && rolesAutorises.includes(role)) {
      return true;
    }
    return router.parseUrl('/tableau-de-bord');
  };
};
