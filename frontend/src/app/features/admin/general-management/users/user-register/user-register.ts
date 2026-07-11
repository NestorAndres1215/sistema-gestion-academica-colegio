import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ReactiveFormsModule, ValidationErrors, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { AdminRequest } from '../../../../../core/models/admin.interface';
import { AuthService } from '../../../../../core/services/auth.service';
import { MatDatepickerModule } from "@angular/material/datepicker";
import { AdminService } from '../../../../../core/services/admin.service';
import { BreadCrumb } from "../../../../../shared/ui/bread-crumb/bread-crumb";
import { PageHeader } from "../../../../../shared/ui/page-header/page-header";
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { toApiDate } from '../../../../../core/utils/date.util';
import { FormValidationService } from '../../../../../core/services/form-validation.service';
import { Button } from "../../../../../shared/ui/button/button";
import { AlertService } from '../../../../../core/services/alert.service';

@Component({
  selector: 'app-user-register',
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    BreadCrumb,
    PageHeader,
    Button
  ],
  templateUrl: './user-register.html',
  styleUrl: './user-register.css',
})
export class UserRegister {
  
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  private readonly fb = inject(FormBuilder);
  private readonly adminService = inject(AdminService);
  private readonly alertService = inject(AlertService)
  private readonly formValidationService = inject(FormValidationService);
  readonly icon = 'person_add';
  readonly title = 'Registrar usuario';
  readonly subtitle = 'Ingrese los datos requeridos para crear una nueva cuenta de usuario.';
  readonly hidePassword = signal(true);
  readonly hideConfirmPassword = signal(true);

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  private async initUser(): Promise<void> {

    this.breadcrumbs.set([
      {
        label: 'Inicio',
        href: '/admin'
      },
      {
        label: 'Usuarios'
      },
      {
        label: 'Listado de Usuarios'
      }
    ]);
  }

  private readonly router = inject(Router);
  readonly generos = [
    { value: 'MASCULINO', label: 'Masculino' },
    { value: 'FEMENINO', label: 'Femenino' },
    { value: 'OTRO', label: 'Otro' }
  ];

  readonly nacionalidades = [
    { value: 'PERUANA', label: 'Peruana' },
    { value: 'COLOMBIANA', label: 'Colombiana' },
    { value: 'CHILENA', label: 'Chilena' },
    { value: 'ARGENTINA', label: 'Argentina' },
    { value: 'ECUATORIANA', label: 'Ecuatoriana' },
    { value: 'BOLIVIANA', label: 'Boliviana' },
    { value: 'VENEZOLANA', label: 'Venezolana' },
    { value: 'OTRA', label: 'Otra' }
  ];

  readonly registerForm: FormGroup = this.fb.group({
    email: ['', [Validators.required]],
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
    confirmPassword: ['', Validators.required],
    firstName: ['', Validators.required],
    middleName: [''],
    paternalLastName: ['', Validators.required],
    maternalLastName: [''],
    dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
    phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
    birthDate: ['', Validators.required],
    gender: ['', Validators.required],
    nationality: ['', Validators.required]
  }, {
  });


  togglePassword(): void {
    this.hidePassword.set(!this.hidePassword());
  }

  toggleConfirmPassword(): void {
    this.hideConfirmPassword.set(!this.hideConfirmPassword());
  }


  async operar(): Promise<void> {

    if (!this.formValidationService.validate(this.registerForm)) return;

    const raw = this.registerForm.getRawValue();

    const payload: AdminRequest = {
      email: raw.email,
      username: raw.username,
      password: raw.password,
      firstName: raw.firstName,
      middleName: raw.middleName?.trim() ? raw.middleName : null,
      paternalLastName: raw.paternalLastName,
      maternalLastName: raw.maternalLastName?.trim() ? raw.maternalLastName : null,
      dni: raw.dni,
      phone: raw.phone,
      birthDate: raw.birthDate instanceof Date
        ? toApiDate(raw.birthDate)
        : raw.birthDate,
      gender: raw.gender,
      nationality: raw.nationality
    };

    try {

      await firstValueFrom(
        this.adminService.create(payload)
      );
      this.alertService.success("Administrador Registro")
      this.router.navigate([
        '/admin/usuarios/listado-usuario'
      ]);

    } catch (error: any) {
      this.alertService.error(error.error?.message)
    }

  }
  
  cancelar(): void {
    this.registerForm.reset();
  }

}
