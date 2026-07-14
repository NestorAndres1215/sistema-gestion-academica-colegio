// src/app/core/utils/date.utils.ts

/**
 * Convierte "yyyy-MM-dd" a Date local
 * Evita el problema del desfase por zona horaria.
 */
export function toLocalDate(date: string | null | undefined): Date | null {
  if (!date) return null;

  const [year, month, day] = date.split('-').map(Number);

  return new Date(year, month - 1, day);
}

/**
 * Convierte un Date a "yyyy-MM-dd"
 */
export function toApiDate(date: Date | null | undefined): string | null {
  if (!date) return null;

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');

  return `${year}-${month}-${day}`;
}

/**
 * Obtiene únicamente el año.
 */
export function getYear(date: string | Date | null | undefined): string {
  if (!date) return '—';

  if (typeof date === 'string') {
    return String(toLocalDate(date)?.getFullYear() ?? '—');
  }

  return String(date.getFullYear());
}


export function parseDate(date: string | Date | null | undefined): Date | null {
  if (!date) return null;

  if (date instanceof Date) {
    return date;
  }

  return toLocalDate(date);
}

export function formatDate( date: string | Date | null | undefined): string {
  if (!date) return '—';

  const d = typeof date === 'string' ? new Date(date) : date;

  return new Intl.DateTimeFormat('es-PE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric'
  }).format(d);
}