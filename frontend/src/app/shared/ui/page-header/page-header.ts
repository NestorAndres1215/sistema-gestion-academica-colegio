import { CommonModule } from '@angular/common';
import { Component, input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-page-header',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule
  ],
  templateUrl: './page-header.html',
  styleUrl: './page-header.css',
})
export class PageHeader {

  readonly icon = input('description');
  readonly title = input.required<string>();
  readonly subtitle = input('');

}
