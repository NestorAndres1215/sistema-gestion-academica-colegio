import { Component, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { BreadcrumbItem } from '../../../core/models/bread-crumb.interface';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';
import { PageHeader } from "../../../shared/ui/page-header/page-header";

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [BreadCrumb, PageHeader],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings {

  private readonly authService = inject(AuthService);
  readonly icon = "settings";
  readonly title = "Configuración";
  readonly subtitle = "Gestiona las preferencias y ajustes del sistema";
  breadcrumbs = signal<BreadcrumbItem[]>([]);

  username = '';
  roleName = '';

  async ngOnInit(): Promise<void> {

    const user = await firstValueFrom(this.authService.getCurrentUser());

    if (!user) return;

    this.username = user.username ?? '';
    this.roleName = user.roles;

    const homeRoute = this.authService.getHomeByRole(this.roleName);

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Configuración' }
    ]);
  }
}