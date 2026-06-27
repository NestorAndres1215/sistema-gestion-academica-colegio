
import { Routes } from '@angular/router';
import { Login } from '../auth/login/login';

export const AUTHENTICATION_ROUTES: Routes = [
  {
    path: 'login',
    component: Login
  }
];