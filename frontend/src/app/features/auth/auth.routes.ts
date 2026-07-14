
import { Routes } from '@angular/router';
import { Login } from '../auth/login/login';
import { noAuthGuard } from '../../core/guards/no-auth.guard';

export const AUTHENTICATION_ROUTES: Routes = [
  {
    path: 'login',
    canActivate: [noAuthGuard],
    component: Login
  }

];