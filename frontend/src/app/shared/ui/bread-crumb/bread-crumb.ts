import { Component, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-bread-crumb',
  standalone: true, // 🔥 ESTO ES LO QUE TE FALTA
  imports: [RouterLink, MatIconModule, CommonModule],
  templateUrl: './bread-crumb.html',
  styleUrl: './bread-crumb.css',
})
export class BreadCrumb {

  @Input({ required: true })
  items: BreadcrumbItem[] = [];
}