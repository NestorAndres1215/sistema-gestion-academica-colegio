import { inject } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    CanActivateFn,
    Router
} from '@angular/router';

import { AuthService } from '../services/auth.service';
import { ROLES } from '../constants/roles';

export const roleGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot
) => {

    const authService = inject(AuthService);
    const router = inject(Router);

    const token = localStorage.getItem('jwt');

    // No hay token → login
    if (!token) {
        console.log('No token → login');
        return router.parseUrl('/auth/login');
    }

    const userRole = authService.getUserRole();

    const allowedRoles: string[] = route.data?.['roles'] ?? [];

    console.log('USER ROLE:', userRole);
    console.log('ALLOWED ROLES:', allowedRoles);

    if (allowedRoles.includes(userRole)) {
        console.log('Acceso permitido');
        return true;
    }

    const rutasPorRol: Record<string, string> = {
        [ROLES.ROLE_ADMINISTRATOR]: '/admin',
        [ROLES.ROLE_STUDENT]: '/inicio',
        [ROLES.ROLE_STAFF]: '/staff',
        [ROLES.ROLE_TEACHER]: '/teacher',
        [ROLES.ROLE_GUARDIAN]: '/guardian'
    };

    const ruta = rutasPorRol[userRole] ?? '/auth/login';

    console.log('Redirigiendo a:', ruta);

    return router.parseUrl(ruta);
};