import {
  Component,
  TemplateRef,
  ContentChild,
  input,
  output,
  computed
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { TableColumn } from '../../../core/models/table.interface';


export type TableAction =
  | 'detail'
  | 'edit'
  | 'delete'
  | 'activate'
  | 'blocked'
  | 'deactivate'
  | 'print';


@Component({
  selector: 'app-table',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    MatCheckboxModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './table.html',
  styleUrl: './table.css'
})
export class Table<T extends Record<string, any>> {


  // ======================
  // Inputs
  // ======================

  readonly actions = input<TableAction[]>([
    'detail',
    'edit',
    'delete'
  ]);

  readonly columns = input<TableColumn[]>([]);

  readonly data = input<T[]>([]);

  readonly emptyMessage = input('No se encontraron registros');

  readonly trackByKey = input('id');


  readonly detailTooltip = input('Ver detalle');

  readonly editTooltip = input('Actualizar');

  readonly deleteTooltip = input('Eliminar');

  readonly activateTooltip = input('Activar');

  readonly deactivateTooltip = input('Desactivar');

  readonly blockedTooltip = input('Bloquear');

  readonly printTooltip = input('Imprimir');


  // --- Selección múltiple ---
  readonly selectable = input(false);

  readonly selectedIds = input<Set<any>>(new Set());


  // ======================
  // Outputs
  // ======================

  readonly rowClick = output<T>();

  readonly detail = output<T>();

  readonly edit = output<T>();

  readonly delete = output<T>();

  readonly activate = output<T>();

  readonly deactivate = output<T>();

  readonly blocked = output<T>();

  readonly print = output<T>();

  readonly selectionChange = output<Set<any>>();


  // ======================
  // Templates
  // ======================

  @ContentChild('actionsTemplate')
  actionsTemplate?: TemplateRef<unknown>;


  @ContentChild('cellTemplate')
  cellTemplate?: TemplateRef<unknown>;



  // ======================
  // Computed
  // ======================

  readonly hasActionsColumn = computed(() =>
    !!this.actionsTemplate ||
    this.actions().length > 0
  );


  readonly colspanTotal = computed(() =>
    this.columns().length +
    (this.hasActionsColumn() ? 1 : 0) +
    (this.selectable() ? 1 : 0)
  );


  readonly allSelected = computed(() => {
    const rows = this.data();
    const ids = this.selectedIds();
    return rows.length > 0 && rows.every(row => ids.has(this.rowId(row)));
  });


  readonly someSelected = computed(() => {
    const rows = this.data();
    const ids = this.selectedIds();
    return rows.some(row => ids.has(this.rowId(row))) && !this.allSelected();
  });



  // ======================
  // Helpers
  // ======================

  hasAction(action: TableAction): boolean {
    return this.actions().includes(action);
  }


  rowId(row: T): any {
    return row[this.trackByKey()];
  }


  isRowSelected(row: T): boolean {
    return this.selectedIds().has(this.rowId(row));
  }



  // ======================
  // Events
  // ======================

  onRowClick(row: T): void {
    this.rowClick.emit(row);
  }


  onDetail(row: T, event: Event): void {
    event.stopPropagation();
    this.detail.emit(row);
  }


  onEdit(row: T, event: Event): void {
    event.stopPropagation();
    this.edit.emit(row);
  }


  onDelete(row: T, event: Event): void {
    event.stopPropagation();
    this.delete.emit(row);
  }


  onActivate(row: T, event: Event): void {
    event.stopPropagation();
    this.activate.emit(row);
  }


  onDeactivate(row: T, event: Event): void {
    event.stopPropagation();
    this.deactivate.emit(row);
  }

  onBlocked(row: T, event: Event): void {
    event.stopPropagation();
    this.blocked.emit(row);
  }

  onPrint(row: T, event: Event): void {
    event.stopPropagation();
    this.print.emit(row);
  }


  toggleRow(row: T, event: Event): void {
    event.stopPropagation();

    const id = this.rowId(row);
    const next = new Set(this.selectedIds());

    if (next.has(id)) {
      next.delete(id);
    } else {
      next.add(id);
    }

    this.selectionChange.emit(next);
  }


  toggleAll(event: Event): void {
    event.stopPropagation();

    const next = new Set(this.selectedIds());

    if (this.allSelected()) {
      this.data().forEach(row => next.delete(this.rowId(row)));
    } else {
      this.data().forEach(row => next.add(this.rowId(row)));
    }

    this.selectionChange.emit(next);
  }

  trackByFn(index: number, item: T): unknown {
    return item[this.trackByKey()] ?? index;
  }

}