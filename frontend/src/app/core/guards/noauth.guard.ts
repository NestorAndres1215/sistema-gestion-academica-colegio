import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ROLES } from '../constants/roles';


export const NoAuthGuard: CanActivateFn = () => {
  const auth = inject(AuthService);
  const router = inject(Router);

  // si NO está logueado → puede ver login
  if (!auth.isLoggedIn()) {
    return true;
  }

  // si está logueado → lo sacas del login
  return router.parseUrl('/admin'); // o /inicio según tu app
};