import { Component, input, computed, ElementRef, inject } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration, ChartData } from 'chart.js';
import { Statistic } from '../../../core/models/statistic.interface';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-chart-donut',
  standalone: true,
  imports: [BaseChartDirective, MatIconModule],
  templateUrl: './chart-donut.html',
  styleUrl: './chart-donut.css',
})
export class ChartDonut {
  private readonly elementRef = inject(ElementRef);

  readonly title = input<string>('');
  readonly icon = input<string>('');
  readonly statistics = input.required<Statistic[]>();
  readonly height = input<number>(280);

  private readonly paletteVars = [
    '--color-grafico-1',
    '--color-grafico-2',
    '--color-grafico-3',
    '--color-grafico-4',
    '--color-grafico-5',
  ];

  private readonly fallbackPalette = ['#6366f1', '#ec4899', '#22c55e', '#f59e0b', '#38bdf8'];

  private getCssVar(name: string, fallback: string): string {
    const value = getComputedStyle(this.elementRef.nativeElement).getPropertyValue(name).trim();

    return value || fallback;
  }

  readonly chartData = computed<ChartData<'doughnut'>>(() => {
    const statistics = this.statistics();

    const colors = this.paletteVars
      .slice(0, statistics.length)
      .map((varName, i) => this.getCssVar(varName, this.fallbackPalette[i]));

    return {
      labels: statistics.map((item) => item.label),
      datasets: [
        {
          data: statistics.map((item) => item.quantity),
          backgroundColor: colors,
          hoverBackgroundColor: colors,
          borderWidth: 0,
          hoverOffset: 6,
        },
      ],
    };
  });

  readonly chartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '70%',
    layout: { padding: 12 },
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          color: '#94a3b8',
          padding: 20,
          font: { size: 13, weight: 500 },
          usePointStyle: true,
          pointStyle: 'circle',
          boxWidth: 8,
          boxHeight: 8,
        },
      },
      tooltip: {
        backgroundColor: '#1e293b',
        padding: 12,
        cornerRadius: 8,
        titleFont: { size: 13, weight: 600 },
        bodyFont: { size: 13 },
        displayColors: true,
        boxPadding: 4,
      },
    },
  };
}
