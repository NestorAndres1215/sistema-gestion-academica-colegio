import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { firstValueFrom, Observable } from 'rxjs';
import * as XLSX from 'xlsx';
export interface ParsedFileResult {
  headers: string[];
  rows: Record<string, string>[];
}
@Service()
export class ImportService {

  private readonly http = inject(HttpClient);

  downloadTemplate(fileName: string, headers: string[]): void {
    const worksheet = XLSX.utils.aoa_to_sheet([headers]);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Plantilla');
    XLSX.writeFile(workbook, `${fileName}.xlsx`);
  }

  submitImport<T>(endpoint: string, payload: T[]): Observable<unknown> {
    return this.http.post(endpoint, { data: payload });
  }

  parseFile(file: File): Promise<{ headers: string[]; rows: Record<string, string>[] }> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();

      reader.onload = (event) => {
        try {
          const data = event.target?.result;
          const workbook = XLSX.read(data, { type: 'binary', cellDates: false, cellText: true });
          const firstSheet = workbook.SheetNames[0];
          const worksheet = workbook.Sheets[firstSheet];

          const rows: Record<string, string>[] = XLSX.utils.sheet_to_json(worksheet, {
            defval: '',
            raw: false,
          });
          const headers = rows.length > 0 ? Object.keys(rows[0]) : [];

          resolve({ headers, rows });
        } catch (error) {
          reject(error);
        }
      };

      reader.onerror = () => reject(reader.error);
      reader.readAsBinaryString(file);
    });
  }

  generateExcelFile<T>(data: T[], fileName: string, sheetName = 'Hoja1'): File {

    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();

    XLSX.utils.book_append_sheet(workbook, worksheet, sheetName);

    const buffer = XLSX.write(workbook, {
      bookType: 'xlsx',
      type: 'array'
    });


    return new File(
      [buffer],
      `${fileName}.xlsx`,
      {
        type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      }
    );
  }
}
