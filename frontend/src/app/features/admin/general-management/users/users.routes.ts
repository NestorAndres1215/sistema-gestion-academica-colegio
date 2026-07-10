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
        path: 'busqueda',
        loadComponent: () =>
            import('./user-advanced-search/user-advanced-search')
                .then(m => m.UserAdvancedSearch)
    }
];