import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Service()
export class MenuService {

    private readonly http = inject(HttpClient);
    private readonly backendUrl = environment.baseUrl;

    getAll(): Observable<any> {
        return this.http.get<any>(`${this.backendUrl}/menu`);
    }

}
