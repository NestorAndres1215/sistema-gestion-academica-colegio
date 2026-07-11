import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreadCrumb, } from '../../../shared/ui/bread-crumb/bread-crumb';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { PageHeader } from "../../../shared/ui/page-header/page-header";

@Component({
  selector: 'app-admin-main',
  standalone: true,
  imports: [
    CommonModule,
    BreadCrumb,


    PageHeader
  ],
  templateUrl: './admin-main.html'
})
export class AdminMain {

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