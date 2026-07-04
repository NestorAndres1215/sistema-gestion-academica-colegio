import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BreadCrumb,  } from '../../../shared/ui/bread-crumb/bread-crumb';
import { SearchBar } from '../../../shared/ui/search-bar/search-bar';
import { Button } from '../../../shared/ui/button/button';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';

@Component({
  selector: 'app-admin-main',
  standalone: true,
  imports: [
    CommonModule,
    BreadCrumb,
    SearchBar,
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



}