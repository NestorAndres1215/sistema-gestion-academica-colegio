import { CommonModule } from '@angular/common';
import { Component, EventEmitter, input, Input, output, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from '@angular/material/tooltip';
export type ButtonVariant = 'primary' | 'secondary' | 'danger' | 'success' | 'variant-primary';
export type ButtonShape = 'default' | 'icon';
@Component({
  selector: 'app-button',
imports: [MatButtonModule, MatIconModule, MatTooltipModule],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {
// Contenido
  readonly label = input<string>('');
  readonly icon = input<string>('');
  readonly iconPosition = input<'left' | 'right'>('left');

  // Apariencia
  readonly variant = input<ButtonVariant>('primary');
  readonly shape = input<ButtonShape>('default'); // 'icon' = solo ícono, circular
  readonly tooltip = input<string>('');

  // Estado
  readonly disabled = input<boolean>(false);
  readonly loading = input<boolean>(false);

  readonly clicked = output<void>();

  onClick(): void {
    if (this.disabled() || this.loading()) return;
    this.clicked.emit();
  }
}