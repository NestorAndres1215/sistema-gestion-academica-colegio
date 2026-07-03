import { Routes } from "@angular/router";
import { Settings } from "./settings/settings";


export const CONFIGURATION_ROUTES: Routes = [

    {
        path: '',
        loadComponent: () =>
            import('./settings/settings')
                .then(m => m.Settings),
    },
    {
        path: 'cambiar-tema',
        loadComponent: () =>
            import('./theme/theme')
                .then(m => m.Theme),

    },
    {
        path: 'historial-usuarios',
        loadComponent: () =>
            import('./user-history/user-history')
                .then(m => m.UserHistory),

    },
    {
        path: 'company',
        loadComponent: () =>
            import('./company/company')
                .then(m => m.Company),

    },
    {
        path: 'cambiar-contrasena',
        loadComponent: () =>
            import('../users/change-password/change-password')
                .then(m => m.ChangePassword),
    },
]