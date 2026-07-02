import { CommonModule } from '@angular/common';
import { Component, EventEmitter, input, Input, output, Output } from '@angular/core';

@Component({
  selector: 'app-button',
  imports: [CommonModule],
  templateUrl: './button.html',
  styleUrl: './button.css',
})
export class Button {
 readonly label = input('');
  readonly color = input('');
  readonly size = input('md');
  readonly icon = input('');
  readonly iconPosition = input<'left' | 'right'>('left');
  readonly block = input(false);
  readonly loading = input(false);
  readonly disabled = input(false);

  readonly onClick = output<void>();

  handleClick(): void {
    if (!this.disabled() && !this.loading()) {
      this.onClick.emit();
    }
  }
}