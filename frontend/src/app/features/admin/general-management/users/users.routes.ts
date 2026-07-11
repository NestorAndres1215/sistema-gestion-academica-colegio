import { Routes } from '@angular/router';

export const USERS_ROUTES: Routes = [
    {
        path: 'listado-usuario',
        loadComponent: () =>
            import('./user-list/user-list')
                .then(m => m.UserList),
    },
    {
        path: 'registro-usuario',
        loadComponent: () =>
            import('./user-register/user-register')
                .then(m => m.UserRegister)
    },
    {
        path: 'estado-cuenta',
        loadComponent: () =>
            import('./user-status/user-status')
                .then(m => m.UserStatus)
    },
    {
        path: 'busqueda',
        loadComponent: () =>
            import('./user-advanced-search/user-advanced-search')
                .then(m => m.UserAdvancedSearch)
    }
];