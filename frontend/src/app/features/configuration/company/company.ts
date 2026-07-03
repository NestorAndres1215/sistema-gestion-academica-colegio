import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

import { CompanyService } from '../../../core/services/company.service';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { CompanyModel } from '../../../core/models/company.interface';

@Component({
  selector: 'app-company',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './company.html',
  styleUrl: './company.css'
})
export class Company implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly companyService = inject(CompanyService);

  readonly editMode = signal(false);
  readonly success = signal(false);
  readonly logoPreview = signal<string | null>(null);

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);

  selectedFile: File | null = null;

  readonly companyForm = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    ruc: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]],
    phone: ['', Validators.required],
    address: [''],
    city: [''],
    country: [''],
    companyType: [''],
    foundingDate: [''],
    logo: ['']
  });

  readonly tiposCompania = [
    'Institución Educativa',
    'Colegio Privado',
    'Colegio Público',
    'Universidad',
    'Instituto Superior',
    'Academia',
    'Otro'
  ];

  company!: CompanyModel;

  async ngOnInit(): Promise<void> {
    await this.getCompany();
  }

  async getCompany(): Promise<void> {
    try {
      const company = await firstValueFrom(
        this.companyService.getById('COMP0001')
      );

      this.company = company;

      this.companyForm.patchValue({
        name: company.name,
        description: company.description,
        ruc: company.ruc,
        email: company.email,
        phone: company.phone,
        address: company.address,
        city: company.city,
        country: company.country,
        companyType: company.companyType,
        foundingDate: company.foundingDate,
        logo: company.logo
      });

      this.logoPreview.set(company.logo);

    } catch (error) {
      console.error(error);
    }
  }

  get inicial(): string {
    return this.company?.name?.charAt(0).toUpperCase() ?? '';
  }

  get anioFundacion(): string {
    return this.company?.foundingDate
      ? new Date(this.company.foundingDate).getFullYear().toString()
      : '—';
  }

  toggleEdit(): void {
    this.logoPreview.set(this.company.logo);
    this.companyForm.patchValue(this.company);
    this.editMode.set(true);
  }

  cancelar(): void {
    this.logoPreview.set(this.company.logo);
    this.companyForm.patchValue(this.company);
    this.editMode.set(false);
  }

  async guardar(): Promise<void> {
    if (this.companyForm.invalid) {
      this.companyForm.markAllAsTouched();
      return;
    }

    const formData = new FormData();

    formData.append(
      'company',
      new Blob(
        [JSON.stringify(this.companyForm.getRawValue())],
        { type: 'application/json' }
      )
    );

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    try {
      await firstValueFrom(
        this.companyService.update('COMP0001', formData)
      );

      this.editMode.set(false);
      this.success.set(true);

      await this.getCompany();

    } catch (error) {
      console.error('Error updating company:', error);
    }
  }

  onLogoChange(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files?.length) return;

    this.selectedFile = input.files[0];

    const reader = new FileReader();

    reader.onload = () => {
      this.logoPreview.set(reader.result as string);
    };

    reader.readAsDataURL(this.selectedFile);
  }

  triggerLogoInput(): void {
    document.getElementById('logo-input')?.click();
  }
}