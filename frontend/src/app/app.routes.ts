import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';
import { ROLES } from './core/constants/roles';
import { AdminHome } from './features/admin/admin-home/admin-home';

export const routes: Routes = [
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/routes/auth.routes')
        .then(m => m.AUTHENTICATION_ROUTES)
  },
  {
    path: '',
    component: AdminHome,
    canActivate: [roleGuard],
    data: { roles: [ROLES.ROLE_ADMINISTRATOR] },
    loadChildren: () => import('./features/routes/admin.routes').then(m => m.ADMIN_ROUTES)
  },
];