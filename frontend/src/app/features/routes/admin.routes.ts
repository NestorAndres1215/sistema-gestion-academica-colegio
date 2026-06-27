import { Routes } from '@angular/router';

export const ADMIN_ROUTES: Routes = [
    {
        path: 'admin',
        loadComponent: () => import('../admin/admin-main/admin-main')
            .then(m => m.AdminMain)
    },
];