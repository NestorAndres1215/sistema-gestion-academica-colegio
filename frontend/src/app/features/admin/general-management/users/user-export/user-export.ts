import { Component, computed, inject, signal } from '@angular/core';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { SelectFilterOption } from '../../../../../core/models/select-option.interface';
import { MatCheckboxModule } from "@angular/material/checkbox";
import { SelectFilter } from "../../../../../shared/ui/select-filter/select-filter";
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { AdminStatisticsService } from '../../../../../core/services/admin-statistics.service';
import { Statistic } from '../../../../../core/models/statistic.interface';
import { firstValueFrom } from 'rxjs';
import { AdminReportService } from '../../../../../core/services/admin-report.service';
import { AdminReportRequest } from '../../../../../core/models/admin.interface';
import { PrintService } from '../../../../../core/services/print.service';
import { CompanyService } from '../../../../../core/services/company.service';

interface ExportField {
  key: string;
  label: string;
  selected: boolean;
}

@Component({
  selector: 'app-user-export',
  imports: [MatCheckboxModule, SelectFilter, BreadCrumb, PageHeader],
  templateUrl: './user-export.html',
  styleUrl: './user-export.css',
})
export class UserExport {

  readonly icon = "download";
  readonly title = "Exportación de usuarios";
  readonly subtitle = "Genera reportes de usuarios con los filtros seleccionados.";
  private readonly adminStatisticsService = inject(AdminStatisticsService);
  private readonly adminReportService = inject(AdminReportService);
  private readonly companyService = inject(CompanyService)
  private readonly printService = inject(PrintService)
  readonly totalAdmins = signal<Statistic | null>(null);
  readonly activeAdmins = signal<Statistic | null>(null);
  readonly inactiveAdmins = signal<Statistic | null>(null);
  readonly exportingPdf = signal(false);
  readonly exportingExcel = signal(false);
  readonly statusFilter = signal('');
  private originalTotal!: Statistic;
  private originalActive!: Statistic;
  private originalInactive!: Statistic;
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);

  async ngOnInit(): Promise<void> {
    this.loadBreadcrumbs();
    await this.loadStatistics();
  }

  private loadBreadcrumbs(): void {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Reporte de Administrador' }
    ]);
  }

  private async loadStatistics(): Promise<void> {
    const [total, active, inactive] = await Promise.all([
      firstValueFrom(this.adminStatisticsService.getTotal()),
      firstValueFrom(this.adminStatisticsService.getActive()),
      firstValueFrom(this.adminStatisticsService.getInactive()),
    ]);

    this.originalTotal = total;
    this.originalActive = active;
    this.originalInactive = inactive;

    this.totalAdmins.set(total);
    this.activeAdmins.set(active);
    this.inactiveAdmins.set(inactive);
  }

  readonly statusOptions: SelectFilterOption[] = [
    { value: '', label: 'Todos los estados' },
    { value: 'ACTIVE', label: 'Activo' },
    { value: 'INACTIVE', label: 'Inactivo' },
  ];

  readonly fields = signal<ExportField[]>([
    { key: 'name', label: 'Nombre', selected: true },
    { key: 'lastName', label: 'Apellido', selected: true },
    { key: 'email', label: 'Correo', selected: true },
    { key: 'phone', label: 'Teléfono', selected: true },
    { key: 'dni', label: 'DNI', selected: true },
    { key: 'gender', label: 'Género', selected: true },
  ]);

  readonly selectedFields = computed(() =>
    this.fields().filter(f => f.selected)
  );

  onStatusFilterChange(status: string): void {
    this.statusFilter.set(status);

    if (status === 'ACTIVE') {
      this.totalAdmins.set({
        ...this.originalTotal,
        quantity: this.originalActive.quantity
      });

      this.activeAdmins.set(this.originalActive);

      this.inactiveAdmins.set({
        ...this.originalInactive,
        quantity: 0
      });

    } else if (status === 'INACTIVE') {

      this.totalAdmins.set({
        ...this.originalTotal,
        quantity: this.originalInactive.quantity
      });

      this.activeAdmins.set({
        ...this.originalActive,
        quantity: 0
      });

      this.inactiveAdmins.set(this.originalInactive);

    } else {

      this.totalAdmins.set(this.originalTotal);
      this.activeAdmins.set(this.originalActive);
      this.inactiveAdmins.set(this.originalInactive);
    }
  }


  toggleField(key: string): void {

    this.fields.update(list =>
      list.map(f => f.key === key ? { ...f, selected: !f.selected } : f)
    );

  }

  toggleAllFields(checked: boolean): void {
    this.fields.update(list => list.map(f => ({ ...f, selected: checked })));
  }

  readonly canExport = computed(() =>
    this.selectedFields().length > 0
  );


  private buildRequest(): AdminReportRequest {
    const selected = this.fields();

    return {
      name: selected.find(f => f.key === 'name')?.selected ?? false,
      lastName: selected.find(f => f.key === 'lastName')?.selected ?? false,
      email: selected.find(f => f.key === 'email')?.selected ?? false,
      phone: selected.find(f => f.key === 'phone')?.selected ?? false,
      dni: selected.find(f => f.key === 'dni')?.selected ?? false,
      gender: selected.find(f => f.key === 'gender')?.selected ?? false,
      status: true,
      statusFilter: this.statusFilter()
    };
  }

  async exportPdf(): Promise<void> {

    if (!this.canExport()) return;

    this.exportingPdf.set(true);

    try {
      const blob = await firstValueFrom(
        this.adminReportService.generatePdf(this.buildRequest())
      );

      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'administrators.pdf';
      link.click();
      URL.revokeObjectURL(url);

    } catch (error) {
      console.error(error);
    } finally {
      this.exportingPdf.set(false);
    }
  }

  async exportExcel(): Promise<void> {

    if (!this.canExport()) return;

    this.exportingExcel.set(true);

    try {

      const blob = await firstValueFrom(
        this.adminReportService.generateExcel(this.buildRequest())
      );

      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'reporte_administradores.xlsx';
      link.click();
      window.URL.revokeObjectURL(url);


    } catch (error) {
      console.error('Error al exportar Excel', error);
    } finally {
      this.exportingExcel.set(false);
    }

  }

  async print(): Promise<void> {
    if (!this.canExport()) {
      return;
    }

    try {
      const [admins, empresa] = await Promise.all([
        firstValueFrom(this.adminReportService.getReport(this.buildRequest())),
        firstValueFrom(this.companyService.getById("COMP0001")),
      ]);

      this.printService.printJsonAsTable(
        admins,
        'Administradores',
        { name: empresa.name, logoUrl: empresa.logo },
      );
    } catch (error) {
      console.error('Error al preparar la impresión.', error);
    }
  }

}
