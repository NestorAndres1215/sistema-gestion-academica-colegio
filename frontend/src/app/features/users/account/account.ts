import { CommonModule } from '@angular/common';
import { Component, OnInit, inject, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';

import { UserService } from '../../../core/services/user.service';
import { User } from '../../../core/models/user.interface';
import { BreadcrumbItem } from '../../../core/models/breadcrumb.interface';
import { AuthService } from '../../../core/services/auth.service';
import { firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-account',
  standalone: true,
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    FormField
  ],
  templateUrl: './account.html',
  styleUrl: './account.css'
})
export class Account implements OnInit {

  private readonly userService = inject(UserService);
  private readonly authService = inject(AuthService);

  currentUserId!: string;
  currentRoles!: string;
  breadcrumbs = signal<BreadcrumbItem[]>([]);
  editMode = false;


  user = signal<User>({
    username: '',
    email: '',
    role: ''
  });

  userForm = form(this.user);

  async ngOnInit(): Promise<void> {
    this.getUsername()

  }

  async getUsername(): Promise<void> {
    const user = await firstValueFrom(this.authService.getCurrentUser());
    this.currentUserId = user.id
    this.currentRoles = user.roles?.[0]?.name
    const homeRoute = this.authService.getHomeByRole(this.currentRoles);

    this.breadcrumbs.set([
      { label: 'Inicio', href: homeRoute },
      { label: 'Configuración' }
    ]);
    console.log(this.currentUserId)
    this.loadUser(this.currentUserId);
  }

  loadUser(id: string): void {
    this.userService.findById(id).subscribe({
      next: (res: any) => {

        this.user.set({
          username: res.username,
          email: res.email,
          role: this.currentRoles
        });

      }
    });
  }

  toggleEdit(): void {
    this.editMode = true;
  }

  guardar(): void {

    const u = this.user();

    const payload = {
      username: u.username,
      email: u.email,
      role: u.role
    };


    this.userService.update(this.currentUserId, payload).subscribe({
      next: (user) => {

        this.user.set({
          username: user.username,
          email: user.email,
          role: user.roles?.[0]?.name ?? ''
        });

        this.loadUser(this.currentUserId);
        this.editMode = false;
      }
    });
  }


  cancelar(): void {
    this.editMode = false;
    this.loadUser(this.currentUserId);
  }
}