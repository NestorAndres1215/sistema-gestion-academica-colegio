import { LOCALE_ID, Provider } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeEs from '@angular/common/locales/es';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';

registerLocaleData(localeEs);

export const DATE_PROVIDERS: Provider[] = [
  provideNativeDateAdapter(),

  {
    provide: LOCALE_ID,
    useValue: 'es',
  },
  {
    provide: MAT_DATE_LOCALE,
    useValue: 'es',
  },
];
