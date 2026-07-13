import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { AdminReportRequest } from '../models/admin.interface';

@Service()
export class AdminReportService {

    private readonly http = inject(HttpClient);
    private readonly backendUrl = environment.baseUrl;

    generatePdf(request: AdminReportRequest): Observable<Blob> {
        return this.http.post(`${this.backendUrl}/reports/admin/pdf`, request, {
            responseType: 'blob'
        });
    }

    generatePdfById(id: number): Observable<Blob> {
        return this.http.post(`${this.backendUrl}/reports/admin/pdf/${id}`, null, {
            responseType: 'blob'
        });
    }

    generateExcel(request: AdminReportRequest): Observable<Blob> {
        return this.http.post(`${this.backendUrl}/reports/admin/excel`, request, {
            responseType: 'blob'
        });
    }

    getReport(request: AdminReportRequest): Observable<Record<string, unknown>[]> {
        return this.http.post<Record<string, unknown>[]>(`${this.backendUrl}/reports/admin/print`, request);
    }

}
