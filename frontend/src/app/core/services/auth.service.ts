import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';

import { environment } from '../../../environments/environment';

@Service()
export class AuthService {

    private readonly http = inject(HttpClient);
    private readonly router = inject(Router);
    private readonly backendUrl = environment.baseUrl;


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
        this.router.navigate(['/auth/login']);
    }

    changePassword(id: string, data: any): Observable<any> {
        return this.http.post(`${this.backendUrl}/auth/${id}/change-password`, data);
    }

    generateSession() {
        return this.http.post(`${this.backendUrl}/auth/generate-session`, {});
    }

    logoutSession(userId: string): Observable<void> {
        return this.http.post<void>(`${this.backendUrl}/logout/${userId}`, {});
    }

    getHomeByRole(role: string): string {
        const map: any = {
            ROLE_ADMINISTRATOR: '/admin',
            ROLE_TEACHER: '/teacher',
            ROLE_STUDENT: '/student'
        };

        return map[role] ?? '/';
    }

}