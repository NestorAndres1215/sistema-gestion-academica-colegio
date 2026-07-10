import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { AdminRequest, AdminResponse } from '../models/admin.interface';


@Service()
export class AdminService {

  private readonly http = inject(HttpClient);
  private readonly backendUrl = environment.baseUrl;

  getAll(status: string, page: number, size: number, search: string = ''): Observable<any> {

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (status !== null && status !== undefined) {
      params = params.set('status', status.toString());
    }

    if (search.trim() !== '') {
      params = params.set('search', search.trim());
    }

    return this.http.get<any>(`${this.backendUrl}/admin`, { params });
  }

  search(search?: string): Observable<AdminResponse[]> {

    let params = new HttpParams();

    if (search?.trim()) {
      params = params.set('search', search.trim());
    }

    return this.http.get<AdminResponse[]>(`${this.backendUrl}/admin/search`, { params });
  }


  getById(id: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/admin/${id}`);
  }

  getByIdEmail(email: string): Observable<any> {
    return this.http.get<any>(`${this.backendUrl}/admin/email/${email}`);
  }

  create(data: AdminRequest) {
    console.log('Entró al service', data);
    return this.http.post(`${this.backendUrl}/admin`, data);
  }

  update(id: Number, data: FormData) {
    return this.http.put(`${this.backendUrl}/admin/${id}`, data);
  }

  deactivate(id: string): Observable<any> {
    return this.http.put(`${this.backendUrl}/admin/deactivate/${id}`, {});
  }

  activate(id: string): Observable<any> {
    return this.http.put(`${this.backendUrl}/admin/activate/${id}`, {});
  }

}
