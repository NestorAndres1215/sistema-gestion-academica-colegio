import { Routes } from "@angular/router";


export const CONFIGURATION_ROUTES: Routes = [
    {
        path: 'cambiar-tema',
        loadComponent: () =>
            import('./theme/theme')
                .then(m => m.Theme),

    },
]