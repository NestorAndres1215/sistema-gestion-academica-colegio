import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { firstValueFrom } from 'rxjs';

export const noAuthGuard: CanActivateFn = async () => {

    const authService = inject(AuthService);
    const router = inject(Router);
    const token = authService.token();

    if (!token) return true;


    const user = await firstValueFrom(
        authService.getCurrentUser()
    );

    if (!user) return true;

    const role = user.role;

    if (role) {
        const home = authService.getHomeByRole(role);
        return router.createUrlTree([home]);
    }

    return router.createUrlTree(['/auth/login']);
};