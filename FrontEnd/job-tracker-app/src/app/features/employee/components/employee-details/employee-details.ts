import {ChangeDetectorRef, Component, inject, OnInit} from '@angular/core';
import {Employee} from '../../models/employee-model';
import {EmployeeService} from '../../services/employee-service';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import {MatIcon} from '@angular/material/icon';
import {FormsModule} from '@angular/forms';
import {MatFormField, MatInput} from '@angular/material/input';
import {MatLabel} from '@angular/material/input';
import {MatError} from '@angular/material/input';
import {CommonModule} from '@angular/common';
import {AuthService} from '../../../security/service/auth-service';

@Component({
  selector: 'app-employee-details',
  imports: [
    MatProgressSpinner,
    MatCard,
    MatCardHeader,
    MatIcon,
    MatCardTitle,
    MatCardSubtitle,
    MatCardContent,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatError,
    CommonModule,
  ],
  templateUrl: './employee-details.html',
  styleUrl: './employee-details.css',
})
export class EmployeeDetails implements OnInit {
  private employeeService = inject(EmployeeService);
  private cdr = inject(ChangeDetectorRef);
  private authService = inject(AuthService);
  protected employee!: Employee;
  protected loading = true;
  protected saving = false;
  protected successMessage = '';
  protected errorMessage = '';

  ngOnInit(): void {
    this.loadEmployee();
  }

  private loadEmployee(): void {
    this.loading = true;
    this.errorMessage = '';

    this.employeeService.getLoggedInEmployee().subscribe({
      next: data => {
        this.employee = data;
        this.loading = false;
        this.cdr.detectChanges();

      },
      error: error => {
        this.errorMessage = error;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  protected onUpdateEmployee(){
    this.saving = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.employeeService.updateEmployee(this.employee).subscribe({
      next: data => {
        this.employee = data;
        this.authService.updateGlobalEmployee(data);
        this.successMessage = 'profile changed successfully';
        this.saving = false;
        this.cdr.detectChanges();
      },
      error: error => {
        this.errorMessage = error;
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }




}
