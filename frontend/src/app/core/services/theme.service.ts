import { Service } from '@angular/core';
import { ThemeOption } from '../models/theme.interface';

@Service()
export class ThemeService {

  private themes: ThemeOption[] = [];
  private current!: ThemeOption;

  init(themes: ThemeOption[]): void {
    this.themes = themes;

    const saved = (localStorage.getItem('app_theme') || '').trim();

    const theme =
      this.themes.find(t => t.key === saved) ??
      this.themes.find(t => t.key === 'default')!;

    this.setTheme(theme);
  }

  setTheme(theme: ThemeOption): void {
    this.current = theme;

    this.applyTheme(theme);

    localStorage.setItem('app_theme', theme.key);
  }

  getThemes(): ThemeOption[] {
    return this.themes;
  }

  getCurrent(): ThemeOption {
    return this.current;
  }

  private applyTheme(t: ThemeOption): void {
    const root = document.documentElement;

    root.style.setProperty('--color-principal', t.colorPrincipal);
    root.style.setProperty('--color-secundario', t.colorSecundario);
    root.style.setProperty('--color-tercero', t.colorTercero);
    root.style.setProperty('--color-pagina-principal', t.colorPaginaPrincipal);

    root.style.setProperty('--color-texto-primario', t.colorTextoPrimario);
    root.style.setProperty('--color-texto-secundario', t.colorTextoSecundario);

    root.style.setProperty('--color-border', t.colorBorder);
    root.style.setProperty('--color-shadow', t.colorShadow);
    root.style.setProperty('--color-danger', t.colorDanger);
  }
}