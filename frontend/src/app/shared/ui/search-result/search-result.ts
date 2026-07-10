import { Component, input, output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { SearchResultItem } from '../../../core/models/search-result.interface';


@Component({
  selector: 'app-search-result',
  standalone: true,
  imports: [MatIconModule, MatButtonModule],
  templateUrl: './search-result.html',
  styleUrl: './search-result.css'
})
export class SearchResult {

  readonly items = input<SearchResultItem[]>([]);
  readonly loading = input<boolean>(false);
  readonly emptyMessage = input<string>('No se encontraron resultados');
  readonly message = output<SearchResultItem>();
  readonly viewProfile = output<SearchResultItem>();

  getInitial(name: string): string {
    return name?.charAt(0).toUpperCase() ?? '';
  }

  onMessage(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    this.message.emit(item);
  }

  onViewProfile(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    this.viewProfile.emit(item);
  }

  trackByFn(index: number, item: SearchResultItem): string {
    return item.id ?? index.toString();
  }
}