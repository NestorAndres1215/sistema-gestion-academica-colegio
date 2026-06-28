import { Service } from '@angular/core';

@Service()
export class ThemeService {

  private isDark = false;

  initTheme(): void {
    const theme = localStorage.getItem('theme') ?? 'light';
    this.isDark = theme === 'dark';
    this.applyTheme(theme);
  }

  toggleTheme(): void {
    const theme = this.isDark ? 'light' : 'dark';
    this.isDark = !this.isDark;

    this.applyTheme(theme);
    localStorage.setItem('theme', theme);
  }

  private applyTheme(theme: string): void {
    document.documentElement.setAttribute('data-theme', theme);
  }

  isDarkMode(): boolean {
    return this.isDark;
  }
}