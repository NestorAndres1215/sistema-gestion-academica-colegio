import { CommonModule } from '@angular/common';
import { Component, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-search-bar',
  imports: [
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
})
export class SearchBar {
  readonly searchText = signal('');
  readonly isFocused = signal(false);

  readonly searchChange = output<string>();

  onInputChange(value: string): void {
    this.searchText.set(value);
    this.searchChange.emit(value);
  }

  clearSearch(): void {
    this.searchText.set('');
    this.searchChange.emit('');
  }
}