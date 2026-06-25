import { Routes } from "@angular/router";
import { NoAuthGuard } from "../../core/guards/noauth.guard";

export const AUTHENTICATION_ROUTES: Routes = [

    {
        path: '',
        redirectTo: 'login',
        pathMatch: 'full'
    },
    {
        path: 'login',
        loadComponent: () => import('./login/login').then(m => m.Login),
        canActivate: [NoAuthGuard]
    }

]