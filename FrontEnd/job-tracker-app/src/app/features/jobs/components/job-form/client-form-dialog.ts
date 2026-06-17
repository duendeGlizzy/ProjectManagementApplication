import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ClientService } from '../../services/client';

@Component({
  selector: 'app-client-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title style="margin-bottom: 12px;">Add New Client Profile</h2>
    <mat-dialog-content>
      <form [formGroup]="clientForm" style="display: flex; flex-direction: column; gap: 8px; min-width: 300px; padding-top: 4px;">
        <mat-form-field appearance="outline">
          <mat-label>First Name</mat-label>
          <input matInput formControlName="firstName">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Last Name</mat-label>
          <input matInput formControlName="lastName">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Phone Number</mat-label>
          <input matInput formControlName="phoneNumber">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Street Address</mat-label>
          <input matInput formControlName="address">
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end" style="padding-bottom: 12px;">
      <button mat-button (click)="dialogRef.close()">Cancel</button>
      <button mat-raised-button color="primary" [disabled]="clientForm.invalid" (click)="saveClient()">Create Profile</button>
    </mat-dialog-actions>
  `
})
export class ClientDialogComponent {
  clientForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private clientService: ClientService,
    public dialogRef: MatDialogRef<ClientDialogComponent>
  ) {
    this.clientForm = this.fb.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      phoneNumber: [''],
      address: ['']
    });
  }

  saveClient(): void {
    if (this.clientForm.valid) {
      this.clientService.createClient(this.clientForm.value).subscribe({
        next: (savedClient) => this.dialogRef.close(savedClient),
        error: (err) => console.error('Failed to create client entity:', err)
      });
    }
  }
}
