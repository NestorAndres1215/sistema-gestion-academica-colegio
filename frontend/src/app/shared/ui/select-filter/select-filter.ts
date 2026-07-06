import { Component, input, output } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SelectFilterOption } from '../../../core/models/select-option.interface';



@Component({
  selector: 'app-select-filter',
  standalone: true,
  imports: [MatSelectModule, MatFormFieldModule],
  templateUrl: './select-filter.html',
  styleUrl: './select-filter.css',
})
export class SelectFilter {

  readonly placeholder = input<string>('Seleccionar');
  readonly options = input.required<SelectFilterOption[]>();
  readonly value = input<string>('');

  readonly valueChange = output<string>();

  onSelect(value: string): void {
    this.valueChange.emit(value);
  }

  compareValue = (a: string, b: string): boolean => a === b;
}