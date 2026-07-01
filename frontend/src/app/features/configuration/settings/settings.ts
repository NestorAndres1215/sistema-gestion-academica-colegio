import { Component, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [BreadCrumb],
  templateUrl: './settings.html',
  styleUrl: './settings.css',
})
export class Settings {

  private authService = inject(AuthService);

  breadcrumbs = signal<BreadcrumbItem[]>([]);

  username = '';
  roleName = '';

  async ngOnInit(): Promise<void> {

    const user = await firstValueFrom(this.authService.getCurrentUser());

    if (!user) return;

    this.username = user.username ?? '';
    this.roleName = user.roles?.[0]?.name ?? '';

    const homeRoute = this.authService.getHomeByRole(this.roleName);

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Configuración' }
    ]);
  }
}