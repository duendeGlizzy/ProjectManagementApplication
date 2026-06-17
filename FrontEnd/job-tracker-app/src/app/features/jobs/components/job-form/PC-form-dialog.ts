import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { PrimeContractorService } from '../../../contractors/services/prime-contractor';

@Component({
  selector: 'app-contractor-dialog',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <h2 mat-dialog-title style="margin-bottom: 12px;">Add Prime Corporate Contractor</h2>
    <mat-dialog-content>
      <form [formGroup]="contractorForm" style="display: flex; flex-direction: column; gap: 8px; min-width: 300px; padding-top: 4px;">
        <mat-form-field appearance="outline">
          <mat-label>Company Name</mat-label>
          <input matInput formControlName="companyName">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Business Address</mat-label>
          <input matInput formControlName="address">
        </mat-form-field>
        <mat-form-field appearance="outline">
          <mat-label>Office Phone Number</mat-label>
          <input matInput formControlName="phoneNumber">
        </mat-form-field>
      </form>
    </mat-dialog-content>
    <mat-dialog-actions align="end" style="padding-bottom: 12px;">
      <button mat-button (click)="dialogRef.close()">Cancel</button>
      <button mat-raised-button color="primary" [disabled]="contractorForm.invalid" (click)="saveContractor()">Register Business</button>
    </mat-dialog-actions>
  `
})
export class ContractorDialogComponent {
  contractorForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private contractorService: PrimeContractorService,
    public dialogRef: MatDialogRef<ContractorDialogComponent>
  ) {
    this.contractorForm = this.fb.group({
      companyName: ['', [Validators.required]],
      address: [''],
      phoneNumber: ['']
    });
  }

  saveContractor(): void {
    if (this.contractorForm.valid) {
      this.contractorService.createPrimeContractor(this.contractorForm.value).subscribe({
        next: (savedContractor) => this.dialogRef.close(savedContractor),
        error: (err) => console.error('Failed to register contractor company:', err)
      });
    }
  }
}
