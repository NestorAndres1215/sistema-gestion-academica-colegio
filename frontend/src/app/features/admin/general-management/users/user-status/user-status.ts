import { Component, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { BreadcrumbItem } from '../../../../../core/models/bread-crumb.interface';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../../../core/services/auth.service';
import { AdminService } from '../../../../../core/services/admin.service';
import { SelectFilter } from "../../../../../shared/ui/select-filter/select-filter";
import { SelectFilterOption } from '../../../../../core/models/select-option.interface';
import { UserModel } from '../../../../../core/models/user.interface';
import { TableColumn } from '../../../../../core/models/table.interface';
import { Table, TableAction } from "../../../../../shared/ui/table/table";
import { Pagination } from "../../../../../shared/ui/pagination/pagination";

@Component({
  selector: 'app-user-status',
  imports: [BreadCrumb, PageHeader, SelectFilter, Table, Pagination],
  templateUrl: './user-status.html',
  styleUrl: './user-status.css',
})
export class UserStatus {


  private readonly adminService = inject(AdminService);
  readonly icon = "manage_accounts";
  readonly title = "Estado de cuentas";
  readonly subtitle = "Gestión del estado de las cuentas de usuario (activas, bloqueadas e inactivas)";
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly users = signal<UserModel[]>([]);
  readonly totalItems = signal(0);
  readonly currentPage = signal(1);
  readonly pageSize = signal(5);
  readonly searchTerm = signal('');
  readonly statusFilter = signal('');
  readonly statusOptions: SelectFilterOption[] = [
    {
      value: '',
      label: 'Todos los estados'
    },
    {
      value: 'active',
      label: 'Activo'
    },
    {
      value: 'inactive',
      label: 'Inactivo'
    }
  ];

  async ngOnInit(): Promise<void> {
    await this.initUser();
    this.loadUsers()
  }

  private async initUser(): Promise<void> {
    this.breadcrumbs.set([
      {
        label: 'Inicio',
        href: '/admin'
      },
      {
        label: 'Usuarios'
      },
      {
        label: 'Estado de Cuentas'
      }
    ]);
  }

  readonly tableActions: TableAction[] = [
    'activate',
    'deactivate'
  ];


  loadUsers(): void {
    this.adminService.getAll("ACTIVE", this.currentPage() - 1, this.pageSize(), this.searchTerm()).subscribe({
      next: (res: any) => {

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
      },
    });

  }

  onStatusFilterChange(status: string): void {
    this.statusFilter.set(status);
    this.currentPage.set(1);
    this.loadUsers();
  }

  onPageChange(page: number): void {
    this.currentPage.set(page);
    this.loadUsers();
  }

  onPageSizeChange(size: number): void {
    this.pageSize.set(size);
    this.currentPage.set(1);
    this.loadUsers();
  }
  
  readonly columns: TableColumn[] = [
    {
      key: 'fullName',
      label: 'Nombre',
      sortable: true
    },
    {
      key: 'username',
      label: 'Usuario',
      sortable: true
    },
    {
      key: 'email',
      label: 'Correo',
      sortable: true
    },
    {
      key: 'status',
      label: 'Estado',
      width: '120px'
    }
  ];

  onDelete(user: UserModel): void {
    console.log(user);
  }
  onDeactivate($event: any) {
    throw new Error('Method not implemented.');
  }
  onActivate($event: any) {
    throw new Error('Method not implemented.');
  }
}
