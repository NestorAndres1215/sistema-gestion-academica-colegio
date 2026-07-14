import { Component, inject } from '@angular/core';
import { MatIcon } from "@angular/material/icon";
import { Router } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [MatIcon],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {

  private readonly router = inject(Router);

  goHome(): void {
    this.router.navigate(['/']);
  }

}
