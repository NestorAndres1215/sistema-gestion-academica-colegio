import { CommonModule, TitleCasePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-data-table',
  standalone: true,
   imports: [
    CommonModule,
    MatTableModule,
    TitleCasePipe
  ],
  templateUrl: './data-table.html',
  styleUrl: './data-table.css',
})
export class DataTable {
  @Input() displayedColumns: string[] = [];

  @Input() dataSource: any[] = [];


}
