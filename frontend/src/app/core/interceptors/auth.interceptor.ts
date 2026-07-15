import { HttpInterceptorFn } from "@angular/common/http";
import { AuthService } from "../services/auth.service";
import { inject } from "@angular/core";
import { catchError, throwError } from 'rxjs';
import { AlertService } from "../services/alert.service";
import { Router } from "@angular/router";

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);
  const alertService = inject(AlertService);
  const router = inject(Router);

  const token = authService.token();

  if (!token) return next(req);


  const cloned = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });


  return next(cloned).pipe(
    catchError(error => {

      if (error.status === 401 || error.status === 403) {
        alertService.error('Sesión cerrada', 'Tu sesión fue cerrada desde otro dispositivo.');
        authService.logout();
        router.navigate(['/auth/login']);
      }

      return throwError(() => error);

    })

  );
};