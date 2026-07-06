import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Service()
export class UserStoryService {

    private readonly http = inject(HttpClient);
    private readonly backendUrl = environment.baseUrl;

    findWithFilters(filters: any): Observable<any> {

        let params = new HttpParams()
            .set('email', filters.email ?? '')
            .set('page', filters.page ?? 0)
            .set('size', filters.size ?? 10)
            .set('sort', filters.sort ?? 'desc');

        if (filters.status) {
            params = params.set('status', filters.status);
        }

        if (filters.action) {
            params = params.set('action', filters.action);
        }

        // 🔥 FECHA FORMATEADA (OBLIGATORIO para Spring LocalDateTime)
        if (filters.dateFrom) {
            params = params.set('dateFrom', this.formatDate(filters.dateFrom));
        }

        if (filters.dateTo) {
            params = params.set('dateTo', this.formatDate(filters.dateTo));
        }

        return this.http.get<any>(`${this.backendUrl}/user-story`, { params });
    }

    // ✅ Convierte Date → "YYYY-MM-DDTHH:mm:ss"
    private formatDate(date: Date): string {

        const d = new Date(date);

        const pad = (n: number) => n.toString().padStart(2, '0');

        return (
            d.getFullYear() +
            '-' + pad(d.getMonth() + 1) +
            '-' + pad(d.getDate()) +
            'T' + pad(d.getHours()) +
            ':' + pad(d.getMinutes()) +
            ':' + pad(d.getSeconds())
        );
    }
}
