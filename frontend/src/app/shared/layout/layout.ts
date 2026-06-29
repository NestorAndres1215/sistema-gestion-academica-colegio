import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSidenav, MatSidenavModule } from '@angular/material/sidenav';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { MenuService } from '../../core/services/menu.service';
import { MatMenuTrigger, MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';
import { BreakpointObserver } from '@angular/cdk/layout';
import { Subscription } from 'rxjs';
import { ThemeService } from '../../core/services/theme.service';
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
    MatDividerModule,
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
  contrana() {
    throw new Error('Method not implemented.');
  }
  cuenta() {
    throw new Error('Method not implemented.');
  }
  perfil() {
    throw new Error('Method not implemented.');
  }

  @ViewChild('sidenav') sidenav!: MatSidenav;

  isMobile = false;
  user: any;
  userRoleName = '';
  username = '';
  mainMenus: Menu[] = [];

  private bpSub!: Subscription;

  private menuService = inject(MenuService);
  private router = inject(Router);
  private bp = inject(BreakpointObserver);
  private themeService = inject(ThemeService);
  private authService = inject(AuthService);
  private cdr = inject(ChangeDetectorRef);

  ngOnInit(): void {
    const userData = localStorage.getItem('user');

    this.user = userData ? JSON.parse(userData) : null;
    this.username = this.user?.email ?? 'Usuario';
    this.userRoleName = this.user?.roles?.[0]?.name ?? '';

    this.loadMenus();
    this.themeService.initTheme();

    this.bpSub = this.bp
      .observe(['(max-width: 768px)'])
      .subscribe(r => {
        this.isMobile = r.matches;
        if (this.isMobile && this.sidenav?.opened) {
          this.sidenav.close();
        }
      });
  }

  ngOnDestroy(): void {
    this.bpSub?.unsubscribe();
  }

  loadMenus(): void {
    this.menuService.getAll().subscribe((menus: (Menu | null)[]) => {

      const valid = (menus ?? []).filter((m): m is Menu => !!m);

      this.mainMenus = valid
        .filter(m => m.roles?.some(r => r.name === this.userRoleName))
        .sort((a, b) => Number(a.menuOrder) - Number(b.menuOrder))
        .map(m => ({
          ...m,
          mostrarSubMenu: false
        }));

      this.cdr.markForCheck();
    });
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
      if (this.isMobile) this.sidenav.close();
      return;
    }
  }

  navigateTo(route?: string): void {
    if (!route) return;
    this.router.navigateByUrl(route);
    if (this.isMobile) this.sidenav.close();
  }

  hasChildren(menu: Menu): boolean {
    return !!menu.children?.length;
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

  logout(): void {
    this.authService.logout();
    this.authService.logoutSession(this.user.id);
  }
}