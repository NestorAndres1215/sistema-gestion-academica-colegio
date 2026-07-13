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
    },
    {
        path: 'reporte',
        loadComponent: () =>
            import('./user-report/user-report')
                .then(m => m.UserReport)
    },
    {
        path: 'exportar',
        loadComponent: () =>
            import('./user-export/user-export')
                .then(m => m.UserExport)
    },
    {
        path: 'importar',
        loadComponent: () =>
            import('./user-import/user-import')
                .then(m => m.UserImport)
    }

];