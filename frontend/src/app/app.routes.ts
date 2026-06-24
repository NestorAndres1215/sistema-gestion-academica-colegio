import { Routes } from '@angular/router';
import { Principal } from './features/principal/principal';

export const routes: Routes = [

    {
        path: '',
        component: Principal,
        pathMatch: 'full'
    },

];
