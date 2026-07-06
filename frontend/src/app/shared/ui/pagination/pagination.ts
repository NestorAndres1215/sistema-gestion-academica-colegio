import { Component, computed, input, output, signal, effect } from '@angular/core';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [MatSelectModule, MatFormFieldModule],
  templateUrl: './pagination.html',
  styleUrl: './pagination.css',
})
export class Pagination {

  readonly totalItems = input.required<number>();
  readonly currentPage = input<number>(1);
  readonly pageSize = input<number>(10);

  readonly pageChange = output<number>();
  readonly pageSizeChange = output<number>();

  readonly pageSizeOptions = [5, 10, 20, 50];

  // Signal interno sincronizado con el input, para garantizar
  // que mat-select siempre tenga un valor numérico válido y reactivo.
  readonly selectedSize = signal<number>(10);

  readonly totalPages = computed(() => {
    const size = this.pageSize();
    if (size <= 0) return 1;
    return Math.max(1, Math.ceil(this.totalItems() / size));
  });

  constructor() {
    effect(() => {
      this.selectedSize.set(this.pageSize());
    });
  }

  prev(): void {
    const page = this.currentPage();
    if (page > 1) {
      this.pageChange.emit(page - 1);
    }
  }

  next(): void {
    const page = this.currentPage();
    if (page < this.totalPages()) {
      this.pageChange.emit(page + 1);
    }
  }

  onPageSizeSelect(value: number): void {
    this.selectedSize.set(value);
    this.pageSizeChange.emit(value);
  }

  compareSize = (a: number, b: number): boolean => Number(a) === Number(b);
}