import { CommonModule } from '@angular/common';
import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { firstValueFrom } from 'rxjs';

import { AuthService } from '../../../core/services/auth.service';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { PasswordChange } from '../../../core/models/user.interface';
import { BreadCrumb } from '../../../shared/ui/bread-crumb/bread-crumb';
import { FormValidationService } from '../../../core/services/form-validation.service';
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    BreadCrumb
  ],
  templateUrl: './change-password.html',
  styleUrl: './change-password.css',
})
export class ChangePassword {

  private readonly authService = inject(AuthService);
  private readonly fb = inject(FormBuilder);
  private readonly formValidationService = inject(FormValidationService);
  private readonly alertService = inject(AlertService);
  // Estado de la UI
  editMode = signal(false);
  username = signal('');
  currentRole = signal('');
  breadcrumbs = signal<BreadcrumbItem[]>([]);

  showNueva = signal(false);
  showConfirmar = signal(false);
  showActual = signal(false);
  private currentUserId = '';

  avatarLetter = computed(() =>
    this.username().charAt(0).toUpperCase() || '?'
  );

  passwordForm = this.fb.nonNullable.group({
    currentPassword: ['', Validators.required],
    newPassword: ['', Validators.required],
    confirmNewPassword: ['', Validators.required]
  });

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  private async initUser(): Promise<void> {
    const currentUser = await firstValueFrom(this.authService.getCurrentUser());

    this.currentUserId = currentUser.id;
    this.username.set(currentUser.username);
    this.currentRole.set(currentUser.role);

    this.breadcrumbs.set([
      {
        label: 'Inicio',
        href: this.authService.getHomeByRole(currentUser.role)
      },
      {
        label: 'Cambiar Contraseña'
      }
    ]);
  }

  toggleEdit(): void {
    this.editMode.set(true);
  }

  cancelar(): void {
    this.editMode.set(false);
    this.passwordForm.reset();
  }

  async guardar(): Promise<void> {
    if (!this.formValidationService.validate(this.passwordForm)) return;

    const payload: PasswordChange = this.passwordForm.getRawValue();

    try {
      console.log('Payload:', payload);
      await firstValueFrom(
        this.authService.changePassword(this.currentUserId, payload)
      );

      this.editMode.set(false);
      this.passwordForm.reset();

    } catch (error: any) {
      this.alertService.error(error.error.message);
    }
  }
}