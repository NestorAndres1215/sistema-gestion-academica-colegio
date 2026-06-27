import { Component, EventEmitter, Input, Output } from '@angular/core';
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

  @Input() page = 1;
  @Input() totalPages = 1;
  @Input() pageSize = 10;

  @Output() pageChange = new EventEmitter<number>();

  get totalItems(): number {
    return this.totalPages * this.pageSize;
  }

  onPageChange(event: PageEvent): void {
    this.page = event.pageIndex + 1;
    this.pageChange.emit(this.page);
  }
}