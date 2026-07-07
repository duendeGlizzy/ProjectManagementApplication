import {ChangeDetectorRef, Component, inject} from '@angular/core';
import { AdminService } from '../../services/admin-service';
import {Employee} from '../../../employee/models/employee-model';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatIcon} from '@angular/material/icon';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import {MatError, MatFormField, MatInput, MatLabel, MatSuffix} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatButton, MatIconButton} from '@angular/material/button';
import {CommonModule} from '@angular/common';


@Component({
  selector: 'app-admin-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatProgressSpinner,
    MatCard,
    MatCardHeader,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    MatFormField,
    MatLabel,
    MatInput,
    MatError,
    MatButton,
    MatIcon,
    MatSuffix
  ],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard {
  private adminService = inject(AdminService);
  private cdr = inject(ChangeDetectorRef);

  protected newEmployee: Partial<Employee> = {
    firstName: '',
    lastName: '',
    email: '',
    password: ''
  };

  protected employees: Employee[] = [];
  protected loading = true;
  protected successMessage = '';
  protected errorMessage = '';

  protected activePasswordResetId: number | null = null;
  protected targetNewPassword = '';

  ngOnInit(): void {
    this.loadRoster();
  }

  private loadRoster(): void {
    this.loading = true;
    this.adminService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        this.errorMessage = 'could not access records';
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  protected onCreateEmployee(){
    this.clearAlerts();
    this.adminService.createEmployee(this.newEmployee as Employee).subscribe({
      next: (data) => {
        this.successMessage = 'Account created successfully';
        this.newEmployee = {firstName: '', lastName: '', email: '', password: ''};
        this.loadRoster();
      },
      error: (error) => {
        this.errorMessage = 'could not access records';
      }
    });
  }

  protected onDeleteEmployee(id: number | undefined){
    if(!id) return;
    if(confirm('Are you sure?')){
      this.clearAlerts();
      this.adminService.deleteEmployee(id).subscribe({
        next: () =>{
          this.successMessage = 'Account deleted successfully';
          this.loadRoster();
        },
        error: (error) => {
          this.errorMessage = 'could not access records';
        }
      });
    }
  }

  protected togglePasswordResetRow(id: number | undefined){
    if(!id) return;
    this.targetNewPassword = '';

    this.activePasswordResetId = this.activePasswordResetId === id ? null : id;
    this.cdr.detectChanges();
  }

  protected onUpdatePassword(id: number | undefined){
    if(!id || !this.targetNewPassword.trim()) return;

    this.clearAlerts();
    this.adminService.updateEmployeePassword(id, this.targetNewPassword).subscribe({
      next: () =>{
        this.successMessage = 'Account updated successfully';
        this.activePasswordResetId = null;
        this.targetNewPassword = '';
        this.loadRoster();
      },
      error: (error) => {
        this.errorMessage = 'could not access records';
      }
    });
  }

  private clearAlerts(){
    this.successMessage = '';
    this.errorMessage = '';
  }


}
