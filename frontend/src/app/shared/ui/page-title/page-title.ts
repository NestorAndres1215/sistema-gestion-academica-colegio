import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
@Component({
  selector: 'app-page-title',
  imports: [MatCardModule],
  templateUrl: './page-title.html',
  styleUrl: './page-title.css',
})
export class PageTitle {
  @Input() title: string = 'Título por defecto';
  @Input() icon: string = 'fas fa-crown';
}
