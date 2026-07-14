import { Component, computed, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { PageHeader } from '../../../../../shared/ui/page-header/page-header';
import { SelectFilter } from '../../../../../shared/ui/select-filter/select-filter';
import { Table, TableAction } from '../../../../../shared/ui/table/table';
import { Pagination } from '../../../../../shared/ui/pagination/pagination';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { SelectFilterOption } from '../../../../../core/models/select-option.interface';
import { UserModel } from '../../../../../core/models/user.interface';
import { TableColumn } from '../../../../../core/models/table.interface';
import { AdminService } from '../../../../../core/services/admin.service';
import { AlertService } from '../../../../../core/services/alert.service';
import { MatIconModule } from '@angular/material/icon';
import { Button } from '../../../../../shared/ui/button/button';

@Component({
  selector: 'app-user-bulk-status',
  imports: [BreadCrumb, PageHeader, SelectFilter, Table, Pagination, MatIconModule, Button],
  templateUrl: './user-bulk-status.html',
  styleUrl: './user-bulk-status.css',
})
export class UserBulkStatus {

  private readonly adminService = inject(AdminService);
  private readonly alertService = inject(AlertService);
  readonly icon = 'fact_check';
  readonly title = 'Cambio masivo de estado';
  readonly subtitle = 'Selecciona varios usuarios para activarlos o desactivarlos a la vez';
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly users = signal<UserModel[]>([]);
  readonly totalItems = signal(0);
  readonly currentPage = signal(1);
  readonly pageSize = signal(5);
  readonly searchTerm = signal('');
  readonly statusFilter = signal('active');
  readonly selectedIds = signal<Set<any>>(new Set());
  readonly submitting = signal(false);

  readonly statusOptions: SelectFilterOption[] = [
    { value: 'active', label: 'Activo' },
    { value: 'inactive', label: 'Inactivo' }
  ];

  readonly columns: TableColumn[] = [
    { key: 'fullName', label: 'Nombre' },
    { key: 'username', label: 'Usuario' },
    { key: 'email', label: 'Correo' },
    { key: 'status', label: 'Estado', width: '120px' }
  ];

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

  readonly selectedCount = computed(() => this.selectedIds().size);

  readonly bulkActionLabel = computed(() =>
    this.statusFilter() === 'active' ? 'Desactivar seleccionados' : 'Activar seleccionados'
  );

  readonly bulkActionIcon = computed(() =>
    this.statusFilter() === 'active' ? 'block' : 'check_circle'
  );

  readonly bulkActionVariant = computed<'danger' | 'success'>(() =>
    this.statusFilter() === 'active' ? 'danger' : 'success'
  );

  readonly selectAllMatching = signal(false);
  readonly loadingAllIds = signal(false);
  readonly hasMoreThanPage = computed(() => this.totalItems() > this.users().length);

  readonly canSelectAllMatching = computed(() =>
    this.selectedCount() > 0 &&
    !this.selectAllMatching() &&
    this.hasMoreThanPage() &&
    this.selectedCount() < this.totalItems()
  );

  async ngOnInit(): Promise<void> {
    await this.initHeader();
    await this.loadUsers();
  }

  private async initHeader(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Cambio masivo de estado' }
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
    this.clearSelection();
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

  onSelectionChange(ids: Set<any>): void {
    this.selectedIds.set(ids);
    if (ids.size !== this.totalItems()) {
      this.selectAllMatching.set(false);
    }
  }

  clearSelection(): void {
    this.selectedIds.set(new Set());
    this.selectAllMatching.set(false);
  }

  backToPageSelection(): void {
    const pageIds = new Set(this.users().map(u => u.id));
    this.selectedIds.set(pageIds);
    this.selectAllMatching.set(false);
  }

  async selectAllAcrossPages(): Promise<void> {
    this.loadingAllIds.set(true);
    try {
      const status = this.statusFilter() ? this.statusFilter().toUpperCase() : '';

      const res: any = await firstValueFrom(
        this.adminService.getAll(status, 0, this.totalItems(), this.searchTerm())
      );

      const allIds = res.content.map((admin: any) => admin.id);
      this.selectedIds.set(new Set(allIds));
      this.selectAllMatching.set(true);
    } catch {
      this.alertService.error('Error', 'No se pudo seleccionar a todos los usuarios.');
    } finally {
      this.loadingAllIds.set(false);
    }
  }

  async onDeactivate(fila: any): Promise<void> {
    const confirmed = await this.alertService.confirm(
      `¿Desactivar a ${fila.fullName}?`,
      'El usuario ya no podrá acceder al sistema.'
    );

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
    const confirmed = await this.alertService.confirm(
      `¿Activar a ${fila.fullName}?`,
      'El usuario volverá a tener acceso al sistema.'
    );

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


  async operar(): Promise<void> {
    const count = this.selectedCount();
    if (count === 0) return;

    const willActivate = this.statusFilter() !== 'active';
    const verb = willActivate ? 'activar' : 'desactivar';

    const confirmed = await this.alertService.confirm(
      `¿${willActivate ? 'Activar' : 'Desactivar'} ${count} usuario(s)?`,
      willActivate
        ? 'Los usuarios seleccionados volverán a tener acceso al sistema.'
        : 'Los usuarios seleccionados ya no podrán acceder al sistema.'
    );

    if (!confirmed) {
      this.alertService.info('Acción cancelada', `No se pudo ${verb} a los usuarios seleccionados.`);
      return;
    }

    this.submitting.set(true);

    try {
      const ids = Array.from(this.selectedIds());

      const requests = ids.map(id =>
        firstValueFrom(
          willActivate
            ? this.adminService.activate(id)
            : this.adminService.deactivate(id)
        )
      );

      await Promise.all(requests);

      this.alertService.success(
        `Usuarios ${willActivate ? 'activados' : 'desactivados'}`,
        `${ids.length} usuario(s) ${willActivate ? 'activados' : 'desactivados'} correctamente.`
      );

      this.clearSelection();
      await this.loadUsers();

    } catch {
      this.alertService.error('Error', `No se pudo ${verb} a los usuarios seleccionados.`);
    } finally {
      this.submitting.set(false);
    }
  }
}