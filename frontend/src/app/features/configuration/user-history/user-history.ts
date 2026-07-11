import { Component, inject, signal, OnInit } from '@angular/core';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';
import { UserStoryService } from '../../../core/services/user-story.service';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { TableColumn } from '../../../core/models/table.interface';
import { Button } from "../../../shared/ui/button/button";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatDatepickerModule } from "@angular/material/datepicker";
import { Search } from "../../../shared/ui/search/search";
import { PageHeader } from "../../../shared/ui/page-header/page-header";
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { Table } from "../../../shared/ui/table/table";
import { CommonModule } from '@angular/common';
import { Pagination } from "../../../shared/ui/pagination/pagination";
import { FormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-user-history',
  templateUrl: './user-history.html',
  styleUrl: './user-history.css',
  standalone: true,
  imports: [CommonModule,
    FormsModule, // 👈 ESTO ES LO QUE TE FALTA
    MatDatepickerModule,
    MatFormFieldModule,
    MatInputModule,
    MatNativeDateModule,
    Search,
    BreadCrumb,
    Button,
    Table,
    Pagination,
    PageHeader]
})
export class UserHistory implements OnInit {

  private readonly authService = inject(AuthService);
  private readonly userStoryService = inject(UserStoryService);
  readonly icon = 'history';
  readonly title = 'Historial de actividad';
  readonly subtitle = 'Registro de acciones realizadas en el sistema';
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly userName = signal('');
  readonly logs = signal<any[]>([]);
  readonly totalItems = signal(0);
  readonly searchTerm = signal('');
  readonly dateFrom = signal<Date | null>(null);
  readonly dateTo = signal<Date | null>(null);
  readonly currentPage = signal(1);
  readonly pageSize = signal(10);
  readonly sort = signal<'asc' | 'desc'>('desc');

  readonly columns: TableColumn[] = [
    { key: 'action', label: 'Acción', sortable: true },
    { key: 'module', label: 'Módulo', sortable: true, width: '160px' },
    { key: 'detail', label: 'Descripción' },
    { key: 'createdAt', label: 'Fecha', sortable: true, width: '180px' },
  ];

  async ngOnInit(): Promise<void> {
    await this.initUser();
    this.loadHistory();
  }

  private async initUser(): Promise<void> {
    const user = await firstValueFrom(this.authService.getCurrentUser());

    this.userName.set(user.email);

    const homeRoute = this.authService.getHomeByRole(user.role);

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Usuarios' },
      { label: 'Historial de actividad' }
    ]);
  }

  async loadHistory(): Promise<void> {

    const filters = {
      email: this.userName(),
      page: this.currentPage(),
      size: this.pageSize(),
      sort: this.sort(),

      action: this.searchTerm() || null,
      status: null,

      dateFrom: this.dateFrom(),
      dateTo: this.dateTo()
    };
    const userStorys = await firstValueFrom(this.userStoryService.findWithFilters(filters));

    this.logs.set(userStorys.content);
    this.totalItems.set(userStorys.totalElements);

  }

  clearDateFilters() {
    this.dateFrom.set(null);
    this.dateTo.set(null);
    this.loadHistory();
  }

  toggleSortByDate() {
    this.sort.set(this.sort() === 'asc' ? 'desc' : 'asc');
    this.loadHistory();
  }

  onPageChange(page: number) {
    this.currentPage.set(page - 1);
    this.loadHistory();
  }

  onPageSizeChange(size: number) {
    this.pageSize.set(size);
    this.currentPage.set(0);
    this.loadHistory();
  }

  onSearch(term: string) {
    this.searchTerm.set(term);
    this.currentPage.set(0);
    this.loadHistory();
  }

  onDateFromChange(date: Date | null) {
    this.dateFrom.set(date);
    this.currentPage.set(0);
    this.loadHistory();
  }

  onDateToChange(date: Date | null) {
    this.dateTo.set(date);
    this.currentPage.set(0);
    this.loadHistory();
  }
}