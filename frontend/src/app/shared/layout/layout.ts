import { ChangeDetectorRef, Component, inject, OnDestroy, ViewChild } from '@angular/core';
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
export class Layout implements OnDestroy {


  @ViewChild('sidenav')
  sidenav!: MatSidenav;

  @ViewChild(MatMenuTrigger)
  mainMenuTrigger!: MatMenuTrigger;

  isMobile = false;
  isDark = false;

  user: any = null;
  userRoleName = '';
  mainMenus: Menu[] = [];
  username = '';

  private bpSub!: Subscription;

  private readonly menuService = inject(MenuService);
  private readonly router = inject(Router);
  private readonly breakpointObserver = inject(BreakpointObserver);
  private readonly themeService = inject(ThemeService);
  private readonly authService = inject(AuthService);

  ngOnInit(): void {
    const userData = localStorage.getItem('user');
    this.user = userData ? JSON.parse(userData) : null;
    this.username = this.user?.email || 'Usuario';
    this.userRoleName = this.user?.roles?.[0]?.name || '';

    this.loadMenus();
    this.themeService.initTheme()

    this.bpSub = this.breakpointObserver
      .observe(['(max-width: 768px)'])
      .subscribe(result => {
        this.isMobile = result.matches;
        if (this.isMobile && this.sidenav?.opened) {
          this.sidenav.close();
        }
      });
  }

  ngOnDestroy(): void {
    this.bpSub?.unsubscribe();
  }

  toggleTheme(): void {
    this.themeService.toggleTheme();
  }

  loadMenus(): void {

    this.menuService.getAll().subscribe((menus: (Menu | null)[]) => {

      if (!menus) return;
      const validMenus: Menu[] = menus.filter((m): m is Menu => m !== null);
      const filtered = validMenus.filter(menu =>
        menu.roles?.some(r => r.name === this.userRoleName)
      );
      this.mainMenus = filtered.sort((a, b) => Number(a.menuOrder) - Number(b.menuOrder));

    });
  }

  handleClick(menu: Menu): void {
    if (menu.children?.length) {
      this.toggleSubMenu(menu);
    } else if (menu.route) {
      this.navigateTo(menu.route);
    } else {
      console.warn('Menu sin ruta ni hijos:', menu);
    }
  }

  toggleSubMenu(menu: Menu): void {
    menu.mostrarSubMenu = !menu.mostrarSubMenu;
  }

  navigateTo(route?: string): void {
    if (route) {
      this.router.navigateByUrl(route);
      if (this.isMobile) this.sidenav.close();
    }
  }

  logout(): void {
    console.log(this.user.id)
    this.authService.logout();
    this.authService.logoutSession(this.user.id)
  }

  contrana(): void {
    throw new Error('Method not implemented.');
  }

  cuenta(): void {
    throw new Error('Method not implemented.');
  }

  perfil(): void {
    throw new Error('Method not implemented.');
  }
}