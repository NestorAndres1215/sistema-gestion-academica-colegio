import { Component, OnDestroy, ViewChild } from '@angular/core';
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

interface Role {
  id: string;
  name: string;
  description?: string;
}

interface Menu {
  id: string;
  code: string;
  name: string;
  icon?: string;
  route?: string;
  menuOrder?: string;
  category?: string;
  parent?: Menu | null;
  roles: Role[];
  children?: Menu[];
  mostrarSubMenu?: boolean;
}

@Component({
  selector: 'app-layout',
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

  @ViewChild('sidenav') sidenav!: MatSidenav;
  @ViewChild(MatMenuTrigger) mainMenuTrigger!: MatMenuTrigger;

  isMobile = false;
  isDark = false;

  user: any = null;
  userRoleName = '';
  mainMenus: Menu[] = [];
  username = '';

  private bpSub!: Subscription;

  constructor(
    private menuService: MenuService,
    private router: Router,
    private breakpointObserver: BreakpointObserver,
    private themeService: ThemeService,
    private authService: AuthService
  ) { }


  ngOnInit(): void {
    const userData = localStorage.getItem('user');
    this.user = userData ? JSON.parse(userData) : null;
    this.username = this.user?.email || 'Usuario';
    this.userRoleName = this.user?.roles?.[0]?.name || '';
    this.loadMenus();
    this.themeService.initTheme();
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
    console.log("entro")
    this.themeService.toggleTheme();
  }

  loadMenus(): void {
    this.menuService.getAll().subscribe((menus: (Menu | null)[]) => {
      if (!menus) return;

      // Eliminar null
      const validMenus: Menu[] = menus.filter((m): m is Menu => m !== null);

      // Filtrar por rol
      console.log(this.userRoleName)
      const filtered = validMenus.filter(menu =>
        menu.roles?.some(r => r.name === this.userRoleName)
      );
      console.log(filtered)

      this.mainMenus = filtered.sort((a, b) => Number(a.menuOrder) - Number(b.menuOrder));
      console.log(this.mainMenus)
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
    this.authService.logout();
  }
}