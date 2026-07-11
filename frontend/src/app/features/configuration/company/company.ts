import { Component, OnInit, inject, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';

import { CompanyService } from '../../../core/services/company.service';
import { CompanyModel } from '../../../core/models/company.interface';
import { getYear, toLocalDate, toApiDate } from '../../../core/utils/date.util';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";
import { AuthService } from '../../../core/services/auth.service';
import { BreadcrumbItem } from '../../../core/models/bread-crumb.interface';
import { PageHeader } from "../../../shared/ui/page-header/page-header";
import { FormValidationService } from '../../../core/services/form-validation.service';
import { AlertService } from '../../../core/services/alert.service';
import { Button } from "../../../shared/ui/button/button";
import { MatDividerModule } from '@angular/material/divider';



@Component({
  selector: 'app-company',
  standalone: true,
  imports: [
    CommonModule,MatDividerModule ,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    BreadCrumb,
    PageHeader,
    Button
],
  templateUrl: './company.html',
  styleUrl: './company.css'
})
export class Company implements OnInit {

  private readonly fb = inject(FormBuilder);
  private readonly companyService = inject(CompanyService);
  private readonly authService = inject(AuthService);
  private readonly formValidationService = inject(FormValidationService);
  private readonly alertService = inject(AlertService)
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly editMode = signal(false);
  readonly success = signal(false);
  readonly logoPreview = signal<string | null>(null);
  readonly company = signal<CompanyModel | null>(null);
  readonly icon = "business";
  readonly title = "Mi compañía";
  readonly subtitle = "Administra la información de tu institución";
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
    foundingDate: this.fb.control<Date | null>(null),
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

  readonly inicial = computed(() =>
    this.company()?.name?.charAt(0).toUpperCase() ?? ''
  );

  readonly anioFundacion = computed(() =>
    getYear(this.company()?.foundingDate)
  );

  async ngOnInit(): Promise<void> {
    await this.getCompany();
    await this.initUser();
  }


  private async initUser(): Promise<void> {
    const currentUser = await firstValueFrom(this.authService.getCurrentUser());

    this.breadcrumbs.set([
      {
        label: 'Inicio',
        href: this.authService.getHomeByRole(currentUser.role)
      },
      {
        label: 'Compania'
      }
    ]);
  }

  async getCompany(): Promise<void> {

    const data = await firstValueFrom(this.companyService.getById('COMP0001'));

    this.company.set(data);
    this.logoPreview.set(data.logo);
    this.companyForm.patchValue({
      name: data.name,
      description: data.description,
      ruc: data.ruc,
      email: data.email,
      phone: data.phone,
      address: data.address,
      city: data.city,
      country: data.country,
      companyType: data.companyType,
      foundingDate: toLocalDate(data.foundingDate),
      logo: data.logo
    });
  }

  toggleEdit(): void {
    const c = this.company();

    if (!c) return;

    this.logoPreview.set(c.logo);

    this.companyForm.patchValue({
      ...c,
      foundingDate: toLocalDate(c.foundingDate)
    });

    this.editMode.set(true);
  }

  cancelar(): void {
    const c = this.company();

    if (!c) return;

    this.logoPreview.set(c.logo);

    this.companyForm.patchValue({
      ...c,
      foundingDate: toLocalDate(c.foundingDate)
    });

    this.editMode.set(false);
  }

  async guardar(): Promise<void> {

    if (!this.formValidationService.validate(this.companyForm)) return;

    const company = {
      ...this.companyForm.getRawValue(),
      foundingDate: toApiDate(
        this.companyForm.get('foundingDate')?.value
      )
    };

    const formData = new FormData();

    formData.append(
      'company',
      new Blob(
        [JSON.stringify(company)],
        {
          type: 'application/json'
        }
      )
    );

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    try {

      await firstValueFrom(
        this.companyService.update('1', formData)
      );

      this.editMode.set(false);
      this.success.set(true);

      await this.getCompany();

    } catch (error: any) {
      this.alertService.error(error.error.message);
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