import { Component } from '@angular/core';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../shared/ui/page-header/page-header";
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';

@Component({
  selector: 'app-admin-dashboard',
  imports: [BreadCrumb, PageHeader],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard {

    readonly icon = "admin_panel_settings";
    readonly title = "Panel de administración";
    readonly subtitle = "Gestiona y supervisa las operaciones del sistema";
  
    breadcrumbs: BreadcrumbItem[] = [
      {
        label: 'Dashboard',
        href: '/admin'
      },
      {
        label: 'Usuarios'
      }
    ];
  
}
