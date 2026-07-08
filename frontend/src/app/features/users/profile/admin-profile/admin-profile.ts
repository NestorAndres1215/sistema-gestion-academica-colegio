import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { firstValueFrom } from 'rxjs';
import { AuthService } from '../../../../core/services/auth.service';
import { AdminService } from '../../../../core/services/admin.service';
import { AdminRequest, AdminResponse } from '../../../../core/models/admin.interface';
import { MatDatepickerModule } from "@angular/material/datepicker";
import { toLocalDate } from '../../../../core/utils/date.util';
import { AlertService } from '../../../../core/services/alert.service';
import { FormValidationService } from '../../../../core/services/form-validation.service';
import { PageHeader } from "../../../../shared/ui/page-header/page-header";

@Component({
  selector: 'app-admin-profile',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    PageHeader
  ],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css',
})
export class AdminProfile {
  readonly admin = signal<AdminResponse | null>(null);

  readonly logoPreview = signal<string | null>(null);
  private readonly authService = inject(AuthService);
  private readonly adminService = inject(AdminService);
  private readonly alertService = inject(AlertService);
  private readonly formValidationService = inject(FormValidationService);
  private readonly fb = inject(FormBuilder);
  readonly icon = "account_circle";
  readonly title = "Mi perfil";
  readonly subtitle = "Administra tu información personal";
  selectedFile: File | null = null;
  editMode = signal(false);
  generos = [
    { value: 'MALE', label: 'Masculino' },
    { value: 'FEMALE', label: 'Femenino' },
    { value: 'OTHER', label: 'Otro' },
    { value: 'PREFER_NOT_TO_SAY', label: 'Prefiero no decir' }
  ];

  readonly adminForm = this.fb.group({
    firstName: [''],
    middleName: [''],
    paternalLastName: [''],
    maternalLastName: [''],
    dni: ['', [Validators.required,]],
    phone: ['', [Validators.required,]],
    birthDate: this.fb.control<Date | null>(null),
    profile: [''],
    gender: [''],
    nationality: ['']
  });

  private async initUser(): Promise<void> {
    const currentUser = await firstValueFrom(this.authService.getCurrentUser());

    const admins = await firstValueFrom(this.adminService.getByIdEmail(currentUser.email))

    this.admin.set(admins)

    this.adminForm.patchValue({
      firstName: admins.firstName,
      middleName: admins.middleName,
      paternalLastName: admins.paternalLastName,
      maternalLastName: admins.maternalLastName,
      dni: admins.dni,
      phone: admins.phone,
      birthDate: admins.birthDate ? new Date(admins.birthDate) : null,
      profile: admins.profile,
      gender: admins.gender,
      nationality: admins.nationality
    });
  }


  get inicial(): string {
    return this.admin()?.firstName?.charAt(0).toUpperCase() ?? '';
  }

  toggleEdit(): void {
    this.editMode.set(true);
  }

  cancelar(): void {
    this.editMode.set(false);
  }

  async guardar(): Promise<void> {
    if (!this.formValidationService.validate(this.adminForm)) return;

    const admin = this.admin();

    if (!admin?.id) return;

    const adminRequest: AdminRequest = {
      email: admin.email,
      username: admin.username,
      password: "contrasena_backend",
      firstName: this.adminForm.value.firstName!,
      middleName: this.adminForm.value.middleName!,
      paternalLastName: this.adminForm.value.paternalLastName!,
      maternalLastName: this.adminForm.value.maternalLastName || null,
      dni: this.adminForm.value.dni!,
      phone: this.adminForm.value.phone!,
      birthDate: this.adminForm.value.birthDate!.toISOString().split('T')[0],
      gender: this.adminForm.value.gender!,
      nationality: this.adminForm.value.nationality!,

    };

    const formData = new FormData();

    formData.append(
      'admin',
      new Blob(
        [JSON.stringify(adminRequest)],
        { type: 'application/json' }
      )
    );

    if (this.selectedFile) {
      formData.append('file', this.selectedFile);
    }

    try {
      await firstValueFrom(this.adminService.update(admin.id, formData));
      this.editMode.set(false);
      await this.initUser();
    } catch (error: any) {
      this.alertService.error(error.error.message);
    }
  }

  triggerLogoInput(): void {
    document.getElementById('logo-input')?.click();
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

  readonly profileImage = computed(() => {
    const preview = this.logoPreview();

    if (preview) {
      return preview;
    }

    const profile = this.admin()?.profile;

    return profile && profile.trim() !== '' ? profile : null;
  });

  getGenderLabel(value: string): string {
    return this.generos.find(g => g.value === value)?.label ?? '';
  }

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }
}
