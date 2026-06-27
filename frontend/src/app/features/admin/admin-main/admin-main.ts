import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BreadCrumb, BreadcrumbItem } from '../../../shared/ui/bread-crumb/bread-crumb';
import { SearchBar } from '../../../shared/ui/search-bar/search-bar';
import { Pagination } from '../../../shared/ui/pagination/pagination';
import { PageTitle } from '../../../shared/ui/page-title/page-title';
import { Button } from '../../../shared/ui/button/button';
import { DataTable } from '../../../shared/ui/data-table/data-table';

@Component({
  selector: 'app-admin-main',
  standalone: true,
  imports: [
    CommonModule,
    BreadCrumb,
    SearchBar,
    
    PageTitle,
    Button,
    
  ],
  templateUrl: './admin-main.html'
})
export class AdminMain {

  breadcrumbs: BreadcrumbItem[] = [
    {
      label: 'Dashboard',
      href: '/admin'
    },
    {
      label: 'Usuarios'
    }
  ];

  page = 1;
  pageSize = 5;

 

}