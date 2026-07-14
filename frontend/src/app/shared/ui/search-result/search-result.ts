import { Component, input, output } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { SearchResultItem } from '../../../core/models/search-result.interface';
import { Button } from "../button/button";
export type SearchResultAction =
  | 'message'
  | 'viewProfile'
  | 'activate'
  | 'deactivate'
  | 'blocked'
  | 'closeSession';

@Component({
  selector: 'app-search-result',
  standalone: true,
  imports: [MatIconModule, MatButtonModule, Button],
  templateUrl: './search-result.html',
  styleUrl: './search-result.css'
})
export class SearchResult {

 // ======================
  // Inputs
  // ======================

  readonly items = input<SearchResultItem[]>([]);

  readonly loading = input<boolean>(false);

  readonly emptyMessage = input<string>('No se encontraron resultados');

  readonly actions = input<SearchResultAction[]>([
    'message',
    'viewProfile',
    'closeSession'
  ]);

  readonly messageTooltip = input('Enviar mensaje');

  readonly viewProfileTooltip = input('Ver Perfil');

  readonly activateTooltip = input('Activar');

  readonly deactivateTooltip = input('Desactivar');

  readonly blockedTooltip = input('Bloquear');

  readonly closeSessionTooltip = input('Cerrar sesión');

  // Deshabilita una acción puntual según el item (botón visible pero gris)
  readonly actionDisabled = input<(action: SearchResultAction, item: SearchResultItem) => boolean>(
    () => false
  );


  // ======================
  // Outputs
  // ======================

  readonly message = output<SearchResultItem>();

  readonly viewProfile = output<SearchResultItem>();

  readonly activate = output<SearchResultItem>();

  readonly deactivate = output<SearchResultItem>();

  readonly blocked = output<SearchResultItem>();

  readonly closeSession = output<SearchResultItem>();


  // ======================
  // Helpers
  // ======================

  hasAction(action: SearchResultAction): boolean {
    return this.actions().includes(action);
  }

  isActionDisabled(action: SearchResultAction, item: SearchResultItem): boolean {
    return this.actionDisabled()(action, item);
  }

  getInitial(name: string): string {
    return name?.charAt(0).toUpperCase() ?? '';
  }


  // ======================
  // Events
  // ======================

  onMessage(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('message', item)) return;
    this.message.emit(item);
  }

  onViewProfile(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('viewProfile', item)) return;
    this.viewProfile.emit(item);
  }

  onActivate(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('activate', item)) return;
    this.activate.emit(item);
  }

  onDeactivate(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('deactivate', item)) return;
    this.deactivate.emit(item);
  }

  onBlocked(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('blocked', item)) return;
    this.blocked.emit(item);
  }

  onCloseSession(item: SearchResultItem, event: Event): void {
    event.stopPropagation();
    if (this.isActionDisabled('closeSession', item)) return;
    this.closeSession.emit(item);
  }


  // ======================
  // Track
  // ======================

  trackByFn(index: number, item: SearchResultItem): string {
    return item.id ?? index.toString();
  }
}