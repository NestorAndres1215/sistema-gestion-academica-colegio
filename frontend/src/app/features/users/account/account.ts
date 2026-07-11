import { CommonModule } from '@angular/common';
import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { firstValueFrom } from 'rxjs';

import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.interface';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { AuthService } from '../../../core/services/auth.service';
import { BreadCrumb } from '../../../shared/ui/bread-crumb/bread-crumb';
import { FormValidationService } from '../../../core/services/form-validation.service';
import { Button } from "../../../shared/ui/button/button";
import { AlertService } from '../../../core/services/alert.service';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    BreadCrumb,
    Button
  ],
  templateUrl: './account.html',
  styleUrl: './account.css'
})
export class Account implements OnInit {

  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService);
  private readonly alertService = inject(AlertService)
  private readonly fb = inject(FormBuilder);
  private readonly formValidationService = inject(FormValidationService);

  editMode = signal(false);
  currentUserId = signal('');
  currentRoles = signal('');
  breadcrumbs = signal<BreadcrumbItem[]>([]);
  username = signal('');

  userForm = this.fb.group({
    username: ['', Validators.required],
    email: ['', [Validators.required, Validators.email]]
  });

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  private async initUser(): Promise<void> {

    const currentUser = await firstValueFrom(this.authService.getCurrentUser());

    this.currentUserId.set(currentUser.id);
    this.currentRoles.set(currentUser.role);
    this.username.set(currentUser.username);
    const homeRoute = this.authService.getHomeByRole(this.currentRoles());

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Mi Cuenta' }
    ]);

    await this.loadUser();

  }

  async loadUser(): Promise<void> {

    const res = await firstValueFrom(
      this.userService.findById(this.currentUserId())
    );

    this.userForm.patchValue({
      username: res.username,
      email: res.email
    });

  }

  toggleEdit(): void {
    this.editMode.set(true);
  }

  async guardar(): Promise<void> {

    if (!this.formValidationService.validate(this.userForm)) return;

    const payload: User = {
      username: this.userForm.value.username!,
      email: this.userForm.value.email!,
      role: this.currentRoles()
    };

    try {
      const user = await firstValueFrom(
        this.userService.update(this.currentUserId(), payload)
      );

      this.userForm.patchValue({
        username: user.username,
        email: user.email
      });

      this.editMode.set(false);

    } catch (error: any) {
      this.alertService.error(error.error.message);
    }
  }
  cancelar(): void {
    this.editMode.set(false);
    this.loadUser();
  }

  avatarLetter = computed(() =>
    this.username().charAt(0).toUpperCase() || '?'
  );

}