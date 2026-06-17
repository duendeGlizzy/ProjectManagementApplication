import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { JobService } from '../../services/job';
import { Job } from '../../models/job.model';
import { Subscription } from 'rxjs';

import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { JobStatus } from '../../models/job-status.enum';
import {FormGroup} from '@angular/forms';
import {TaskTable} from '../task-table/task-table';

@Component({
  selector: 'app-job-details',
  standalone: true,
  imports: [
    CommonModule, MatCardModule, MatButtonModule, MatChipsModule,
    MatIconModule, MatProgressSpinnerModule, RouterModule, TaskTable
  ],
  templateUrl: './job-details.html',
  styleUrl: './job-details.css',
})
export class JobDetails implements OnInit, OnDestroy {
  job: Job | null = null;
  isLoading = true;
  errorMessage = '';
  private routeSub!: Subscription;

  constructor(
    private route: ActivatedRoute,
    private jobService: JobService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    // Listen to parameter changes from the router mapping link
    this.routeSub = this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      const jobId = Number(idParam);

      if (jobId && !isNaN(jobId)) {
        this.loadJobData(jobId);
      } else {
        this.errorMessage = 'Invalid Job Identifier';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  ngOnDestroy() {
    if (this.routeSub) {
      this.routeSub.unsubscribe();
    }
  }

  loadJobData(jobId: number): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges(); // Render spinner immediately

    this.jobService.getJobById(jobId).subscribe({
      next: (data: Job) => {
        this.job = data;
        this.isLoading = false;
        this.cdr.detectChanges(); // Push refreshed data details into template fields
      },
      error: (err) => {
        console.error('API Retrieval Error:', err);
        this.errorMessage = 'Job details could not be parsed or found.';
        this.job = null;
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  progressJob(status: JobStatus) {
    if (this.job && this.job.jobId != null) {
      if (this.job.status === "NOT_STARTED") {
        this.jobService.startJob(this.job.jobId).subscribe({
          next: () => {
            if (this.job && this.job.jobId != null) {
              this.loadJobData(this.job.jobId);
            }
          },
          error: (err) => {
            console.error("Unable to execute state progression interface command:", err);
          },
        });
      }else if (this.job.status === "STARTED") {
        this.jobService.completeJob(this.job.jobId).subscribe({
          next: () => {
            if (this.job && this.job.jobId != null) {
              this.loadJobData(this.job.jobId);
            }
          },
          error: (err) => {
            console.error("Unable to execute state progression interface command:", err);
          }
        });
      }
    }
  }

  calculateTimeToComplete(): number{
    if(!this.job || !this.job.endDate || !this.job.startDate){
      return 0;
    }
    const startDate: Date = new Date(this.job.startDate);
    startDate.setHours(0, 0, 0, 0);
    const endDate: Date = new Date(this.job.endDate);
    endDate.setHours(0, 0, 0, 0);
    const currentDate: Date = new Date()
    startDate.setHours(0, 0, 0, 0);

    let timeDifferenceMs = 0;


    if(currentDate > startDate){
      timeDifferenceMs = endDate.getTime() - currentDate.getTime();
    }else{
      timeDifferenceMs = endDate.getTime() - startDate.getTime();
    }
    const days = Math.ceil(timeDifferenceMs /(1000*60*60*24));
    return days < 0 ? 0:days;
  }



}
