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
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { TableColumn } from '../../../core/models/table.interface';


export type TableAction =
  | 'detail'
  | 'edit'
  | 'delete'
  | 'activate'
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

  readonly emptyMessage = input(
    'No se encontraron registros'
  );

  readonly trackByKey = input('id');


  readonly detailTooltip = input('Ver detalle');

  readonly editTooltip = input('Actualizar');

  readonly deleteTooltip = input('Eliminar');

  readonly activateTooltip = input('Activar');

  readonly deactivateTooltip = input('Desactivar');

  readonly printTooltip = input('Imprimir');


  // ======================
  // Outputs
  // ======================

  readonly rowClick = output<T>();

  readonly detail = output<T>();

  readonly edit = output<T>();

  readonly delete = output<T>();

  readonly activate = output<T>();

  readonly deactivate = output<T>();

  readonly print = output<T>();


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
    (this.hasActionsColumn() ? 1 : 0)
  );



  // ======================
  // Helpers
  // ======================

  hasAction(action: TableAction): boolean {
    return this.actions().includes(action);
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


  onPrint(row: T, event: Event): void {
    event.stopPropagation();
    this.print.emit(row);
  }



  // ======================
  // Track
  // ======================

  trackByFn(index: number, item: T): unknown {

    return item[this.trackByKey()] ?? index;

  }

}