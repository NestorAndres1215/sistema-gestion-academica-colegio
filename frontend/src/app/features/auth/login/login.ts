import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Button } from "../../../shared/ui/button/button";
import { AuthService } from '../../../core/services/auth.service';
import { AlertService } from '../../../core/services/alert.service';

import { ROLES } from '../../../core/constants/roles';
import { Router } from '@angular/router';
export interface Login_Auth {
  login: string;
  password: string;
}
@Component({
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule, Button],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login implements OnInit {

  showPassword: boolean = false;

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  formulario!: FormGroup;

  private router = inject(Router);
  private authService = inject(AuthService);
  private alertService = inject(AlertService);
  private formBuilder = inject(FormBuilder)

  initForm() {
    this.formulario = this.formBuilder.group({
      login: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  operar() {
    if (this.formulario.invalid) {
      this.alertService.warning("campos incompleto", "faltan campos");
      this.formulario.markAllAsTouched();
      return;
    }

    const login: Login_Auth = {
      login: this.formulario.value.login,
      password: this.formulario.value.password,
    };

    this.authService.generateToken(login).subscribe({
      next: async (data: any) => {


        this.authService.setToken(data.token);

        this.authService.getCurrentUser().subscribe({
          next: (user) => {
            const rol = user.roles[0].name;

            if (!rol) {
              return;
            }
            console.log(user);
            localStorage.setItem('user', JSON.stringify(user));

            switch (rol) {
              case ROLES.ROLE_ADMINISTRATOR:
                console.log(rol)
                this.router.navigate(['/admin']);
                break;

              case ROLES.ROLE_GUARDIAN:
                this.router.navigate(['/guardian']);
                break;
              case ROLES.ROLE_STAFF:
                this.router.navigate(['/staff']);
                break;
              case ROLES.ROLE_TEACHER:
                this.router.navigate(['/teacher']);
                break;
              case ROLES.ROLE_STUDENT:
                this.router.navigate(['/student']);
                break;
              default:
                this.router.navigate(['/inicio']);
                break;
            }

          },
          error: (error) => {
            console.log(error.error.message)
            this.alertService.error("ERROR", error.error.message);
          }
        });
      },
      error: (error) => {
        console.log(error.error.message)
        this.alertService.error("ERROR", error.error.message);
      }
    });
  }


  ngOnInit(): void {
    this.initForm();
  }
}