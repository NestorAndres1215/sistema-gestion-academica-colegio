import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { PageHeader } from '../../../../../shared/ui/page-header/page-header';
import { Button } from '../../../../../shared/ui/button/button';
import { ActivatedRoute } from '@angular/router';
import { AdminService } from '../../../../../core/services/admin.service';
import { firstValueFrom } from 'rxjs';
import { AdminRequest } from '../../../../../core/models/admin.interface';
// Ajusta esta ruta al path real de tu componente app-button
// import { Button } from '../../../../../shared/ui/button/button';

@Component({
  selector: 'app-user-edit',
  imports: [
    BreadCrumb,
    PageHeader,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    Button,
  ],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
})
export class UserEdit implements OnInit {
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);

  editForm!: FormGroup;

  readonly generos = [
    { value: 'MALE', label: 'Masculino' },
    { value: 'FEMALE', label: 'Femenino' },
    { value: 'OTRO', label: 'Otro' },
  ];

  readonly nacionalidades = [
    { value: 'Peruana', label: 'Peruana' },
    { value: 'Colombiana', label: 'Colombiana' },
    { value: 'Chilena', label: 'Chilena' },
    { value: 'Argentina', label: 'Argentina' },
    { value: 'Ecuatoriana', label: 'Ecuatoriana' },
    { value: 'Boliviana', label: 'Boliviana' },
    { value: 'Venezolana', label: 'Venezolana' },
    { value: 'Otra', label: 'Otra' },
  ];
  readonly admin = signal<AdminRequest | null>(null);
  readonly icon = 'edit';
  readonly title = 'Editar usuario';
  readonly subtitle = 'Modifica la información personal del usuario.';
  private readonly route = inject(ActivatedRoute);
  private readonly adminService = inject(AdminService);

  userId!: string;

  constructor(private fb: FormBuilder) {
    this.buildForm();
  }

  async ngOnInit(): Promise<void> {
    this.userId = this.route.snapshot.paramMap.get('id') ?? '';
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Listado de Usuarios', href: '/admin/usuarios/listado-usuario' },
      { label: 'Editar Usuario' },
    ]);

    await this.loadUser();
  }

  private buildForm(): void {
    this.editForm = this.fb.group({
      dni: ['', [Validators.required, Validators.pattern(/^\d{8}$/)]],
      firstName: ['', Validators.required],
      middleName: [''],
      paternalLastName: ['', Validators.required],
      maternalLastName: [''],
      phone: ['', [Validators.required, Validators.pattern(/^\d{9}$/)]],
      birthDate: ['', Validators.required],
      gender: ['', Validators.required],
      nationality: ['', Validators.required],
    });
  }

  private async loadUser(): Promise<void> {
    const usuario = await firstValueFrom(this.adminService.getById(this.userId));
    this.editForm.patchValue(usuario);
    this.admin.set(usuario);
    console.log(this.admin());
  }

  cancelar(): void {
    this.editForm.reset();
  }

  async operar(): Promise<void> {
    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    const payload: AdminRequest = {
      email: this.admin()?.email ?? null,
      username: this.admin()?.username ?? null,
      password: '',
      dni: this.editForm.value.dni!,
      firstName: this.editForm.value.firstName!,
      middleName: this.editForm.value.middleName || null,
      paternalLastName: this.editForm.value.paternalLastName!,
      maternalLastName: this.editForm.value.maternalLastName || null,
      phone: this.editForm.value.phone!,
      birthDate: this.editForm.value.birthDate!,
      gender: this.editForm.value.gender!,
      nationality: this.editForm.value.nationality!,
    };

    const formData = new FormData();

    formData.append(
      'admin',
      new Blob([JSON.stringify(payload)], {
        type: 'application/json',
      }),
    );

    try {
      await firstValueFrom(this.adminService.update(this.userId, formData));
      console.log('Actualizado correctamente');
    } catch (error) {
      console.error(error);
    }
  }
}
