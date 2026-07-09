import { Component, OnInit, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { BreadcrumbItem } from '../../../../../core/models/bread-crumb.interface';
import { AuthService } from '../../../../../core/services/auth.service';
import { AdminService } from '../../../../../core/services/admin.service';
import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { Search } from '../../../../../shared/ui/search/search';
import { Table } from '../../../../../shared/ui/table/table';
import { Pagination } from '../../../../../shared/ui/pagination/pagination';
import { SelectFilter } from '../../../../../shared/ui/select-filter/select-filter';
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { SelectFilterOption } from '../../../../../core/models/select-option.interface';
import { TableColumn } from '../../../../../core/models/table-column.interface';

export interface UserModel {
  id: number;
  username: string;
  firstName: string;
  middleName?: string;
  paternalLastName: string;
  maternalLastName: string;
  email: string;
  profile: string;
  role: string;
  status: 'ACTIVE' | 'INACTIVE';
}

@Component({
  selector: 'app-user-list',
  imports: [
    BreadCrumb,
    Search,
    Table,
    Pagination,
    SelectFilter,
    PageHeader
  ],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css'
})
export class UserList implements OnInit {

  private readonly authService = inject(AuthService);
  private readonly adminService = inject(AdminService);
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly users = signal<UserModel[]>([]);
  readonly totalItems = signal(0);
  readonly currentPage = signal(1);
  readonly pageSize = signal(5);
  readonly searchTerm = signal('');
  readonly statusFilter = signal('');
  readonly icon = "manage_accounts";
  readonly title = "Gestión de usuarios";
  readonly subtitle = "Búsqueda, filtros y administración de usuarios del sistema";


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

  readonly columns: TableColumn[] = [
    {
      key: 'fullName',
      label: 'Nombre',
      sortable: true
    },
    {
      key: 'birthDate',
      label: 'Nacimiento',
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

  async ngOnInit(): Promise<void> {
    await this.initUser();
    this.loadUsers();
  }

  private async initUser(): Promise<void> {

    const currentUser = await firstValueFrom(this.authService.getCurrentUser());

    this.breadcrumbs.set([
      {
        label: 'Inicio',
        href: this.authService.getHomeByRole(currentUser.role)
      },
      {
        label: 'Usuarios'
      },
      {
        label: 'Listado de Usuarios'
      }
    ]);
  }

  loadUsers(): void {

    const status = this.statusFilter() === '' ? '' : this.statusFilter().toUpperCase();

    this.adminService.getAll(status, this.currentPage() - 1, this.pageSize(), this.searchTerm()).subscribe({
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


  onSearch(term: string): void {
    console.log('onSearch:', term);
    this.searchTerm.set(term);
    this.currentPage.set(1);
    this.loadUsers();
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

  onRowClick(user: UserModel): void {
    console.log(user);
  }

  onDetail(user: UserModel): void {
    console.log(user);
  }

  onEdit(user: UserModel): void {
    console.log(user);
  }

  onDelete(user: UserModel): void {
    console.log(user);
  }

}