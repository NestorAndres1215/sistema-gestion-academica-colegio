import { inject } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    CanActivateFn,
    Router
} from '@angular/router';

import { AuthService } from '../services/auth.service';


export const roleGuard: CanActivateFn = (route) => {
    const auth = inject(AuthService);
    const router = inject(Router);

    if (!auth.isLoggedIn()) {
        console.log("aaaaa")
        return router.parseUrl('/auth/login');
    }

    const user = auth.getUser();
    if (!user) {
        return router.parseUrl('/auth/login');
    }

    const role = auth.getUserRole();
    if (!role) {
        return router.parseUrl('/auth/login');
    }

    const allowedRoles: string[] = route.data?.['roles'] ?? [];

    if (!allowedRoles.includes(role)) {
        return router.parseUrl('/auth/login');
    }

    return true;
};