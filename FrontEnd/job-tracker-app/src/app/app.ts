import {Component, inject, signal} from '@angular/core';
import {RouterOutlet, RouterModule, RouterLink, RouterLinkActive, Router} from '@angular/router';
import {MatToolbar, MatToolbarModule} from '@angular/material/toolbar';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule} from '@angular/material/sidenav';
import {MatListItem, MatListModule, MatNavList} from '@angular/material/list';
import {MatButtonModule, MatIconButton} from '@angular/material/button';
import {CommonModule} from '@angular/common';
import {AuthService} from './features/security/service/auth-service';


@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected authService = inject(AuthService);
  private router = inject(Router);

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }


}
