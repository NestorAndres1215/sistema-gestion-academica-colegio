import { ChangeDetectorRef, Component, computed, inject, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { MenuService } from '../../core/services/menu.service';
import { MatMenuTrigger, MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { BreakpointObserver } from '@angular/cdk/layout';
import { firstValueFrom, Subscription } from 'rxjs';
import { ROLES } from '../../core/constants/roles';
import { CompanyService } from '../../core/services/company.service';
import { Menu } from '../../core/models/menu.interface';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [
    MatBadgeModule,
    CommonModule,
    MatIconModule,
    MatSidenavModule,
    FormsModule,
    MatListModule,
    MatToolbarModule,
    RouterModule,
    MatMenuTrigger,
    MatMenuModule,
  ],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout implements OnInit, OnDestroy {


  @ViewChild('sidenav') sidenav!: MatSidenav;

  readonly isMobile = signal(false);
  readonly user = signal<any | null>(null);
  readonly userRoleName = signal('');
  readonly username = signal('');
  readonly codigo = signal(0);
  readonly mainMenus = signal<Menu[]>([]);
  readonly nameSchool = signal('');
  private readonly menuService = inject(MenuService);
  private readonly router = inject(Router);
  private readonly bp = inject(BreakpointObserver);
  private readonly authService = inject(AuthService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly companyService = inject(CompanyService);

  private bpSub?: Subscription;

  async ngOnInit(): Promise<void> {
    await this.getUsername();
    await this.getCompanies();
    this.loadMenus();
    this.initBreakpointObserver();
  }

  private initBreakpointObserver(): void {
    this.bpSub = this.bp
      .observe(['(max-width: 768px)'])
      .subscribe(result => {
        const isMobile = result.matches;

        this.isMobile.set(isMobile);

        if (isMobile && this.sidenav?.opened) {
          this.sidenav.close();
        }
      });
  }

  ngOnDestroy(): void {
    this.bpSub?.unsubscribe();
  }

  async getUsername(): Promise<void> {
    const user = await firstValueFrom(this.authService.getCurrentUser());

    this.user.set(user);

    this.username.set(user.username);
    this.userRoleName.set(user.role);
  }

  async getCompanies(): Promise<void> {
    const company = await firstValueFrom(this.companyService.getById("COMP0001"));
    this.nameSchool.set(company.name)
  }

  async loadMenus(): Promise<void> {

    const menus = await firstValueFrom(this.menuService.getAll());

    const valid = menus.filter((m: Menu) => !!m);

    this.mainMenus.set(
      valid
        .filter((m: Menu) =>
          m.roles?.some(r => r.name === this.userRoleName())
        )
        .sort((a: Menu, b: Menu) =>
          Number(a.menuOrder) - Number(b.menuOrder)
        )
        .map((m: Menu) => ({
          ...m,
          children: (m.children ?? [])
            .sort((a: Menu, b: Menu) =>
              Number(a.menuOrder) - Number(b.menuOrder)
            )
            .map((child: Menu) => ({
              ...child,
              children: (child.children ?? [])
                .sort((a: Menu, b: Menu) =>
                  Number(a.menuOrder) - Number(b.menuOrder)
                )
            })),
          mostrarSubMenu: false
        }))
    );

    this.cdr.markForCheck();

  }

  toggleSubMenu(menu: Menu): void {
    menu.mostrarSubMenu = !menu.mostrarSubMenu;
  }

  handleClick(menu: Menu): void {
    if (menu.children?.length) {
      this.toggleSubMenu(menu);
      return;
    }

    if (menu.route) {
      this.router.navigateByUrl(menu.route);
      if (this.isMobile()) this.sidenav.close();
      return;
    }
  }

  navigateTo(route?: string): void {
    if (!route) return;
    this.router.navigateByUrl(route);
    if (this.isMobile()) this.sidenav.close();
  }

  hasChildren(menu: Menu): boolean {
    return !!menu.children?.length;
  }

  toggleTheme(): void {
    this.router.navigate(['/configuracion/cambiar-tema']);
  }

  historial(): void {
    this.router.navigate(['/configuracion/historial-usuarios']);
  }

  settings(): void {
    this.router.navigate(['/configuracion']);
  }

  contrana(): void {
    this.router.navigate(['/configuracion/cambiar-contrasena']);
  }

  cuenta(): void {
    this.router.navigate(['/mi-cuenta']);
  }

  perfil(): void {
    this.router.navigate(['/mi-perfil']);
  }

  company(): void {
    this.router.navigate(['/configuracion/company']);
  }

  help(): void {
    this.router.navigate(['/configuracion/ayuda']);
  }

  async logout(): Promise<void> {
    await firstValueFrom(this.authService.logoutSession(this.user()?.id));
    await this.authService.logout();
    await this.authService.logoutSession(this.user().id);
  }

  isAdmin = computed(() =>
    this.userRoleName() === ROLES.ROLE_ADMINISTRATOR
  );

  isTeacher = computed(() =>
    this.userRoleName() === ROLES.ROLE_TEACHER
  );

  isStudent = computed(() =>
    this.userRoleName() === ROLES.ROLE_STUDENT
  );
}