import { Component, ViewChild } from '@angular/core';
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
import { MatMenuTrigger, MatMenu, MatMenuModule } from '@angular/material/menu';
import { MatBadgeModule } from '@angular/material/badge';

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
    MatIconModule,
    MatToolbarModule,
    RouterModule,
    MatMenuTrigger,
    MatMenuModule
],
  templateUrl: './layout.html',
  styleUrl: './layout.css',
})
export class Layout {
toggleTheme() {
throw new Error('Method not implemented.');
}

  @ViewChild(MatMenuTrigger)
  mainMenuTrigger!: MatMenuTrigger;

  @ViewChild('sidenav')
  sidenav!: MatSidenav;

  user: any = null;
  userRoleName: string = '';
  mainMenus: Menu[] = [];
  username: string = '';

  constructor(
    private menuService: MenuService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const userData = localStorage.getItem('user');

    this.user = userData ? JSON.parse(userData) : null;
    this.username = this.user?.email || 'Usuario';
    this.userRoleName = this.user?.roles?.[0]?.name || '';
    this.loadMenus();
    
  }

  loadMenus() {
    this.menuService.getAll().subscribe((menus: (Menu | null)[]) => {
      if (!menus) return;

      // Eliminar null
      const validMenus: Menu[] = menus.filter((m): m is Menu => m !== null);

      // Filtrar por rol
      const filtered = validMenus.filter(menu =>
        menu.roles?.some(r => r.name === this.userRoleName)
      );

      // Ordenar por menuOrder
      this.mainMenus = filtered.sort((a, b) => Number(a.menuOrder) - Number(b.menuOrder));
    });
  }

  handleClick(menu: Menu) {
    if (menu.children?.length) {
      this.toggleSubMenu(menu);
    } else if (menu.route) {
      this.navigateTo(menu.route);
    } else {
      console.warn("Menu sin ruta ni hijos:", menu);
    }
  }

  toggleSubMenu(menu: Menu) {
    menu.mostrarSubMenu = !menu.mostrarSubMenu;
  }

  navigateTo(route?: string) {
    if (route) {
      this.router.navigateByUrl(route);
      this.sidenav.close();
    }
  }

  logout() {
    this.authService.logout();
  }

}
