import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';

import { environment } from '../../../environments/environment';
import { TokenStatus } from '../models/login.interface';
import { AlertService } from './alert.service';

@Service()
export class AuthService {

    private readonly http = inject(HttpClient);

    private readonly backendUrl = environment.baseUrl;

    private readonly router = inject(Router);
    private readonly alertService = inject(AlertService);
    private loginStatusSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('jwt'));
    readonly loginStatus$ = this.loginStatusSubject.asObservable();



    generateToken(loginData: any): Observable<any> {
        return this.http.post(`${this.backendUrl}/auth/generate-token`, loginData);
    }

    // USER INFO
    getCurrentUser(): Observable<any> {
        return this.http.get(`${this.backendUrl}/auth/current-user`);
    }

    // TOKEN
    token(): string | null {
        return localStorage.getItem('jwt');
    }

    isLoggedIn(): boolean {
        return !!this.token();
    }

    setToken(token: string): void {
        localStorage.setItem('jwt', token);
        this.loginStatusSubject.next(true);
    }
    logout(): void {
        localStorage.removeItem('jwt');
        this.loginStatusSubject.next(false);
    }

    changePassword(id: string, data: any): Observable<any> {
        return this.http.post(`${this.backendUrl}/auth/${id}/change-password`, data);
    }


    logoutSession(userId: string): Observable<void> {
        return this.http.post<void>(`${this.backendUrl}/auth/logout/${userId}`, {});
    }

    findStatus(userId: number): Observable<TokenStatus[]> {
        return this.http.get<TokenStatus[]>(`${this.backendUrl}/auth/session/${userId}/status`);
    }

    getHomeByRole(role: string): string {
        const map: any = {
            ROLE_ADMINISTRATOR: '/admin',
            ROLE_TEACHER: '/teacher',
            ROLE_STUDENT: '/student'
        };

        return map[role] ?? '/';
    }

    checkSessionStatus(id: number): void {
        this.findStatus(id)
            .subscribe({
                next: status => {

                    const active = status.some(s => s.status === 'ACTIVE');

                    if (!active) {

                        this.logout();
                        this.router.navigate(['/auth/login']);

                    }
                },

                error: error => {
                    console.error('🔥 Error checkSessionStatus:', error);
                },

                complete: () => {
                    console.log('🏁 checkSessionStatus terminado');
                }

            });
    }

}