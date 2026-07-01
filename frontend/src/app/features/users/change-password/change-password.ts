import { CommonModule } from '@angular/common';
import { Component, inject, signal } from '@angular/core';
import { FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { UserService } from '../../../core/services/user.service';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { PasswordChange } from '../../../core/models/user.interface';
import { form, FormField } from '@angular/forms/signals';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,

],

  templateUrl: './change-password.html',
  styleUrl: './change-password.css',
})
export class ChangePassword {


  private readonly authService = inject(AuthService);

passwordForm!: FormGroup;
  editMode = false;
  currentUserId!: string;
  currentRoles!: string;
  breadcrumbs = signal<BreadcrumbItem[]>([]);

  username !: string;

  nuevaPassword = '';
  confirmarPassword = '';
  actualPassword = '';
  showNueva = false;
  showConfirmar = false;
  showActual = false;
  error = '';
  success = false;


  password = signal<PasswordChange>({
    currentPassword: '',
    newPassword: '',
    confirmNewPassword: ''
  });



  toggleEdit(): void {
    this.editMode = true;
    this.nuevaPassword = '';
    this.confirmarPassword = '';
    this.error = '';
    this.success = false;
  }

  cancelar(): void {
    this.editMode = false;
    this.nuevaPassword = '';
    this.confirmarPassword = '';
    this.error = '';
  }

  async ngOnInit(): Promise<void> {
    this.getUsername()

  }
  async getUsername(): Promise<void> {
    const user = await firstValueFrom(this.authService.getCurrentUser());
    this.currentUserId = user.id
    this.username = user.username
    this.currentRoles = user.role
    console.log(user)
    const homeRoute = this.authService.getHomeByRole(this.currentRoles);
  
    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Configuración' }
    ]);

  
  }


  guardar(): void {
    this.error = '';
    this.success = true;
    this.editMode = false;
    this.nuevaPassword = '';
    this.confirmarPassword = '';

    setTimeout(() => this.success = false, 3000);
  }
}
