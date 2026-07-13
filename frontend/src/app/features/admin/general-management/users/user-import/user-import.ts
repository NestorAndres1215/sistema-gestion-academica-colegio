import { Component, computed, ElementRef, inject, OnInit, signal, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { TableColumn } from '../../../../../core/models/table.interface';
import { Button } from "../../../../../shared/ui/button/button";
import { Table, TableAction } from "../../../../../shared/ui/table/table";
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { ImportService } from '../../../../../core/services/import.service';
import { AdminService } from '../../../../../core/services/admin.service';
import { AdminReportService } from '../../../../../core/services/admin-report.service';
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { firstValueFrom } from 'rxjs';
import { AlertService } from '../../../../../core/services/alert.service';

interface ImportRow {
  rowNumber: number;
  email: string;
  username: string;
  firstName: string;
  middleName: string;
  paternalLastName: string;
  maternalLastName: string;
  dni: string;
  phone: string;
  birthDate: string;
  gender: string;
  nationality: string;
  isValid: boolean;
  errors: string[];
}

type EditableFields = Omit<ImportRow, 'rowNumber' | 'isValid' | 'errors'>;

@Component({
  selector: 'app-user-import',
  imports: [Button, Table, BreadCrumb, FormsModule, PageHeader],
  templateUrl: './user-import.html',
  styleUrl: './user-import.css',
})
export class UserImport implements OnInit {

  private readonly importService = inject(ImportService);
  private readonly adminService = inject(AdminService);
  private readonly adminReportService = inject(AdminReportService);
  private readonly alertService = inject(AlertService);
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly fileName = signal<string>('');
  readonly parsing = signal(false);
  readonly submitting = signal(false);
  readonly isDragging = signal(false);
  readonly parsedRows = signal<ImportRow[]>([]);
  readonly hasFile = computed(() => this.parsedRows().length > 0);
  private readonly originalFile = signal<File | null>(null);
  private readonly existingEmails = signal<Set<string>>(new Set());
  private readonly existingDnis = signal<Set<string>>(new Set());
  private readonly existingPhones = signal<Set<string>>(new Set());
  private readonly existingUsernames = signal<Set<string>>(new Set());
  readonly loadingExisting = signal(false);
  readonly icon = "upload_file";
  readonly title = "Importar usuarios";
  readonly subtitle = "Importa múltiples usuarios al sistema mediante una plantilla de Excel";
  readonly editingRowNumber = signal<number | null>(null);
  readonly editDraft = signal<Record<string, string>>({});

  readonly requiredHeaders = [
    'Email', 'Username', 'FirstName', 'PaternalLastName',
    'MaternalLastName', 'Dni', 'Phone', 'BirthDate', 'Gender', 'Nationality',
  ];

  readonly previewColumns: TableColumn[] = [
    { key: 'rowNumber', label: 'Fila', width: '60px' },
    { key: 'email', label: 'Email' },
    { key: 'username', label: 'Usuario' },
    { key: 'firstName', label: 'Nombres' },
    { key: 'paternalLastName', label: 'Ap. Paterno' },
    { key: 'maternalLastName', label: 'Ap. Materno' },
    { key: 'dni', label: 'DNI' },
    { key: 'phone', label: 'Teléfono' },
    { key: 'birthDate', label: 'F. Nacimiento' },
    { key: 'gender', label: 'Género' },
    { key: 'nationality', label: 'Nacionalidad' },
    { key: 'isValid', label: 'Validación', width: '200px' },
    { key: 'actions', label: 'Acciones', width: '100px' },
  ];

  readonly validRows = computed(() =>
    this.parsedRows().filter(r => r.isValid)
  );

  readonly invalidRows = computed(() =>
    this.parsedRows().filter(r => !r.isValid)
  );

  readonly canImport = computed(() =>
    this.validRows().length > 0 && !this.submitting()
  );

  async ngOnInit(): Promise<void> {
    await this.initUser();
    await this.loadExistingUsers();
  }

  private async initUser(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Importar Usuarios' },
    ]);
  }

  private async loadExistingUsers(): Promise<void> {

    this.loadingExisting.set(true);
    const users = await firstValueFrom(this.adminReportService.getExistingUsersForValidation());
console.log(users)
    this.existingEmails.set(new Set(
      users.map(u => String(u.email ?? '').toLowerCase()).filter(Boolean)
    ));

    this.existingDnis.set(new Set(
      users.map(u => String(u.dni ?? '')).filter(Boolean)
    ));

    this.existingPhones.set(new Set(
      users.map(u => String(u.phone ?? '')).filter(Boolean)
    ));
    this.existingUsernames.set(new Set(
      users.map(u => String(u.username ?? '').toLowerCase()).filter(Boolean)
    ));

  }

  triggerFileSelect(): void {
    this.fileInputRef.nativeElement.click();
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (file) {
      this.processFile(file);
    }
    input.value = '';
  }

  onDragOver(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(true);
  }

  onDragLeave(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(false);
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    this.isDragging.set(false);

    const file = event.dataTransfer?.files?.[0];
    if (file) {
      this.processFile(file);
    }
  }

  private async processFile(file: File): Promise<void> {
    const validExtensions = ['.xlsx', '.xls', '.csv'];
    const isValidExt = validExtensions.some(ext => file.name.toLowerCase().endsWith(ext));

    if (!isValidExt) {
      this.alertService.warning('Formato no soportado. Sube un archivo .xlsx, .xls o .csv')
      return;
    }

    this.parsing.set(true);
    this.fileName.set(file.name);
    this.originalFile.set(file);

    try {
      const { headers, rows } = await this.importService.parseFile(file);

      const missingHeaders = this.requiredHeaders.filter(
        required => !headers.some(h => h.toLowerCase().trim() === required.toLowerCase())
      );

      if (missingHeaders.length > 0) {
        this.alertService.warning(`Faltan columnas requeridas en el archivo: ${missingHeaders.join(', ')}`)
        this.resetFile();
        return;
      }

      this.mapRawRows(headers, rows);

    } catch (error) {
      this.alertService.warning('No se pudo leer el archivo. Verifica que el formato sea correcto.')
      this.resetFile();
    } finally {
      this.parsing.set(false);
    }
  }


  private mapRawRows(headers: string[], rows: Record<string, string>[]): void {
    const findKey = (target: string) => headers.find(h => h.toLowerCase().trim() === target.toLowerCase());

    const emailKey = findKey('Email');
    const usernameKey = findKey('Username');
    const firstNameKey = findKey('FirstName');
    const middleNameKey = findKey('MiddleName');
    const paternalKey = findKey('PaternalLastName');
    const maternalKey = findKey('MaternalLastName');
    const dniKey = findKey('Dni');
    const phoneKey = findKey('Phone');
    const birthDateKey = findKey('BirthDate');
    const genderKey = findKey('Gender');
    const nationalityKey = findKey('Nationality');

    const rawRows: Array<EditableFields & { rowNumber: number }> = rows.map((row, index) => ({
      rowNumber: index + 2,
      email: emailKey ? String(row[emailKey] ?? '').trim() : '',
      username: usernameKey ? String(row[usernameKey] ?? '').trim() : '',
      firstName: firstNameKey ? String(row[firstNameKey] ?? '').trim() : '',
      middleName: middleNameKey ? String(row[middleNameKey] ?? '').trim() : '',
      paternalLastName: paternalKey ? String(row[paternalKey] ?? '').trim() : '',
      maternalLastName: maternalKey ? String(row[maternalKey] ?? '').trim() : '',
      dni: dniKey ? String(row[dniKey] ?? '').trim() : '',
      phone: phoneKey ? String(row[phoneKey] ?? '').trim() : '',
      birthDate: birthDateKey ? String(row[birthDateKey] ?? '').trim() : '',
      gender: genderKey ? String(row[genderKey] ?? '').trim().toUpperCase() : '',
      nationality: nationalityKey ? String(row[nationalityKey] ?? '').trim() : '',
    }));

    this.parsedRows.set(this.buildValidatedRows(rawRows));
  }

  private buildValidatedRows(rawRows: Array<EditableFields & { rowNumber: number }>): ImportRow[] {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const dniRegex = /^\d{8}$/;
    const phoneRegex = /^\d{9}$/;

    const seenEmails = new Set<string>();
    const seenDnis = new Set<string>();
    const seenUsernames = new Set<string>();
    const seenPhones = new Set<string>();

    const existingEmails = this.existingEmails();
    const existingDnis = this.existingDnis();
    const existingPhones = this.existingPhones();
    const existingUsernames = this.existingUsernames();

    return rawRows.map(raw => {
      const email = raw.email.trim();
      const username = raw.username.trim();
      const dni = raw.dni.trim();
      const phone = raw.phone.trim();
      const gender = raw.gender.trim().toUpperCase();

      const errors: string[] = [];

      if (!email) {
        errors.push('Email requerido');
      } else if (!emailRegex.test(email)) {
        errors.push('Email inválido');
      } else if (seenEmails.has(email.toLowerCase())) {
        errors.push('Email duplicado en el archivo');
      } else if (existingEmails.has(email.toLowerCase())) {
        errors.push('Email ya registrado en el sistema');
      }

      if (!username) {
        errors.push('Username requerido');
      } else if (seenUsernames.has(username.toLowerCase())) {
        errors.push('Username duplicado en el archivo');
      } else if (existingUsernames.has(username.toLowerCase())) {
        errors.push('Username ya registrado en el sistema');
      }

      if (!raw.firstName.trim()) errors.push('Nombres requeridos');
      if (!raw.paternalLastName.trim()) errors.push('Apellido paterno requerido');
      if (!raw.maternalLastName.trim()) errors.push('Apellido materno requerido');

      if (!dni) {
        errors.push('DNI requerido');
      } else if (!dniRegex.test(dni)) {
        errors.push('DNI debe tener 8 dígitos');
      } else if (seenDnis.has(dni)) {
        errors.push('DNI duplicado en el archivo');
      } else if (existingDnis.has(dni)) {
        errors.push('DNI ya registrado en el sistema');
      }

      if (!phone) {

        errors.push('Teléfono requerido');

      } else if (!phoneRegex.test(phone)) {

        errors.push('Teléfono debe tener 9 dígitos');

      } else if (seenPhones.has(phone)) {

        errors.push('Teléfono duplicado en el archivo');

      } else if (existingPhones.has(phone)) {

        errors.push('Teléfono ya registrado en el sistema');

      }

      if (!raw.birthDate.trim()) {
        errors.push('Fecha de nacimiento requerida');
      } else if (isNaN(Date.parse(raw.birthDate))) {
        errors.push('Fecha de nacimiento inválida');
      }

      if (!gender) {
        errors.push('Género requerido');
      } else if (gender !== 'MALE' && gender !== 'FEMALE') {
        errors.push('Género debe ser MALE o FEMALE');
      }

      if (!raw.nationality.trim()) errors.push('Nacionalidad requerida');

      if (email && emailRegex.test(email)) seenEmails.add(email.toLowerCase());
      if (dni && dniRegex.test(dni)) seenDnis.add(dni);
      if (username) seenUsernames.add(username.toLowerCase());
      if (phone && phoneRegex.test(phone)) seenPhones.add(phone);


      return {
        ...raw,
        email,
        username,
        dni,
        phone,
        gender,
        isValid: errors.length === 0,
        errors,
      };
    });
  }

  isEditing(row: ImportRow): boolean {
    return this.editingRowNumber() === row.rowNumber;
  }

  startEdit(row: ImportRow): void {
    this.editingRowNumber.set(row.rowNumber);
    this.editDraft.set({
      email: row.email,
      username: row.username,
      firstName: row.firstName,
      middleName: row.middleName,
      paternalLastName: row.paternalLastName,
      maternalLastName: row.maternalLastName,
      dni: row.dni,
      phone: row.phone,
      birthDate: row.birthDate,
      gender: row.gender,
      nationality: row.nationality,
    });
  }

  updateDraftField(field: string, value: string): void {
    this.editDraft.update(draft => ({ ...draft, [field]: value }));
  }

  cancelEdit(): void {
    this.editingRowNumber.set(null);
    this.editDraft.set({});
  }

  saveEdit(rowNumber: number): void {

    const draft = this.editDraft();

    const updatedRows = this.parsedRows().map(r => {
      if (r.rowNumber !== rowNumber) return r;
      return { ...r, ...draft };
    });

    const rawRows = updatedRows.map(r => {
      const { isValid, errors, ...editable } = r;
      return editable;
    });

    this.parsedRows.set(this.buildValidatedRows(rawRows));

    this.editingRowNumber.set(null);
    this.editDraft.set({});

    this.rebuildExcel();
  }

  async deleteRow(rowNumber: number): Promise<void> {

    const confirmed = await this.alertService.confirm('¿Eliminar esta fila de la previsualización?');

    if (!confirmed) {
      this.alertService.info('Acción cancelada');
      return;
    }

    const remainingRows = this.parsedRows()
      .filter(r => r.rowNumber !== rowNumber)
      .map(r => {

        const { isValid, errors, ...editable } = r;

        return editable;

      });


    this.parsedRows.set(this.buildValidatedRows(remainingRows));

    this.alertService.success('Fila eliminada correctamente');
    this.rebuildExcel();
  }

  resetFile(): void {
    this.fileName.set('');
    this.parsedRows.set([]);
    this.originalFile.set(null);
    this.editingRowNumber.set(null);
    this.editDraft.set({});
  }

  downloadTemplate(): void {
    this.importService.downloadTemplate('plantilla-usuarios', [
      'Email', 'Username', 'FirstName', 'MiddleName', 'PaternalLastName',
      'MaternalLastName', 'Dni', 'Phone', 'BirthDate', 'Gender', 'Nationality',
    ]);
  }

  async confirmImport(): Promise<void> {

    const file = this.originalFile();

    if (!this.canImport() || !file) return;

    this.submitting.set(true);

    try {

      const result = await firstValueFrom(this.adminService.importExcel(file));


      this.alertService.success(`Se importaron ${result.total} usuarios correctamente.`);
      this.resetFile();
      await this.loadExistingUsers();


    } catch (error) {

      this.alertService.error('Ocurrió un error al importar. Intenta nuevamente.');

    } finally {

      this.submitting.set(false);

    }
  }

  private rebuildExcel(): void {

    const rows = this.parsedRows().map(r => ({
      Email: r.email,
      Username: r.username,
      FirstName: r.firstName,
      MiddleName: r.middleName,
      PaternalLastName: r.paternalLastName,
      MaternalLastName: r.maternalLastName,
      Dni: r.dni,
      Phone: r.phone,
      BirthDate: r.birthDate,
      Gender: r.gender,
      Nationality: r.nationality
    }));

    this.originalFile.set(
      this.importService.generateExcelFile(rows, 'usuarios', 'Usuarios'));
  }
}