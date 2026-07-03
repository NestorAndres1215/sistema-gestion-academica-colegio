import { Component, computed, EventEmitter, input, Input, output, Output } from '@angular/core';
import { MatPaginatorIntl, MatPaginatorModule, PageEvent } from '@angular/material/paginator';

function customPaginatorIntl() {
  const intl = new MatPaginatorIntl();
  intl.itemsPerPageLabel = 'Items por página:';
  intl.nextPageLabel = 'Siguiente';
  intl.previousPageLabel = 'Anterior';
  intl.firstPageLabel = 'Primera página';
  intl.lastPageLabel = 'Última página';
  intl.getRangeLabel = (page, pageSize, length) => {
    if (length === 0) return '0 de 0';
    const start = page * pageSize + 1;
    const end = Math.min((page + 1) * pageSize, length);
    return `${start} – ${end} de ${length}`;
  };
  return intl;
}

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [MatPaginatorModule],
  templateUrl: './pagination.html',
  styleUrl: './pagination.css',
  providers: [
    { provide: MatPaginatorIntl, useFactory: customPaginatorIntl }
  ]
})
export class Pagination {

  readonly page = input(1);
  readonly totalPages = input(1);
  readonly pageSize = input(10);

  readonly pageChange = output<number>();

  readonly totalItems = computed(() =>
    this.totalPages() * this.pageSize()
  );

  onPageChange(event: PageEvent): void {
    const newPage = event.pageIndex + 1;
    this.pageChange.emit(newPage);
  }
}