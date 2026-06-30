import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';

@Component({
  selector: 'app-bread-crumb',
  imports: [RouterLink, MatIconModule],
  templateUrl: './bread-crumb.html',
  styleUrl: './bread-crumb.css',
})
export class BreadCrumb {

  @Input({ required: true })
  items: BreadcrumbItem[] = [];

}
