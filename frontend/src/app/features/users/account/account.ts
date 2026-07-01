import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { firstValueFrom } from 'rxjs';

import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.interface';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { AuthService } from '../../../core/services/auth.service';
import { BreadCrumb } from "../../../shared/ui/bread-crumb/bread-crumb";

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    FormField,
    BreadCrumb
],
  templateUrl: './account.html',
  styleUrl: './account.css'
})
export class Account implements OnInit {

  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService);

  readonly currentUserId = signal('');
  readonly currentRoles = signal('');

  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  readonly editMode = signal(false);

  readonly user = signal<User>({
    username: '',
    email: '',
    role: ''
  });

  readonly userForm = form(this.user);

  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  private async initUser(): Promise<void> {
    try {
      const currentUser = await firstValueFrom(this.authService.getCurrentUser());

      this.currentUserId.set(currentUser.id);
      this.currentRoles.set(currentUser.role);

      const homeRoute = this.authService.getHomeByRole(this.currentRoles());

      this.breadcrumbs.set([
        { label: 'Inicio', href: homeRoute },
        { label: 'Mi Cuenta' }
      ]);

      this.loadUser();
    } catch (error) {
      console.error(error);
    }
  }

  loadUser(): void {
    this.userService.findById(this.currentUserId()).subscribe({
      next: (res) => {
        this.user.set({
          username: res.username,
          email: res.email,
          role: this.currentRoles()
        });
      },
    });
  }

  toggleEdit(): void {
    this.editMode.set(true);
  }

  guardar(): void {
    const u = this.user();

    const payload: User = {
      username: u.username,
      email: u.email,
      role: this.currentRoles() // ← Aquí estaba el error
    };

    this.userService.update(this.currentUserId(), payload).subscribe({
      next: (user) => {
        this.user.set(user);
        this.editMode.set(false);
      },
      error: (err) => console.error(err)
    });
  }

  get avatarLetter(): string {
    return this.user().username?.charAt(0).toUpperCase() || '?';
  }

  cancelar(): void {
    this.editMode.set(false);
    this.loadUser();
  }
}