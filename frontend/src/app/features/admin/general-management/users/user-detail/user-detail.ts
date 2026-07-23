import { Component, computed, inject, signal } from '@angular/core';
import { BreadCrumb } from '../../../../../shared/ui/bread-crumb/bread-crumb';
import { BreadcrumbItem } from '../../../../../core/models/breadcrumb.interface';
import { MatIconModule } from '@angular/material/icon';
import { AdminResponse } from '../../../../../core/models/admin.interface';
import { AuthService } from '../../../../../core/services/auth.service';
import { AlertService } from '../../../../../core/services/alert.service';
import { AdminService } from '../../../../../core/services/admin.service';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { PageHeader } from '../../../../../shared/ui/page-header/page-header';

@Component({
  selector: 'app-user-detail',
  imports: [BreadCrumb, MatIconModule, CommonModule, PageHeader],
  templateUrl: './user-detail.html',
  styleUrl: './user-detail.css',
})
export class UserDetail {
  readonly breadcrumbs = signal<BreadcrumbItem[]>([]);
  private readonly route = inject(ActivatedRoute);
  userId!: string;

  async ngOnInit(): Promise<void> {
    this.userId = this.route.snapshot.paramMap.get('id') ?? '';

    await this.initUser();
    await this.loadUsers();

    console.log('ID usuario:', this.userId);
  }

  async loadUsers(): Promise<void> {
    const admin = await firstValueFrom(this.adminService.getById(this.userId));

    this.admin.set(admin);
  }

  private async initUser(): Promise<void> {
    this.breadcrumbs.set([
      { label: 'Inicio', href: '/admin' },
      { label: 'Usuarios' },
      { label: 'Listado de Usuarios', href: '/admin/usuarios/listado-usuario' },
      { label: 'Detalle de Usuarios' },
    ]);
  }

  readonly admin = signal<AdminResponse | null>(null);

  readonly logoPreview = signal<string | null>(null);
  private readonly adminService = inject(AdminService);

  readonly icon = 'contact_page';
  readonly title = 'Detalle del usuario';
  readonly subtitle = 'Consulta la información personal y los datos de la cuenta del usuario.';
  selectedFile: File | null = null;
  editMode = signal(false);

  generos = [
    { value: 'MALE', label: 'Masculino' },
    { value: 'FEMALE', label: 'Femenino' },
    { value: 'OTHER', label: 'Otro' },
    { value: 'PREFER_NOT_TO_SAY', label: 'Prefiero no decir' },
  ];

  get inicial(): string {
    return this.admin()?.firstName?.charAt(0).toUpperCase() ?? '';
  }

  toggleEdit(): void {
    this.editMode.set(true);
  }

  cancelar(): void {
    this.editMode.set(false);
  }

  triggerLogoInput(): void {
    document.getElementById('logo-input')?.click();
  }

  onLogoChange(event: Event): void {
    const input = event.target as HTMLInputElement;

    if (!input.files?.length) return;

    this.selectedFile = input.files[0];

    const reader = new FileReader();

    reader.onload = () => {
      this.logoPreview.set(reader.result as string);
    };

    reader.readAsDataURL(this.selectedFile);
  }

  readonly profileImage = computed(() => {
    const preview = this.logoPreview();

    if (preview) {
      return preview;
    }

    const profile = this.admin()?.profile;

    return profile && profile.trim() !== '' ? profile : null;
  });

  getGenderLabel(value: string): string {
    return this.generos.find((g) => g.value === value)?.label ?? '';
  }
}
