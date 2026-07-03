import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Service()
export class CompanyService {

    private readonly http = inject(HttpClient);
    private readonly backendUrl = environment.baseUrl;

    getAll(): Observable<any> {
        return this.http.get<any>(`${this.backendUrl}/company`);
    }

    getById(id: string): Observable<any> {
        return this.http.get<any>(`${this.backendUrl}/company/${id}`);
    }

    getByName(name: string): Observable<any> {
        return this.http.get<any>(`${this.backendUrl}/company/name/${name}`);
    }

    create(data: FormData) {
        return this.http.post(`${this.backendUrl}/company/`, data);
    }

    update(id: string, data: FormData) {
        return this.http.put(`${this.backendUrl}/company/${id}`, data);
    }
}
