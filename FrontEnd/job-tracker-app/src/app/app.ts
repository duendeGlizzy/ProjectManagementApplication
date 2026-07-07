import {ChangeDetectorRef, Component, inject, OnInit, signal} from '@angular/core';
import {RouterOutlet, RouterModule, RouterLink, RouterLinkActive, Router} from '@angular/router';
import {MatToolbar, MatToolbarModule} from '@angular/material/toolbar';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule} from '@angular/material/sidenav';
import {MatListItem, MatListModule, MatNavList} from '@angular/material/list';
import {MatButtonModule, MatIconButton} from '@angular/material/button';
import {CommonModule} from '@angular/common';
import {AuthService} from './features/security/service/auth-service';
import {Employee} from './features/employee/models/employee-model';
import {EmployeeService} from './features/employee/services/employee-service';


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
export class App implements OnInit {
  protected authService = inject(AuthService);
  private router = inject(Router);
  protected employee: Employee | null = null;
  private employeeService = inject(EmployeeService);
  private cdr = inject(ChangeDetectorRef);

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/homepage']);
    this.employee = null;
  }

  ngOnInit() {
    this.authService.currentEmployee$.subscribe((employee: any) => {
      this.employee = employee;
      if (employee && employee.firstName === 'Loading' && employee.lastName === 'Profile...') {
        this.loadEmployee();
      } else {
        this.cdr.detectChanges();
      }
    });

    if (this.authService.isLoggedIn() && !this.employee) {
      this.loadEmployee();
    }

  }

  loadEmployee() {
    this.employeeService.getLoggedInEmployee().subscribe({
      next: data => {
        if(data) {
          this.employee = data;
        }else{
          this.handleAdminSessionFallback();
        }
        this.cdr.detectChanges();
      },
      error: () =>{
        this.handleAdminSessionFallback();
      }
    });
  }

  private handleAdminSessionFallback() {

    this.employee = {
      firstName: 'System',
      lastName: 'Administrator',
      email: localStorage.getItem('user_email') || 'admin@company.com'
    } as Employee;
    this.cdr.detectChanges();

  }






}
