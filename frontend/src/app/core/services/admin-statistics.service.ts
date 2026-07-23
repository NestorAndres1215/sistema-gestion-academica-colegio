import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { Statistic } from '../models/statistic.interface';
import { PercentageStatistic } from '../models/percentage-statistic.interface';

@Service()
export class AdminStatisticsService {
  private readonly http = inject(HttpClient);
  private readonly backendUrl = environment.baseUrl;

  getTotal(): Observable<Statistic> {
    return this.http.get<Statistic>(`${this.backendUrl}/dashboard/admin/total`);
  }

  getActive(): Observable<Statistic> {
    return this.http.get<Statistic>(`${this.backendUrl}/dashboard/admin/active`);
  }

  getInactive(): Observable<Statistic> {
    return this.http.get<Statistic>(`${this.backendUrl}/dashboard/admin/inactive`);
  }

  getLastMonth(): Observable<Statistic> {
    return this.http.get<Statistic>(`${this.backendUrl}/dashboard/admin/last-month`);
  }

  getGender(): Observable<Statistic[]> {
    return this.http.get<Statistic[]>(`${this.backendUrl}/dashboard/admin/gender`);
  }

  getRegistersSixMonths(): Observable<Statistic[]> {
    return this.http.get<Statistic[]>(`${this.backendUrl}/dashboard/admin/registers-six-months`);
  }

  getStatus(): Observable<Statistic[]> {
    return this.http.get<Statistic[]>(`${this.backendUrl}/dashboard/admin/status`);
  }
}
