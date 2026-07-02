import { Component, computed, inject, signal } from '@angular/core';
import { BreadCrumb } from "../../../../shared/ui/bread-crumb/bread-crumb";
import { BreadcrumbItem } from '../../../../core/models/breadcrumb.interface';
import { firstValueFrom } from 'rxjs';
import { UserService } from '../../../../core/services/user.service';
import { AuthService } from '../../../../core/services/auth.service';
import { FormValidationService } from '../../../../core/services/form-validation.service';
import { FormBuilder, FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { AdminProfile } from "../admin-profile/admin-profile";
import { ROLES } from '../../../../core/constants/roles';
import { TeacherProfile } from '../teacher-profile/teacher-profile';
import { StudentProfile } from '../student-profile/student-profile';
interface Perfil {
  nombre: string;
  telefono: string;
  fechaNacimiento: string;
  direccion: string;
  genero: string;
  nacionalidad: string;
  bio: string;
}
@Component({
  selector: 'app-profile',
  imports: [CommonModule, FormsModule, MatIconModule, MatButtonModule, MatFormFieldModule, MatInputModule, MatSelectModule, BreadCrumb, AdminProfile, TeacherProfile, StudentProfile],
  templateUrl: './profile-page.html',
  styleUrl: './profile-page.css',
})
export class ProfilePage {
  currentRoles = signal('');
  breadcrumbs = signal<BreadcrumbItem[]>([]);


  private readonly authService = inject(AuthService);
  async ngOnInit(): Promise<void> {
    await this.initUser();
  }

  ROLES = ROLES;



  isAdmin = computed(() =>
    this.currentRoles() === ROLES.ROLE_ADMINISTRATOR
  );

  isTeacher = computed(() =>
    this.currentRoles() === ROLES.ROLE_TEACHER
  );

  isStudent = computed(() =>
    this.currentRoles() === ROLES.ROLE_STUDENT
  );


  private async initUser(): Promise<void> {
    try {
      const currentUser = await firstValueFrom(this.authService.getCurrentUser());

      this.currentRoles.set(currentUser.role);
      console.log(this.currentRoles());
      const homeRoute = this.authService.getHomeByRole(this.currentRoles());

      this.breadcrumbs.set([
        { label: 'Inicio', href: homeRoute },
        { label: 'Mi Perfil' }
      ]);

    } catch (error) {
      console.error(error);
    }
  }
 
}
