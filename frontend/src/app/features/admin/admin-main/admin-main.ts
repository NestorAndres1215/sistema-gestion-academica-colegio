import { Component } from '@angular/core';
import { BreadCrumb, BreadcrumbItem } from '../../../shared/ui/bread-crumb/bread-crumb';
import { CommonModule } from '@angular/common';
import { SearchBar } from "../../../shared/ui/search-bar/search-bar";
import { Pagination } from "../../../shared/ui/pagination/pagination";
import { PageTitle } from "../../../shared/ui/page-title/page-title";
import { Button } from "../../../shared/ui/button/button";

@Component({
  selector: 'app-admin-main',
  standalone: true,
  imports: [CommonModule, BreadCrumb, SearchBar, Pagination, PageTitle, Button],
  templateUrl: './admin-main.html'
})
export class AdminMain {
filter($event: string) {
throw new Error('Method not implemented.');
}
  page = 1;
  totalPages = 10;

  onPageChange(page: number) {
    this.page = page;
    // Cargar datos de la página
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