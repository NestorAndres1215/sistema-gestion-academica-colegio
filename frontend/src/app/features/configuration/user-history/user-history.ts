import { Component, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { PageHeader } from "../../../shared/ui/page-header/page-header";
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-user-history',
  imports: [BreadCrumb, PageHeader],
  templateUrl: './user-history.html',
  styleUrl: './user-history.css',
})
export class UserHistory {

  readonly icon = "history";
  readonly title = "Historial de usuario";
  readonly subtitle = "Consulta las actividades y acciones realizadas en el sistema";
  
  private readonly authService = inject(AuthService);
  breadcrumbs = signal<BreadcrumbItem[]>([]);

  async ngOnInit(): Promise<void> {
    await this.initUser();
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
}
