import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

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
  selector: 'app-admin-profile',
  imports: [
    CommonModule,
    FormsModule,
    MatIconModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule
  ],
  templateUrl: './admin-profile.html',
  styleUrl: './admin-profile.css',
})
export class AdminProfile {

  editMode = false;
  success = false;

  username = 'usuario123';
  email = 'usuario@colegio.edu.pe';
  rol = 'Docente';

  perfil: Perfil = {
    nombre: 'Juan Carlos Pérez López',
    telefono: '+51 987 654 321',
    fechaNacimiento: '1990-05-15',
    direccion: 'Av. Los Pinos 123, Lima',
    genero: 'Masculino',
    nacionalidad: 'Peruana',
    bio: 'Docente con más de 10 años de experiencia en educación primaria. Apasionado por la enseñanza y el desarrollo integral de los estudiantes.',
  };

  perfilEdit: Perfil = { ...this.perfil };

  generos = ['Masculino', 'Femenino', 'Otro', 'Prefiero no decir'];

  get edad(): number {
    const hoy = new Date();
    const nac = new Date(this.perfil.fechaNacimiento);
    let edad = hoy.getFullYear() - nac.getFullYear();
    const m = hoy.getMonth() - nac.getMonth();
    if (m < 0 || (m === 0 && hoy.getDate() < nac.getDate())) edad--;
    return edad;
  }

  get inicial(): string {
    return this.perfil.nombre.charAt(0).toUpperCase();
  }

  toggleEdit(): void {
    this.perfilEdit = { ...this.perfil };
    this.editMode = true;
  }

  cancelar(): void {
    this.editMode = false;
  }

  guardar(): void {
    this.perfil = { ...this.perfilEdit };
    this.editMode = false;
    this.success = true;
    setTimeout(() => this.success = false, 3000);
  }
}
