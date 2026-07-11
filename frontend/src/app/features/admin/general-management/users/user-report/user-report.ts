import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { firstValueFrom } from 'rxjs';

import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { PageHeader } from '../../../../../shared/ui/page-header/page-header';
import { StatCard } from '../../../../../shared/ui/stat-card/stat-card';

import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { Statistic } from '../../../../../core/models/statistic.interface';

import { AdminStatisticsService } from '../../../../../core/services/admin-statistics.service';
import { ChartBar } from "../../../../../shared/chart/chart-bar/chart-bar";
import { ChartDonut } from "../../../../../shared/chart/chart-donut/chart-donut";

@Component({
  selector: 'app-user-report',
  standalone: true,
  imports: [
    CommonModule,
    BreadCrumb,
    PageHeader,
    StatCard,
    ChartBar,
    ChartDonut
  ],
  templateUrl: './user-report.html',
  styleUrl: './user-report.css'
})
export class UserReport implements OnInit {

  private readonly adminStatisticsService = inject(AdminStatisticsService);
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly icon = 'assessment';
  readonly title = 'Reportes de administradores';
  readonly subtitle = 'Análisis y generación de reportes de la actividad y gestión de los administradores';
  readonly totalAdmins = signal<Statistic | null>(null);
  readonly activeAdmins = signal<Statistic | null>(null);
  readonly inactiveAdmins = signal<Statistic | null>(null);
  readonly lastMonthAdmins = signal<Statistic | null>(null);
  readonly genderAdmins = signal<Statistic[]>([]);
  readonly statusAdmins = signal<Statistic[]>([]);
  readonly monthsAdmins = signal<Statistic[]>([]);

  async ngOnInit(): Promise<void> {
    this.loadBreadcrumbs();
    await this.loadStatistics();
    await this.loadChart();
  }

  private loadBreadcrumbs(): void {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Reporte de Administrador' }
    ]);
  }

  private async loadStatistics(): Promise<void> {
    const [total, active, inactive, lastMonth] = await Promise.all([
      firstValueFrom(this.adminStatisticsService.getTotal()),
      firstValueFrom(this.adminStatisticsService.getActive()),
      firstValueFrom(this.adminStatisticsService.getInactive()),
      firstValueFrom(this.adminStatisticsService.getLastMonth())
    ]);

    this.totalAdmins.set(total);
    this.activeAdmins.set(active);
    this.inactiveAdmins.set(inactive);
    this.lastMonthAdmins.set(lastMonth);
  }

  private async loadChart(): Promise<void> {
    const [gender, status, sixMonth] = await Promise.all([
      firstValueFrom(this.adminStatisticsService.getGender()),
      firstValueFrom(this.adminStatisticsService.getStatus()),
      firstValueFrom(this.adminStatisticsService.getRegistersSixMonths())
    ]);

    this.genderAdmins.set(gender);
    this.statusAdmins.set(status);
    this.monthsAdmins.set(sixMonth);
  }


}