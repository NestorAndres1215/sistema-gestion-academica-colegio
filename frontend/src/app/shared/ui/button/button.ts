
import { Component, input, output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from "@angular/material/icon";
import { MatTooltipModule } from '@angular/material/tooltip';
export type ButtonVariant = 'primary' | 'secondary' | 'danger' | 'success' | 'variant-primary';
export type ButtonShape = 'default' | 'icon';
@Component({
  selector: 'app-button',
  imports: [
    MatButtonModule,
    MatIconModule,
    MatTooltipModule
  ],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {

  readonly label = input<string>('');
  readonly icon = input<string>('');
  readonly iconPosition = input<'left' | 'right'>('left');
  readonly variant = input<ButtonVariant>('primary');
  readonly shape = input<ButtonShape>('default'); 
  readonly tooltip = input<string>('');
  readonly disabled = input<boolean>(false);
  readonly clicked = output<void>();

  onClick(): void {
    this.clicked.emit();
  }
}