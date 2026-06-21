import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import { JobService } from '../../services/job';
import { Job } from '../../models/job.model';
import { Subscription } from 'rxjs';
import { MatTabsModule } from '@angular/material/tabs';

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
    MatIconModule, MatProgressSpinnerModule, RouterModule, TaskTable,
    MatTabsModule
  ],
  templateUrl: './job-details.html',
  styleUrl: './job-details.css',
})
export class JobDetails implements OnInit, OnDestroy {
  job: Job | null = null;
  isLoading = true;
  errorMessage = '';
  private routeSub!: Subscription;

  jobId!: number;
  paymentsSettled: number = 0;
  completedTasksCost: number = 0;
  outstandingTasksCost: number = 0;
  isRefreshing = false;

  constructor(
    private route: ActivatedRoute,
    private jobService: JobService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.routeSub = this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.jobId = +id; // Now it's explicitly assigned!
        this.loadJobData(this.jobId);

        // 💡 MOVE THIS HERE so it waits for the jobId to exist!
        this.refreshFinancialMetrics();
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

  progressJob(): void {
    if (this.job && this.job.jobId != null) {

      // 1. Progress from IN_QUEUE to IN_PROGRESS
      if (this.job.status === 'NOT_STARTED') {
        this.jobService.startJob(this.job.jobId).subscribe({
          next: () => {
            if (this.job && this.job.jobId != null) {
              this.loadJobData(this.job.jobId);
            }
          },
          error: (err) => {
            console.error("Unable to execute state progression interface command:", err);
          }
        });

        // 2. Progress from IN_PROGRESS to COMPLETED
      } else if (this.job.status === 'STARTED') {
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

  refreshFinancialMetrics(): void {
    this.isRefreshing = true;
    this.jobService.getJobFinancialBreakdown(this.jobId).subscribe({
      next: (data) => {
        this.paymentsSettled = data.totalPaymentsSettled;
        this.completedTasksCost = data.completedTaskCost;
        this.outstandingTasksCost = data.outstandingTaskCost;
        this.isRefreshing = false;
      },
      error: (err) => {
        console.error("Failed to refresh financial metrics", err);
        this.isRefreshing = false;
      }
    });
  }




}
