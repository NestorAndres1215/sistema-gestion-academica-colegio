import { Component, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { Router } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { AdminService } from '../../../../../core/services/admin.service';
import { AuthService } from '../../../../../core/services/auth.service';
import { Search } from '../../../../../shared/ui/search/search';
import {
  SearchResult,
  SearchResultAction,
} from '../../../../../shared/ui/search-result/search-result';
import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { PageHeader } from '../../../../../shared/ui/page-header/page-header';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { SearchResultItem } from '../../../../../core/models/search-result.interface';
import { UserModel } from '../../../../../core/models/user.interface';

@Component({
  selector: 'app-user-advanced-search',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatIconModule,
    MatCheckboxModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,

    BreadCrumb,
    PageHeader,
    Search,
    SearchResult,
  ],
  templateUrl: './user-advanced-search.html',
  styleUrl: './user-advanced-search.css',
})
export class UserAdvancedSearch {

  private readonly adminService = inject(AdminService);
  private readonly router = inject(Router);
  
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly results = signal<SearchResultItem[]>([]);
  readonly currentQuery = signal('');

  readonly icon = 'person_search';
  readonly title = 'Búsqueda avanzada de usuarios';
  readonly subtitle = 'Encuentra usuarios utilizando múltiples criterios de búsqueda.';

  readonly sessionAction: SearchResultAction[] = ['message', 'viewProfile'];

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  private async initUser(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Búsqueda Avanzada de Usuarios' },
    ]);
  }

  async loadUsers(): Promise<void> {
    const query = this.currentQuery().trim();

    if (!query) {
      this.results.set([]);
      return;
    }

    const admin = await firstValueFrom(this.adminService.search(query));

    const searchItems: SearchResultItem[] = admin.map((user) => ({
      id: String(user.id),
      name: `${user.firstName} ${user.paternalLastName}`,
      title: `${user.firstName} ${user.paternalLastName}`,
      subtitle: user.email,
      avatar: user.profile,
      description: user.status === 'ACTIVE' ? 'Activo' : 'Inactivo',
    }));

    this.results.set(searchItems);
  }

  onSearchChange(term: string): void {
    this.currentQuery.set(term);
    this.loadUsers();
  }

  onMessage(item: SearchResultItem): void {
    this.router.navigate(['/mensajes', item.id]);
  }

  onViewProfile(item: SearchResultItem): void {
    this.router.navigate(['/usuarios', item.id]);
  }
}
