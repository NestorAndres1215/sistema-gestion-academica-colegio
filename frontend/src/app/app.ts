import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ThemeService } from './core/services/theme.service';
import { ThemeOption } from './core/models/theme.interface';
import { Title } from '@angular/platform-browser';
import { CompanyService } from './core/services/company.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly title = signal('frontend');
  private readonly themeService = inject(ThemeService);
  private readonly titleService = inject(Title);
  private readonly configService = inject(CompanyService);

  async ngOnInit(): Promise<void> {

    this.themesSystem();
    await this.getCompany();
  }

  async getCompany(): Promise<void> {

    const company = await firstValueFrom(
      this.configService.getById("COMP0001")
    );
    
    this.titleService.setTitle(company.name);

    const link: HTMLLinkElement | null =
      document.getElementById('appFavicon') as HTMLLinkElement;

    const newHref = company.logo.startsWith('http')
      ? company.logo
      : window.location.origin + '/' + company.logo;

    link.href = newHref + '?v=' + new Date().getTime();
  }

  themesSystem(): void {
    const THEMES: ThemeOption[] = [
        {
        key: 'default',
        name: 'Institucional',
        colorPrincipal: '#1A3A6B',
        colorSecundario: '#F4F6FA',
        colorTercero: '#F5A623',
        colorPaginaPrincipal: '#F4F6FA',
        colorTextoPrimario: '#1A3A6B',
        colorTextoSecundario: '#5A6D8C',
        colorBorder: '#D0D7E8',
        colorShadow: 'rgba(26, 44, 91, 0.15)',
        colorDanger: '#D32F2F',
          colorHover: 'rgba(17, 24, 39, 0.04)' 
      },
      {
        key: 'dark',
        name: 'Modo Oscuro',
        colorPrincipal: '#1E293B',
        colorSecundario: '#F4F6FA',
        colorTercero: '#FBBF24',
        colorPaginaPrincipal: '#0B1220',
        colorTextoPrimario: '#F8FAFC',
        colorTextoSecundario: '#94A3B8',
        colorBorder: '#334155',
        colorShadow: 'rgba(0, 0, 0, 0.45)',
        colorDanger: '#EF4444',
        colorHover: 'rgba(255, 255, 255, 0.06)' 
      },
      {
        key: 'white-premium',
        name: 'Blanco Premium',
        colorPrincipal: '#111827',
        colorSecundario: '#FFFFFF',
        colorTercero: '#F3F4F6',
        colorPaginaPrincipal: '#FFFFFF',
        colorTextoPrimario: '#111827',
        colorTextoSecundario: '#6B7280',
        colorBorder: '#E5E7EB',
        colorShadow: 'rgba(0, 0, 0, 0.06)',
        colorDanger: '#EF4444',
   colorHover: 'rgba(17, 24, 39, 0.04)' 
      }
    ];

    this.themeService.init(THEMES);
  }
}
