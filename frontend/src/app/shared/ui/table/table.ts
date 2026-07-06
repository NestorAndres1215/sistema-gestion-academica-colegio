import {
  Component,
  TemplateRef,
  ContentChild,
  input,
  output,
  signal,
  computed
} from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { TableColumn } from '../../../core/models/table-column.interface';


@Component({
  selector: 'app-table',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './table.html',
  styleUrl: './table.css'
})
export class Table<T extends Record<string, any>> {

  readonly columns = input<TableColumn[]>([]);
  readonly data = input<T[]>([]);

  readonly emptyMessage = input('No se encontraron registros');
  readonly trackByKey = input('id');
  readonly showDetail = input(true);
  readonly showEdit = input(true);
  readonly showDelete = input(true);
  readonly editTooltip = input('Actualizar');
  readonly deleteTooltip = input('Eliminar');
  readonly detailTooltip = input('Ver detalle');
  readonly sortChange = output<{ key: string; direction: 'asc' | 'desc' }>();
  readonly rowClick = output<T>();
  readonly detail = output<any>();
  readonly edit = output<T>();
  readonly delete = output<T>();


  @ContentChild('actionsTemplate')
  actionsTemplate?: TemplateRef<unknown>;

  @ContentChild('cellTemplate')
  cellTemplate?: TemplateRef<unknown>;

  readonly sortKey = signal('');
  readonly sortDirection = signal<'asc' | 'desc'>('asc');

  readonly hasActionsColumn = computed(() =>
    !!this.actionsTemplate || this.showEdit() || this.showDelete()
  );

  readonly colspanTotal = computed(() =>
    this.columns().length + (this.hasActionsColumn() ? 1 : 0)
  );

  onSort(column: TableColumn): void {
    if (!column.sortable) return;

    if (this.sortKey() === column.key) {
      this.sortDirection.set(
        this.sortDirection() === 'asc' ? 'desc' : 'asc'
      );
    } else {
      this.sortKey.set(column.key);
      this.sortDirection.set('asc');
    }

    this.sortChange.emit({
      key: this.sortKey(),
      direction: this.sortDirection()
    });
  }

  onRowClick(row: T): void {
    this.rowClick.emit(row);
  }

  onEdit(row: T, event: Event): void {
    event.stopPropagation();
    this.edit.emit(row);
  }

  onDelete(row: T, event: Event): void {
    event.stopPropagation();
    this.delete.emit(row);
  }

  onDetail(row: any, event: MouseEvent): void {
    event.stopPropagation();
    this.detail.emit(row);
  }

  trackByFn(index: number, item: T): unknown {
    return item[this.trackByKey()] ?? index;
  }
}