import { Component, OnInit, ChangeDetectorRef } from '@angular/core'; // 👈 Added ChangeDetectorRef
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { JobService } from '../../services/job';
import { ClientService } from '../../services/client';
import { PrimeContractorService } from '../../../contractors/services/prime-contractor';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ClientDialogComponent} from './client-form-dialog';
import { ContractorDialogComponent} from './PC-form-dialog';
import {MatTooltip} from '@angular/material/tooltip';

@Component({
  selector: 'app-job-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatDialogModule,
    MatTooltip
  ],
  templateUrl: './job-form.html',
  styleUrl: './job-form.css'
})
export class JobForm implements OnInit {
  jobForm!: FormGroup;
  isEditMode = false;
  jobId: number | null = null;
  isLoading = false;
  errorMessage = '';
  minDate: Date = new Date();

  clients: any[] = [];
  primeContractors: any[] = [];

  jobTypeOptions = [
    { value: 'GENERAL', label: 'General' },
    { value: 'DELEADING', label: 'De-Lead' },

  ];

  statusOptions = [
    { value: 'NOT_STARTED', label: 'Not Started' },
    { value: 'STARTED', label: 'started' },
    { value: 'IN_PROGRESS', label: 'In Progress' },
    { value: 'COMPLETED', label: 'Completed' }
  ];

  constructor(
    private fb: FormBuilder,
    private jobService: JobService,
    private clientService: ClientService,
    private primeContractorService: PrimeContractorService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadDropdownData();

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.jobId = Number(idParam);
      this.loadJobForEdit(this.jobId);
    }
  }

  private initForm(): void {
    this.jobForm = this.fb.group({
      description: ['', [Validators.required, Validators.maxLength(500)]],
      jobType: ['', [Validators.required]],
      address: ['', [Validators.required]],
      status: ['NOT_STARTED', [Validators.required]],
      estimatedCost: [null, [Validators.required, Validators.min(0)]],
      totalPayment: [0, [Validators.required, Validators.min(0)]],
      startDate: [null],
      endDate: [null],
      clientId: [null, [Validators.required]],
      primeContractorId: [null]
    }, {
      validators: this.dateRangeValidator
    });
  }

  //validator for date pickers
  private dateRangeValidator(group: FormGroup): {[key: string]: any} | null {
    const start = group.get('startDate')?.value;
    const end = group.get('endDate')?.value;

    if(start && end){
      const startDate = new Date(start);
      const endDate = new Date(end);

      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(0, 0, 0, 0);

      if(endDate < startDate){
        return { dateRangeInvalid: true };
      }
    }
    return null;
  }



  private loadDropdownData(): void {
    this.clientService.getClients().subscribe({
      next: (data) => {
        this.clients = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching clients:', err)
    });

    this.primeContractorService.getAllPrimeContractors().subscribe({
      next: (data) => {
        this.primeContractors = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching contractors:', err)
    });
  }

  private loadJobForEdit(id: number): void {
    this.isLoading = true;
    this.cdr.detectChanges();

    this.jobService.getJobById(id).subscribe({
      next: (job) => {
        this.jobForm.patchValue({
          description: job.description,
          jobType: job.jobType,
          address: job.address,
          status: job.status,
          estimatedCost: job.estimatedCost,
          totalPayment: job.totalPayment,
          startDate: job.startDate,
          endDate: job.endDate,
          clientId: job.client?.clientId,
          primeContractorId: job.primeContractor?.primeContractorId
        });
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Could not load job data profile.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    if (this.jobForm.invalid) {
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges();

    const formValues = this.jobForm.value;

    const jobPayload = {
      description: formValues.description,
      jobType: formValues.jobType,
      address: formValues.address,
      status: formValues.status,
      estimatedCost: formValues.estimatedCost,
      totalPayment: formValues.totalPayment,
      startDate: formValues.startDate,
      endDate: formValues.endDate
    };

    if (this.isEditMode && this.jobId) {
      this.jobService.updateJob(this.jobId, jobPayload, formValues.clientId, formValues.primeContractorId).subscribe({
        next: () => {
          this.router.navigate(['/jobs/', this.jobId]);
        },
        error: (err) => this.handleError(err)
      });
    } else {
      this.jobService.createNewJob(jobPayload, formValues.clientId, formValues.primeContractorId).subscribe({
        next: () => {
          this.router.navigate(['/jobs']);
        },
        error: (err) => this.handleError(err)
      });
    }
  }

  private handleError(err: any): void {
    console.error('CRITICAL SYSTEM TRANSACTION FAULT:', err);

    // Parse deep validation errors if the backend sent a specific response map message
    if (err.error && typeof err.error === 'string') {
      this.errorMessage = `Backend Error: ${err.error}`;
    } else if (err.error && err.error.message) {
      this.errorMessage = `Backend Error: ${err.error.message}`;
    } else {
      this.errorMessage = 'Database sync failure. Verify backend types match the model attributes.';
    }

    this.isLoading = false;
    this.cdr.detectChanges(); // Clear loading indicator and render the error box immediately
  }

  openNewClientDialog(): void {
    const dialogRef = this.dialog.open(ClientDialogComponent, {
      width: '400px',
      disableClose: true
    });
    dialogRef.afterClosed().subscribe(newClient => {
      if(newClient) {
        this.clients = [...this.clients, newClient];

        this.jobForm.patchValue({clientId: newClient.clientId});
        this.cdr.detectChanges();
      }
    });
  }

  openNewContractorDialog(): void {
    const dialogRef = this.dialog.open(ContractorDialogComponent, {
      width: '400px',
      disableClose: true
    });
    dialogRef.afterClosed().subscribe(newContractor => {
      if(newContractor) {
        this.primeContractors = [...this.primeContractors, newContractor];
        this.jobForm.patchValue({primeContractorId: newContractor.primeContractorId});
        this.cdr.detectChanges();
      }
    });
  }






}
