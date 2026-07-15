import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { firstValueFrom } from 'rxjs';

import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';
import { FormValidationService } from '../../../core/services/form-validation.service';
import { LoginModel } from '../../../core/models/login.interface';
import { ROLES } from '../../../core/constants/roles';
import { CompanyService } from '../../../core/services/company.service';
import { Button } from "../../../shared/ui/button/button";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    Button
],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);
  private readonly alertService = inject(AlertService);
  private readonly companyService = inject(CompanyService)
  private readonly formBuilder = inject(FormBuilder)
  private readonly formValidationService = inject(FormValidationService);
  readonly hidePassword = signal(true);


  readonly logoUrl = signal<string | null>(null);
  readonly companyName = signal<string>('');
  readonly loginForm = this.formBuilder.group({
    login: ['', Validators.required],
    password: ['', Validators.required]
  });

  togglePassword(): void {
    this.hidePassword.set(!this.hidePassword());
  }
  
  get inicial(): string {
    return this.companyName()?.charAt(0).toUpperCase() ?? '';
  }

  async operar() {

    if (!this.formValidationService.validate(this.loginForm)) return;

    const login: LoginModel = {
      login: this.loginForm.value.login!,
      password: this.loginForm.value.password!
    };

    try {

      const data = await firstValueFrom(this.authService.generateToken(login));
      this.authService.setToken(data.token);

      const user = await firstValueFrom(this.authService.getCurrentUser());

      if (!user.role) return;

      this.navigateByRole(user.role);

    } catch (error: any) {
      this.alertService.error(error.error.message);
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

  async ngOnInit(): Promise<void> {
    await this.getCompanyLogo();
  }

  async getCompanyLogo(): Promise<void> {

    const company = await firstValueFrom(this.companyService.getById('COMP0001'));
    this.logoUrl.set(company.logo);
    this.companyName.set(company.name);

  }

}