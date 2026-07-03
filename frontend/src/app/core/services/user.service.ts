import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Service()
export class UserService {

    private readonly http = inject(HttpClient);
    private readonly backendUrl = environment.baseUrl;

    create(request: any): Observable<any> {
        return this.http.post<any>(`${this.backendUrl}/users`, request);
    }

    update(id: string, request: any): Observable<any> {
        return this.http.put<any>(`${this.backendUrl}/users/${id}`, request);
    }

    findById(id: string): Observable<any> {
        return this.http.get<any>(`${this.backendUrl}/users/${id}`);
    }
}
