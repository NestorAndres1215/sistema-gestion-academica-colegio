import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from "../../../shared/ui/button/button";
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';

import { ROLES } from '../../../core/constants/roles';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { Login_Auth } from '../../../core/models/login';
import { FormValidationService } from '../../../core/services/form-validation.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, Button],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly alertService = inject(AlertService);
  private readonly formBuilder = inject(FormBuilder)
  private readonly formValidationService = inject(FormValidationService);


  showPassword = signal(false);

  togglePassword(): void {
    this.showPassword.update(value => !value);
  }

  formulario = this.formBuilder.group({
    login: ['', Validators.required],
    password: ['', Validators.required]
  });

  async operar() {

    if (!this.formValidationService.validate(this.formulario)) return;

    const login: Login_Auth = {
      login: this.formulario.value.login!,
      password: this.formulario.value.password!
    };

    try {

      const data = await firstValueFrom(this.authService.generateToken(login));
      this.authService.setToken(data.token);
      const user = await firstValueFrom(this.authService.getCurrentUser());

      if (!user.role) return;

      await firstValueFrom(this.authService.generateSession());
      this.navigateByRole(user.role);

    } catch (error: any) {
      this.alertService.error("ERROR", error.error.message);
    }
  }



  private navigateByRole(role: string): void {

    const routes: Record<string, string> = {
      [ROLES.ROLE_ADMINISTRATOR]: '/admin',
      [ROLES.ROLE_GUARDIAN]: '/guardian',
      [ROLES.ROLE_STAFF]: '/staff',
      [ROLES.ROLE_TEACHER]: '/teacher',
      [ROLES.ROLE_STUDENT]: '/student'
    };

    this.router.navigate([routes[role] || '/inicio']);
  }
}