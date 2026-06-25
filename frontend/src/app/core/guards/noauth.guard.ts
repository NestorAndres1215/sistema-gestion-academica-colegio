import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { ROLES } from '../constants/roles';

export const NoAuthGuard: CanActivateFn = () => {

    const authService = inject(AuthService);
    const router = inject(Router);

    if (!authService.isLoggedIn()) {
        return true;
    }

    const role = authService.getUserRole();

    const rutasPorRol: Record<string, string> = {
        [ROLES.ROLE_ADMINISTRATOR]: '/admin',
        [ROLES.ROLE_STUDENT]: '/inicio',
        [ROLES.ROLE_STAFF]: '/staff',
        [ROLES.ROLE_TEACHER]: '/teacher',
        [ROLES.ROLE_GUARDIAN]: '/guardian'
    };

    return router.parseUrl(rutasPorRol[role] ?? '/auth/login');
};