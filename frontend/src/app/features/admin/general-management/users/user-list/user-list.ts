import { Component, inject, signal, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { AuthService } from '../../../../../core/services/auth.service';

import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { Search } from '../../../../../shared/ui/search/search';
import { Table, TableColumn } from '../../../../../shared/ui/table/table';
import { Pagination } from '../../../../../shared/ui/pagination/pagination';
import { SelectFilter, SelectFilterOption } from '../../../../../shared/ui/select-filter/select-filter';
import { MatChipsModule } from '@angular/material/chips';

export interface UserModel {
  id: string;
  name: string;
  email: string;
  role: string;
  status: 'activo' | 'inactivo';
  createdAt?: string;
}

@Component({
  selector: 'app-user-list',
  imports: [BreadCrumb, Search, Table, Pagination, MatChipsModule, SelectFilter],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {

  private readonly authService = inject(AuthService);

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly loading = signal(false);

  // Opciones del filtro de estado (modelo dentro del propio componente)
  readonly statusOptions: SelectFilterOption[] = [
    { value: '', label: 'Todos los estados' },
    { value: 'activo', label: 'Activo' },
    { value: 'inactivo', label: 'Inactivo' },
  ];

  // Base de datos simulada
  readonly allUsers = signal<UserModel[]>([
    {
      id: '1',
      name: 'Juan Pérez',
      email: 'juan.perez@colegio.edu.pe',
      role: 'Administrador',
      status: 'activo',
      createdAt: '2026-07-01'
    },
    {
      id: '2',
      name: 'María López',
      email: 'maria.lopez@colegio.edu.pe',
      role: 'Director',
      status: 'activo',
      createdAt: '2026-07-02'
    },
    {
      id: '3',
      name: 'Carlos Sánchez',
      email: 'carlos.sanchez@colegio.edu.pe',
      role: 'Profesor',
      status: 'activo',
      createdAt: '2026-07-03'
    },
    {
      id: '4',
      name: 'Ana Torres',
      email: 'ana.torres@colegio.edu.pe',
      role: 'Secretaria',
      status: 'activo',
      createdAt: '2026-07-03'
    },
    {
      id: '5',
      name: 'Luis Ramírez',
      email: 'luis.ramirez@colegio.edu.pe',
      role: 'Profesor',
      status: 'inactivo',
      createdAt: '2026-07-04'
    },
    {
      id: '6',
      name: 'Sofía Mendoza',
      email: 'sofia.mendoza@colegio.edu.pe',
      role: 'Tesorería',
      status: 'activo',
      createdAt: '2026-07-04'
    },
    {
      id: '7',
      name: 'Pedro Castillo',
      email: 'pedro.castillo@colegio.edu.pe',
      role: 'Coordinador',
      status: 'activo',
      createdAt: '2026-07-05'
    },
    {
      id: '8',
      name: 'Lucía Vargas',
      email: 'lucia.vargas@colegio.edu.pe',
      role: 'Psicóloga',
      status: 'activo',
      createdAt: '2026-07-05'
    },
    {
      id: '9',
      name: 'Miguel Herrera',
      email: 'miguel.herrera@colegio.edu.pe',
      role: 'Auxiliar',
      status: 'inactivo',
      createdAt: '2026-07-05'
    },
    {
      id: '10',
      name: 'Valeria Flores',
      email: 'valeria.flores@colegio.edu.pe',
      role: 'Administrador',
      status: 'activo',
      createdAt: '2026-07-05'
    },
    {
      id: '11',
      name: 'Diego Romero',
      email: 'diego.romero@colegio.edu.pe',
      role: 'Profesor',
      status: 'activo',
      createdAt: '2026-07-06'
    },
    {
      id: '12',
      name: 'Camila Rojas',
      email: 'camila.rojas@colegio.edu.pe',
      role: 'Secretaria',
      status: 'activo',
      createdAt: '2026-07-06'
    },
    {
      id: '13',
      name: 'Jorge Medina',
      email: 'jorge.medina@colegio.edu.pe',
      role: 'Auxiliar',
      status: 'inactivo',
      createdAt: '2026-07-06'
    },
    {
      id: '14',
      name: 'Patricia Salazar',
      email: 'patricia.salazar@colegio.edu.pe',
      role: 'Director',
      status: 'activo',
      createdAt: '2026-07-07'
    },
    {
      id: '15',
      name: 'Fernando Gutiérrez',
      email: 'fernando.gutierrez@colegio.edu.pe',
      role: 'Administrador',
      status: 'activo',
      createdAt: '2026-07-07'
    }
  ]);

  // Datos que muestra la tabla
  readonly users = signal<UserModel[]>([]);
  readonly totalItems = signal(0);

  readonly columns: TableColumn[] = [
    { key: 'name', label: 'Nombre', sortable: true },
    { key: 'email', label: 'Correo', sortable: true },
    { key: 'role', label: 'Rol', sortable: true },
    { key: 'status', label: 'Estado', width: '120px' }
  ];

  // Estado de búsqueda / filtro / orden / paginación como signals
  readonly searchTerm = signal('');
  readonly statusFilter = signal('');
  readonly currentPage = signal(1);
  readonly pageSize = signal(10);
  readonly sortKey = signal('');
  readonly sortDirection = signal<'asc' | 'desc'>('asc');

  async ngOnInit(): Promise<void> {
    await this.initUser();
    await this.loadUsers();
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
        label: 'Listado Usuarios'
      }
    ]);
  }

  async loadUsers(): Promise<void> {
    this.loading.set(true);

    try {
      await new Promise(resolve => setTimeout(resolve, 300));

      let data = [...this.allUsers()];

      // Buscar
      const term = this.searchTerm().trim().toLowerCase();
      if (term) {
        data = data.filter(user =>
          user.name.toLowerCase().includes(term) ||
          user.email.toLowerCase().includes(term) ||
          user.role.toLowerCase().includes(term)
        );
      }

      // Filtrar por estado
      const status = this.statusFilter();
      if (status) {
        data = data.filter(user => user.status === status);
      }

      // Ordenar
      const key = this.sortKey();
      if (key) {
        const direction = this.sortDirection();

        data.sort((a: any, b: any) => {
          const valueA = a[key];
          const valueB = b[key];

          if (valueA < valueB) return direction === 'asc' ? -1 : 1;
          if (valueA > valueB) return direction === 'asc' ? 1 : -1;
          return 0;
        });
      }

      this.totalItems.set(data.length);

      // Paginación
      const page = this.currentPage();
      const size = this.pageSize();
      const start = (page - 1) * size;
      const end = start + size;

      this.users.set(data.slice(start, end));

    } finally {
      this.loading.set(false);
    }
  }

  onSearch(term: string): void {
    this.searchTerm.set(term);
    this.currentPage.set(1);
    this.loadUsers();
  }

  onStatusFilterChange(status: string): void {
    this.statusFilter.set(status);
    this.currentPage.set(1);
    this.loadUsers();
  }

  onSortChange(sort: { key: string; direction: 'asc' | 'desc' }): void {
    this.sortKey.set(sort.key);
    this.sortDirection.set(sort.direction);
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

  onCreate(): void {
    console.log('Crear');
  }

  onEdit(user: UserModel): void {
    console.log('Editar', user);
  }

  async onDelete(user: UserModel): Promise<void> {
    console.log('Eliminar', user);
  }
}