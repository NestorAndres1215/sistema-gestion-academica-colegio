import { Component } from '@angular/core';
import { BreadCrumb, BreadcrumbItem } from '../../../shared/ui/bread-crumb/bread-crumb';
import { CommonModule } from '@angular/common';
import { SearchBar } from "../../../shared/ui/search-bar/search-bar";

@Component({
  selector: 'app-admin-main',
  standalone: true,
  imports: [CommonModule, BreadCrumb, SearchBar],
  templateUrl: './admin-main.html'
})
export class AdminMain {
filter($event: string) {
throw new Error('Method not implemented.');
}

  breadcrumbs: BreadcrumbItem[] = [
    {
      label: 'Dashboard',
      href: '/admin'
    },
    {
      label: 'Usuarios',
      href: '/admin/users'
    },    {
      label: 'Dashboard',
      href: '/admin'
    },
    {
      label: 'Usuarios',
      href: '/admin/users'
    },
    {
      label: 'Nuevo Usuario'
    }
  ];

}