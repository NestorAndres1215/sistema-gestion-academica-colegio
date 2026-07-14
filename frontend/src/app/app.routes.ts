import { Routes } from '@angular/router';
import { roleGuard } from './core/guards/role.guard';
import { ROLES } from './core/constants/roles';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'auth/login',
    pathMatch: 'full'
  },

  // 🔐 AUTH
  {
    path: 'auth',
    loadChildren: () =>
      import('./features/auth/auth.routes')
        .then(m => m.AUTHENTICATION_ROUTES)
  },

  // 🧱 LAYOUT (AQUÍ VA TODO LO LOGUEADO)
  {
    path: '',
    loadComponent: () =>
      import('./shared/layout/layout')
        .then(m => m.Layout),
    children: [

      // 👑 ADMIN
      {
        path: 'admin',
        canActivate: [roleGuard],
        data: { roles: [ROLES.ROLE_ADMINISTRATOR] },
        loadChildren: () =>
          import('./features/admin/admin.routes')
            .then(m => m.ADMIN_ROUTES)
      },

      // ⚙️ CONFIGURATION 
      {
        path: 'configuracion',
        loadChildren: () =>
          import('./features/configuration/configuration.routes')
            .then(m => m.CONFIGURATION_ROUTES)
      },
      {
        path: 'mi-perfil',
        loadComponent: () =>
          import('./features/users/profile/profile-page/profile-page')
            .then(m => m.ProfilePage),
      },
      {
        path: 'mi-cuenta',
        loadComponent: () =>
          import('./features/users/account/account')
            .then(m => m.Account),
      },

    ]

  }
];