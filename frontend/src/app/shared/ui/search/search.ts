import { Component, input, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { toObservable } from '@angular/core/rxjs-interop';
import { debounceTime, distinctUntilChanged } from 'rxjs';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search {

  readonly placeholder = input<string>('Buscar...');
  readonly debounceMs = input<number>(300);
  readonly searchChange = output<string>();
  readonly value = signal<string>('');

  constructor() {
    const value$ = toObservable(this.value);

    value$
      .pipe(
        debounceTime(this.debounceMs()),
        distinctUntilChanged()
      )
      .subscribe(term => {
        this.searchChange.emit(term.trim());
      });
  }

  onInputChange(term: string): void {
    this.value.set(term);
  }

  clear(): void {
    this.value.set('');
    this.searchChange.emit('');
  }
}