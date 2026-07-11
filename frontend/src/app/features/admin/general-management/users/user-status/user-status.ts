import { Component, computed, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../../../core/services/auth.service';
import { AdminService } from '../../../../../core/services/admin.service';
import { SelectFilter } from "../../../../../shared/ui/select-filter/select-filter";
import { SelectFilterOption } from '../../../../../core/models/select-option.interface';
import { UserModel } from '../../../../../core/models/user.interface';
import { TableColumn } from '../../../../../core/models/table.interface';
import { Table, TableAction } from "../../../../../shared/ui/table/table";
import { Pagination } from "../../../../../shared/ui/pagination/pagination";
import { AlertService } from '../../../../../core/services/alert.service';

@Component({
  selector: 'app-user-status',
  imports: [BreadCrumb, PageHeader, SelectFilter, Table, Pagination],
  templateUrl: './user-status.html',
  styleUrl: './user-status.css',
})
export class UserStatus {


  private readonly adminService = inject(AdminService);
  private readonly alertService = inject(AlertService)
  readonly icon = "manage_accounts";
  readonly title = "Estado de cuentas";
  readonly subtitle = "Gestión del estado de las cuentas de usuario (activas, bloqueadas e inactivas)";
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly users = signal<UserModel[]>([]);
  readonly totalItems = signal(0);
  readonly currentPage = signal(1);
  readonly pageSize = signal(5);
  readonly searchTerm = signal('');
  readonly statusFilter = signal('active');

  readonly statusOptions: SelectFilterOption[] = [
    { value: 'active', label: 'Activo' },
    { value: 'inactive', label: 'Inactivo' }
  ];

  async ngOnInit(): Promise<void> {
    await this.initUser();
    await this.loadUsers();
  }

  private async initUser(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Estado de Cuentas' }
    ]);
  }

  async loadUsers(): Promise<void> {

    const status = this.statusFilter()
      ? this.statusFilter().toUpperCase()
      : '';

    const res: any = await firstValueFrom(
      this.adminService.getAll(
        status,
        this.currentPage() - 1,
        this.pageSize(),
        this.searchTerm()
      )
    );

    this.users.set(
      res.content.map((admin: any) => ({
        id: admin.id,
        fullName: `${admin.firstName} ${admin.paternalLastName}`,
        birthDate: admin.birthDate,
        username: admin.username,
        email: admin.email,
        role: admin.role,
        status: admin.status === 'ACTIVE'
          ? 'activo'
          : 'inactivo'
      }))
    );

    this.totalItems.set(res.totalElements);
  }

  async onStatusFilterChange(status: string): Promise<void> {
    this.statusFilter.set(status);
    this.currentPage.set(1);
    await this.loadUsers();
  }

  async onPageChange(page: number): Promise<void> {
    this.currentPage.set(page);
    await this.loadUsers();
  }

  async onPageSizeChange(size: number): Promise<void> {
    this.pageSize.set(size);
    this.currentPage.set(1);
    await this.loadUsers();
  }

  readonly columns: TableColumn[] = [
    { key: 'fullName', label: 'Nombre', },
    { key: 'username', label: 'Usuario', },
    { key: 'email', label: 'Correo', },
    { key: 'status', label: 'Estado', width: '120px' }
  ];


  async onDeactivate(fila: any): Promise<void> {
    const confirmed = await this.alertService.confirm(`¿Desactivar a ${fila.fullName}?`, 'El usuario ya no podrá acceder al sistema.');

    if (!confirmed) {
      this.alertService.info('Acción cancelada', `No se desactivó a ${fila.fullName}.`);
      return;
    }

    try {
      await firstValueFrom(this.adminService.deactivate(fila.id));
      this.alertService.success('Usuario desactivado', `${fila.fullName} ha sido desactivado correctamente.`);
      await this.loadUsers();
    } catch {
      this.alertService.error('Error', 'No se pudo desactivar el usuario.');
    }
  }

  async onActivate(fila: any): Promise<void> {
    const confirmed = await this.alertService.confirm(`¿Activar a ${fila.fullName}?`, 'El usuario volverá a tener acceso al sistema.');

    if (!confirmed) {
      this.alertService.info('Acción cancelada', `No se activó a ${fila.fullName}.`);
      return;
    }

    try {
      await firstValueFrom(this.adminService.activate(fila.id));
      this.alertService.success('Usuario activado', `${fila.fullName} ha sido activado correctamente.`);
      await this.loadUsers();
    } catch {
      this.alertService.error('Error', 'No se pudo activar el usuario.');
    }
  }

  readonly tableActions = computed<TableAction[]>(() => {
    switch (this.statusFilter()) {
      case 'active':
        return ['deactivate'];

      case 'inactive':
        return ['activate'];

      default:
        return ['activate', 'deactivate'];
    }
  });
}
