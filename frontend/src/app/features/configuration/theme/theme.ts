import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';

import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { ThemeOption } from '../../../core/models/theme.interface';
import { ThemeService } from '../../../core/services/theme.service';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../shared/ui/page-header/page-header";

@Component({
  selector: 'app-theme',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    BreadCrumb,
    PageHeader
  ],
  templateUrl: './theme.html',
  styleUrl: './theme.css',
})
export class Theme implements OnInit {
  readonly icon = "palette";
  readonly title = "Tema de color";
  readonly subtitle = "Personaliza la apariencia visual de la aplicación";
  breadcrumbs: BreadcrumbItem[] = [
    { label: 'Inicio', href: '/admin' },
    { label: 'Cambio de Tema' }
  ];

  selectedKey = 'default';

  themes: ThemeOption[] = [];

  private themeService = inject(ThemeService)


  ngOnInit(): void {

    this.themes = this.getThemes();

    this.themeService.init(this.themes);

    this.selectedKey =
      this.themeService.getCurrent()?.key ?? 'default';
  }

  private getThemes(): ThemeOption[] {
    return [
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
        colorDanger: '#D32F2F'
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
        colorDanger: '#EF4444'
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
        colorDanger: '#EF4444'
      }
    ];
  }

  get currentTheme(): ThemeOption {
    return this.themes.find(t => t.key === this.selectedKey)!;
  }

  get previewColors(): string[] {
    const t = this.currentTheme;
    return [
      t.colorPrincipal,
      t.colorSecundario,
      t.colorTercero,
      t.colorPaginaPrincipal
    ];
  }

  select(key: string): void {
    this.selectedKey = key;
  }

  applyTheme(): void {
    const theme = this.themes.find(t => t.key === this.selectedKey);

    if (theme) {
      this.themeService.setTheme(theme);
    }
  }
}