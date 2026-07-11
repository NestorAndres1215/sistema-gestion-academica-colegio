import { Component, input } from '@angular/core';
import { StatCardModel } from '../../../core/models/stat-card.interface';
import { Statistic } from '../../../core/models/statistic.interface';
export interface StatCard {
  label: string;
  value: number;
}

@Component({
  selector: 'app-stat-card',
  imports: [],
  templateUrl: './stat-card.html',
  styleUrl: './stat-card.css',
})
export class StatCard {

  card = input.required<Statistic | null>();
  icon = input.required<string>();

}
