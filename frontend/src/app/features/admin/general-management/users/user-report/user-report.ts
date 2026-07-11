import { Component, OnInit, signal, computed } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';

import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";


// Modelo del administrador, definido dentro del propio componente
export interface AdminModel {
  id: string;
  name: string;
  status: 'activo' | 'inactivo';
  gender: 'masculino' | 'femenino';
  createdAt: string; // ISO
}

interface StatCard {
  label: string;
  value: number;
  icon: string;
  variant: 'primary' | 'success' | 'danger' | 'info' | 'warning';
}

@Component({
  selector: 'app-user-report',
  standalone: true,
  imports: [BaseChartDirective, BreadCrumb, PageHeader],

  templateUrl: './user-report.html',
  styleUrl: './user-report.css',
})
export class UserReport {

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly icon = "assessment";
  readonly title = "Reportes de administradores";
readonly subtitle = "Análisis y generación de reportes de la actividad y gestión de los administradores";



  // Base de datos simulada de administradores
  readonly allAdmins = signal<AdminModel[]>([
    { id: '1', name: 'Juan Pérez', status: 'activo', gender: 'masculino', createdAt: '2026-07-01' },
    { id: '2', name: 'Valeria Flores', status: 'activo', gender: 'femenino', createdAt: '2026-07-05' },
    { id: '3', name: 'Fernando Gutiérrez', status: 'activo', gender: 'masculino', createdAt: '2026-06-12' },
    { id: '4', name: 'Rosa Delgado', status: 'inactivo', gender: 'femenino', createdAt: '2026-06-18' },
    { id: '5', name: 'Manuel Cárdenas', status: 'activo', gender: 'masculino', createdAt: '2026-05-22' },
    { id: '6', name: 'Elena Ruiz', status: 'inactivo', gender: 'femenino', createdAt: '2026-05-30' },
    { id: '7', name: 'Andrés Silva', status: 'activo', gender: 'masculino', createdAt: '2026-04-03' },
    { id: '8', name: 'Patricia Salazar', status: 'activo', gender: 'femenino', createdAt: '2026-04-14' },
    { id: '9', name: 'Jorge Medina', status: 'inactivo', gender: 'masculino', createdAt: '2026-03-08' },
    { id: '10', name: 'Camila Rojas', status: 'activo', gender: 'femenino', createdAt: '2026-03-20' },
  ]);

  // --- Totales base ---
  readonly totalAdmins = computed(() => this.allAdmins().length);
  readonly activeCount = computed(() => this.allAdmins().filter(a => a.status === 'activo').length);
  readonly inactiveCount = computed(() => this.allAdmins().filter(a => a.status === 'inactivo').length);
  readonly maleCount = computed(() => this.allAdmins().filter(a => a.gender === 'masculino').length);
  readonly femaleCount = computed(() => this.allAdmins().filter(a => a.gender === 'femenino').length);

  readonly newThisMonth = computed(() => {
    const now = new Date();
    return this.allAdmins().filter(a => {
      const d = new Date(a.createdAt);
      return d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear();
    }).length;
  });

  readonly activePercentage = computed(() => {
    const total = this.totalAdmins();
    return total === 0 ? 0 : Math.round((this.activeCount() / total) * 100);
  });

  readonly statCards = computed<StatCard[]>(() => [
    { label: 'Total administradores', value: this.totalAdmins(), icon: 'admin_panel_settings', variant: 'primary' },
    { label: 'Activos', value: this.activeCount(), icon: 'check_circle', variant: 'success' },
    { label: 'Inactivos', value: this.inactiveCount(), icon: 'cancel', variant: 'danger' },
    { label: 'Nuevos este mes', value: this.newThisMonth(), icon: 'trending_up', variant: 'info' },
  ]);

  // --- Gráfico: Activos vs Inactivos (dona) ---
  readonly statusChartData = computed<ChartData<'doughnut'>>(() => ({
    labels: ['Activos', 'Inactivos'],
    datasets: [{
      data: [this.activeCount(), this.inactiveCount()],
      backgroundColor: ['#16a34a', '#f87171'],
      borderWidth: 0,
    }],
  }));

  // --- Gráfico: Distribución por género (dona) ---
  readonly genderChartData = computed<ChartData<'doughnut'>>(() => ({
    labels: ['Masculino', 'Femenino'],
    datasets: [{
      data: [this.maleCount(), this.femaleCount()],
      backgroundColor: ['#6366f1', '#ec4899'],
      borderWidth: 0,
    }],
  }));



  // --- Gráfico: Registros por mes (barras, últimos 6 meses) ---
  readonly monthlyChartData = computed<ChartData<'bar'>>(() => {
    const months = this.getLastSixMonths();

    const counts = months.map(({ year, month }) =>
      this.allAdmins().filter(a => {
        const d = new Date(a.createdAt);
        return d.getFullYear() === year && d.getMonth() === month;
      }).length
    );

    return {
      labels: months.map(m => m.label),
      datasets: [{
        label: 'Nuevos administradores',
        data: counts,
        backgroundColor: '#6366f1',
        borderRadius: 6,
        barThickness: 28,
      }],
    };
  });

  readonly doughnutOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '70%',
    layout: {
      padding: 12,
    },
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#94a3b8',
          padding: 20,
          font: { size: 13, weight: 500 },
          usePointStyle: true,
          pointStyle: 'circle',
          boxWidth: 8,
          boxHeight: 8,
        },
      },
      tooltip: {
        backgroundColor: '#1e293b',
        padding: 12,
        cornerRadius: 8,
        titleFont: { size: 13, weight: 600 },
        bodyFont: { size: 13 },
        displayColors: true,
        boxPadding: 4,
      },
    },
  };

  readonly barOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    layout: {
      padding: { top: 16, right: 8, bottom: 0, left: 0 },
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        backgroundColor: '#1e293b',
        padding: 12,
        cornerRadius: 8,
        titleFont: { size: 13, weight: 600 },
        bodyFont: { size: 13 },
      },
    },
    scales: {
      x: {
        ticks: { color: '#94a3b8', font: { size: 12 } },
        grid: { display: false },
        border: { display: false },
      },
      y: {
        beginAtZero: true,
        ticks: { color: '#94a3b8', stepSize: 1, font: { size: 12 }, padding: 8 },
        grid: { color: 'rgba(148,163,184,0.08)' },
        border: { display: false },
      },
    },
  };

  ngOnInit(): void {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/' },
      { label: 'Reportes' },
      { label: 'Administradores' },
    ]);
  }

  private getLastSixMonths(): { year: number; month: number; label: string }[] {
    const names = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
    const now = new Date();
    const result = [];

    for (let i = 5; i >= 0; i--) {
      const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
      result.push({ year: d.getFullYear(), month: d.getMonth(), label: names[d.getMonth()] });
    }

    return result;
  }
}
