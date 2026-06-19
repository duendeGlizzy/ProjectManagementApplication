import { NgFor, NgIf, CurrencyPipe } from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';

// Angular Material Imports
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {TaskService} from '../../services/task';
import {SubContractorService} from '../../../contractors/services/sub-contractor';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-task-form',
  standalone: true,
  imports: [
    NgFor,
    NgIf,
    CurrencyPipe,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatDatepickerModule,
    MatNativeDateModule

  ],
  templateUrl: './task-form.html',
  styleUrl: './task-form.css',
})
export class TaskForm implements OnInit {
  taskForm!: FormGroup;
  isEditMode = false;
  taskId: number | null = null;
  jobId: number | null = null;
  isLoading: boolean = false;
  errorMessage = '';
  minDate: Date = new Date();

  subContractors: any[] = [];

  statusOptions = [
    {value: 'IN_QUEUE', label: 'In Queue'},
    {value: 'STARTED', label: 'started'},
    {value: 'IN_PROGRESS', label: 'In Progress'},
    {value: 'COMPLETED', label: 'Completed'}
  ];

  constructor(private fb: FormBuilder,
              private taskService: TaskService,
              private subContractorService: SubContractorService,
              private route: ActivatedRoute,
              private router: Router,
              private cdr: ChangeDetectorRef,
              private dialog: MatDialog,) {
  }

  ngOnInit() {

    const jobParam = this.route.snapshot.queryParamMap.get('jobId');
    if (jobParam) {
      this.jobId = Number(jobParam);
    }

    this.initForm();
    this.loadDropdownData();


    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.isEditMode = true;
      this.taskId = Number(idParam);
      this.loadTaskForEdit(this.taskId);
    }
  }

  private initForm(): void {
    this.taskForm = this.fb.group({
      description: ['', [Validators.required, Validators.maxLength(500)]],
      isSubContracted: [false, [Validators.required,]],
      status: ['IN_QUEUE', [Validators.required]],
      totalPrice: [null, [Validators.required, Validators.min(0)]],
      payRoll: [0, [Validators.required, Validators.min(0)]],
      startDate: [null],
      endDate: [null],
      subContractorId: [null],
    }, {
      validators: this.dateRangeValidator
    });
  }

  //validator for date pickers
  private dateRangeValidator(group: FormGroup): { [key: string]: any } | null {
    const start = group.get('startDate')?.value;
    const end = group.get('endDate')?.value;

    if (start && end) {
      const startDate = new Date(start);
      const endDate = new Date(end);

      startDate.setHours(0, 0, 0, 0);
      endDate.setHours(0, 0, 0, 0);

      if (endDate < startDate) {
        return {dateRangeInvalid: true};
      }
    }
    return null;
  }

  private loadDropdownData(): void {
    this.subContractorService.getAllSubContractors().subscribe({
      next: (data) => {
        this.subContractors = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.error('Error fetching contractors:', err)
    });
  }

  private loadTaskForEdit(id: number): void {
    this.isLoading = true;
    this.cdr.detectChanges();

    this.taskService.getTaskById(id).subscribe({
      next: (task) => {

        if(task.job?.jobId){
          this.jobId = task.job.jobId;
        }

        this.taskForm.patchValue({
          description: task.description,
          isSubContracted: task.isSubContracted,
          status: task.status,
          totalPrice: task.totalPrice,
          payRoll: task.payRoll,
          startDate: task.startDate,
          endDate: task.endDate,
          subContractorId: task.subContractor?.subContractorId,
        });
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.errorMessage = 'Could not load task data profile.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    if (this.taskForm.invalid) {
      return;
    }

    if(!this.isEditMode && !this.jobId){
      this.errorMessage = 'Please enter valid job id';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges();

    const formValues = this.taskForm.value;

    const taskPayload = {
      description: formValues.description,
      isSubContracted: formValues.isSubContracted,
      status: formValues.status,
      totalPrice: formValues.totalPrice,
      payRoll: formValues.payRoll,
      startDate: formValues.startDate,
      endDate: formValues.endDate,
      jobId: this.jobId
    };
    if (this.isEditMode && this.taskId) {
      this.taskService.updateTask(this.taskId, taskPayload, formValues.subContractorId).subscribe({
        next: () => {
          this.router.navigate(['/tasks', this.taskId], {queryParams: {jobId: this.jobId}
          });
        },
        error: (err) => this.handleError(err)
      });
    } else if(this.jobId) {
      this.taskService.createTask(taskPayload, formValues.subContractorId, this.jobId).subscribe({
        next: () => {
          this.router.navigate(['/jobs', this.jobId]);
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




}
