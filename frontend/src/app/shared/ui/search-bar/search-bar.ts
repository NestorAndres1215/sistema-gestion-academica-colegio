import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
@Component({
  selector: 'app-search-bar',
  imports: [    FormsModule,CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule],
  templateUrl: './search-bar.html',
  styleUrl: './search-bar.css',
})
export class SearchBar {

  searchText = '';
  isFocused = false;

  @Output() searchChange = new EventEmitter<string>();

  onInputChange(value: string) {
    this.searchText = value;
    this.searchChange.emit(this.searchText);
  }
  clearSearch() {
    this.searchText = '';
    this.onInputChange('');
  }
}
