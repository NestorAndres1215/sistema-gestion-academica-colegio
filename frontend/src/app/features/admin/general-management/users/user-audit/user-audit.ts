import { Component, inject, signal } from '@angular/core';
import { firstValueFrom } from 'rxjs';
import { TableColumn } from '../../../../../core/models/table.interface';
import { TableAction, Table } from '../../../../../shared/ui/table/table';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { AlertService } from '../../../../../core/services/alert.service';
import { Pagination } from "../../../../../shared/ui/pagination/pagination";
import { Search } from "../../../../../shared/ui/search/search";
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { SearchResultItem } from '../../../../../core/models/search-result.interface';
import { SearchResult, SearchResultAction } from "../../../../../shared/ui/search-result/search-result";
import { Button } from "../../../../../shared/ui/button/button";
import { SessionService } from '../../../../../core/services/session.service';
import { formatDate } from '../../../../../core/utils/date.util';
import { AuthService } from '../../../../../core/services/auth.service';
export interface SessionModel {
  id: string;
  userFullName: string;
  email: string;
  ipAddress: string;
  device: string;
  loginAt: string;
  lastActivityAt: string;
  status: 'activa' | 'expirada';
}
@Component({
  selector: 'app-user-audit',
  imports: [Pagination, Search, BreadCrumb, PageHeader, SearchResult],
  templateUrl: './user-audit.html',
  styleUrl: './user-audit.css',
})
export class UserAudit {
  private readonly sessionService = inject(SessionService);
  private readonly authService = inject(AuthService);
  private readonly alertService = inject(AlertService);

  readonly icon = 'security';
  readonly title = 'Auditoría de sesiones';
  readonly subtitle = 'Usuarios con sesión activa en el sistema';

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly sessions = signal<SearchResultItem[]>([]);
  readonly totalItems = signal(0);
  readonly currentPage = signal(1);
  readonly pageSize = signal(10);
  readonly searchTerm = signal('');
  readonly loading = signal(false);

  readonly sessionAction: SearchResultAction[] = ['closeSession'];

  // Como getAll() puede devolver sesiones expiradas mezcladas con activas,
  // solo se puede cerrar una sesión que efectivamente está activa
  isActionDisabled = (action: SearchResultAction, item: SearchResultItem): boolean => {
    if (action === 'closeSession') return item.status !== 'activa';
    return false;
  };

  async ngOnInit(): Promise<void> {
    await this.initHeader();
    await this.loadSessions();
  }

  private async initHeader(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Auditoría de sesiones' }
    ]);
  }

  async loadSessions(): Promise<void> {
    this.loading.set(true);

    try {
      const res: any = await firstValueFrom(
        this.sessionService.getAll(
          this.currentPage() - 1,
          this.pageSize(),
          this.searchTerm()
        )
      );

      this.sessions.set(
        res.content.map((s: any) => ({
          sessionId: s.sessionId,
          name: s.email,
          subtitle: s.username,
          role: formatDate(s.loginAt),
          userId: s.userId
        }))
      );


      this.totalItems.set(res.totalElements);

    } finally {
      this.loading.set(false);
    }
  }

  async onSearch(term: string): Promise<void> {
    this.searchTerm.set(term);
    this.currentPage.set(1);
    await this.loadSessions();
  }

  async onPageChange(page: number): Promise<void> {
    this.currentPage.set(page);
    await this.loadSessions();
  }

  async onPageSizeChange(size: number): Promise<void> {
    this.pageSize.set(size);
    this.currentPage.set(1);
    await this.loadSessions();
  }

  async onCloseSession(item: SearchResultItem): Promise<void> {
    const confirmed = await this.alertService.confirm(
      `¿Cerrar la sesión de ${item.name}?`,
      'El usuario deberá volver a iniciar sesión para continuar.'
    );

    if (!confirmed) {
      this.alertService.info('Acción cancelada', `No se cerró la sesión de ${item.name}.`);
      return;
    }
    console.log(item)
    try {
      await firstValueFrom(this.authService.logoutSession(item.id));
      this.alertService.success('Sesión cerrada', `La sesión de ${item.name} fue cerrada correctamente.`);
      await this.loadSessions();
    } catch {
      this.alertService.error('Error', 'No se pudo cerrar la sesión.');
    }
  }
}
