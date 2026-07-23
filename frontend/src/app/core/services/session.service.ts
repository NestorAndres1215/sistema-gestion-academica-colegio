import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Service()
export class SessionService {
  private readonly http = inject(HttpClient);
  private readonly backendUrl = environment.baseUrl;

  getAll(page: number, size: number, search: string = ''): Observable<any> {
    let params = new HttpParams().set('page', page.toString()).set('size', size.toString());

    if (search.trim() !== '') {
      params = params.set('search', search.trim());
    }

    return this.http.get<any>(`${this.backendUrl}/session`, { params });
  }
}
