import { Routes } from '@angular/router';

export const ADMIN_ROUTES: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('../admin/admin-main/admin-main')
                .then(m => m.AdminMain),
    },
    {
        path: 'usuarios',
        loadChildren: () =>
            import('../admin/general-management/users/users.routes')
                .then(m => m.USERS_ROUTES),
    }
];