import { Component, input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-page-title',
  imports: [MatCardModule],
  templateUrl: './page-title.html',
  styleUrl: './page-title.css',
})
export class PageTitle {
  readonly title = input('Título por defecto');
  readonly icon = input('fas fa-crown');
}