import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';
import {VendorService} from '../../../contractors/services/vendor';

@Component({
  selector: 'app-vendor-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  template: `
    <h2 mat-dialog-title style="margin-bottom: 10px;">Quick Add New Vendor</h2>
    <mat-dialog-content [formGroup]="vendorForm" style="display: flex; flex-direction: column; gap: 12px; min-width: 320px;">
      <mat-form-field appearance="outline">
        <mat-label>Company Name</mat-label>
        <input matInput formControlName="companyName">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Phone Number</mat-label>
        <input matInput formControlName="phoneNumber">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Address</mat-label>
        <input matInput formControlName="address">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Description</mat-label>
        <textarea matInput formControlName="description"></textarea>
      </mat-form-field>
    </mat-dialog-content>

    <mat-dialog-actions align="end" style="padding-bottom: 16px;">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-flat-button color="primary" [disabled]="vendorForm.invalid || isSaving" (click)="onSave()">
        {{ isSaving ? 'Saving...' : 'Save Vendor' }}
      </button>
    </mat-dialog-actions>
  `
})
export class VendorDialogComponent {
  vendorForm: FormGroup;
  isSaving = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<VendorDialogComponent>,
    private vendorService: VendorService,
  ) {
    this.vendorForm = this.fb.group({
      companyName: ['', Validators.required],
      phoneNumber: [''],
      address: [''],
      description: ['']
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.vendorForm.invalid) return;
    this.isSaving = true;


    this.vendorService.createVendor(this.vendorForm.value).subscribe({
      next: (newVendor) => {
        this.dialogRef.close(newVendor);
      },
      error: err => {
        console.error('failed to create vendor', err);
        this.isSaving = false;
      }
    });
  }
}
