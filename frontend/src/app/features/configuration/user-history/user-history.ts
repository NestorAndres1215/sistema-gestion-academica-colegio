import { Component } from '@angular/core';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';

@Component({
  selector: 'app-user-history',
  imports: [BreadCrumb],
  templateUrl: './user-history.html',
  styleUrl: './user-history.css',
})
export class UserHistory {

  breadcrumbs: BreadcrumbItem[] = [
    { label: 'Inicio', href: '/admin' },
    { label: 'Historial de Usuario' }
  ];

}
