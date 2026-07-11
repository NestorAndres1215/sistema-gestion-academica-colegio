import { Component, input, computed, ElementRef, inject } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';
import { MatIconModule } from '@angular/material/icon';
import { ChartConfiguration, ChartData } from 'chart.js';
import { Statistic } from '../../../core/models/statistic.interface';

@Component({
  selector: 'app-chart-bar',
  standalone: true,
  imports: [BaseChartDirective, MatIconModule],
  templateUrl: './chart-bar.html',
  styleUrl: './chart-bar.css',
})
export class ChartBar {

  private readonly elementRef = inject(ElementRef);

  readonly title = input<string>('');
  readonly icon = input<string>('');
  readonly statistics = input.required<Statistic[]>();
  readonly datasetLabel = input<string>('');
  readonly height = input<number>(340);
  readonly emptyMessage = input<string>('Sin datos registrados en este periodo');

  private readonly fallbackColor = '#6366f1';

  private getCssVar(name: string, fallback: string): string {
    const value = getComputedStyle(this.elementRef.nativeElement)
      .getPropertyValue(name)
      .trim();

    return value || fallback;
  }

  // Total de todos los valores — si es 0, no hay nada que graficar
  readonly totalValue = computed(() =>
    this.statistics().reduce((sum, item) => sum + item.quantity, 0)
  );

  readonly hasData = computed(() =>
    this.statistics().length > 0 && this.totalValue() > 0
  );

readonly chartData = computed<ChartData<'bar'>>(() => {
  const color = this.getCssVar('--color-grafico-1', this.fallbackColor);

  return {
    labels: this.statistics().map(item => item.label),
    datasets: [
      {
        label: this.datasetLabel(),
        data: this.statistics().map(item => item.quantity),
        backgroundColor: color,
        hoverBackgroundColor: color,
        borderRadius: 8,
        barThickness: 44,      // antes 32
        maxBarThickness: 52,   // antes 40
        minBarLength: 4,
      }
    ]
  };
});

  readonly chartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    layout: {
      padding: { top: 16, right: 8, bottom: 0, left: 0 },
    },
    interaction: {
      mode: 'index',
      intersect: false,
    },
    plugins: {
      legend: { display: false },
      tooltip: {
        mode: 'index',
        intersect: false,
        backgroundColor: '#1e293b',
        padding: 12,
        cornerRadius: 8,
        titleFont: { size: 13, weight: 600 },
        bodyFont: { size: 13 },
        callbacks: {
          label: (context) => `${context.dataset.label || 'Cantidad'}: ${context.parsed.y}`,
        },
      },
    },
    scales: {
      x: {
        ticks: { color: '#94a3b8', font: { size: 12 } },
        grid: { display: false },
        border: { display: false },
      },
      y: {
        beginAtZero: true,
        suggestedMax: 5,
        ticks: {
          color: '#94a3b8',
          stepSize: 1,
          font: { size: 12 },
          padding: 8,
        },
        grid: { color: 'rgba(148,163,184,0.08)' },
        border: { display: false },
      },
    },
  };
}