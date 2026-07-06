import { Component, input, Input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { BreadcrumbItem } from '../../../core/models/bread-crumb.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-bread-crumb',
  standalone: true,
  imports: [RouterLink, MatIconModule, CommonModule],
  templateUrl: './bread-crumb.html',
  styleUrl: './bread-crumb.css',
})
export class BreadCrumb {
  readonly items = input.required<BreadcrumbItem[]>();
}