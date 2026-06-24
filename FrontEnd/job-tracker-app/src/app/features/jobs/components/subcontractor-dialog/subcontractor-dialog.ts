import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HttpClient } from '@angular/common/http';
import {SubContractorService} from '../../../contractors/services/sub-contractor';

@Component({
  selector: 'app-subcontractor-dialog',
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
    <h2 mat-dialog-title style="margin-bottom: 10px;">Quick Add Subcontractor</h2>
    <mat-dialog-content [formGroup]="subContractorForm" style="display: flex; flex-direction: column; gap: 12px; min-width: 320px;">
      <mat-form-field appearance="outline">
        <mat-label>Company Name</mat-label>
        <input matInput formControlName="companyName">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Contact Name</mat-label>
        <input matInput formControlName="contactName">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Phone Number</mat-label>
        <input matInput formControlName="phoneNumber">
      </mat-form-field>

      <mat-form-field appearance="outline">
        <mat-label>Trades/Specialty</mat-label>
        <input matInput formControlName="trades" placeholder="e.g., Electrical, Plumbing">
      </mat-form-field>
    </mat-dialog-content>

    <mat-dialog-actions align="end" style="padding-bottom: 16px;">
      <button mat-button (click)="onCancel()">Cancel</button>
      <button mat-flat-button color="primary" [disabled]="subContractorForm.invalid || isSaving" (click)="onSave()">
        {{ isSaving ? 'Saving...' : 'Save Subcontractor' }}
      </button>
    </mat-dialog-actions>
  `
})
export class SubcontractorDialogComponent {
  subContractorForm: FormGroup;
  isSaving = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<SubcontractorDialogComponent>,
    private subContractorService: SubContractorService,
  ) {
    this.subContractorForm = this.fb.group({
      companyName: ['', Validators.required],
      contactName: [''],
      phoneNumber: [''],
      trades: ['']
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onSave(): void {
    if (this.subContractorForm.invalid) return;
    this.isSaving = true;


    this.subContractorService.createSubContractor(this.subContractorForm.value).subscribe({
      next: (newSub) => {
        this.dialogRef.close(newSub);
      },
      error: (err) => {
        console.error('Failed to quick create subcontractor', err);
        this.isSaving = false;
      }
    });
  }
}
