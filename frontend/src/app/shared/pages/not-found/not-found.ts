import { Component, inject, signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { Router } from '@angular/router';
import { Button } from '../../ui/button/button';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [MatIcon, Button],
  templateUrl: './not-found.html',
  styleUrl: './not-found.css',
})
export class NotFound {
  private readonly router = inject(Router);

  readonly title = signal('Esta página no existe');

  readonly subtitle = signal('Revisa la dirección o vuelve al inicio del sistema.');

  readonly buttonLabel = signal('Ir al inicio');

  readonly buttonIcon = signal('home');

  goHome(): void {
    this.router.navigate(['/']);
  }
}
