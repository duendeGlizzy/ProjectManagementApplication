import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { JobService } from '../../services/job';
import { Job } from '../../models/job.model'
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatChipsModule } from '@angular/material/chips';

@Component({
  selector: 'app-job-dashboard',
  standalone: true,
  imports: [CommonModule, MatChipsModule,
    MatTableModule, MatButtonModule],
  templateUrl: './job-dashboard.html',
  styleUrl: './job-dashboard.css',
})
export class JobDashboard implements OnInit {
  jobsList = signal<Job[]>([]);
  errorMessage = signal<string>('');

  displayedColumns: string[] = [
    'jobId',
    'description',
    'jobType',
    'estimatedCost',
    'totalPayment',
    'client',
    'primeContractor',
    'status',
    'actions'
  ];

  constructor(private jobService: JobService) { }

  ngOnInit() {
    this.loadAllJobs();
  }

  loadAllJobs() {
    this.jobService.getAllJobs().subscribe({
      next: (data: Job[]) => {
        this.jobsList.set(data);
        this.errorMessage.set('')
      },
      error: err => {
        this.errorMessage.set('Failed to load jobs');
        console.error('API Error: ', err);
      }
    });
  }

  startJob(jobId: number) {
    this.jobService.startJob(jobId).subscribe({
      next: () => this.loadAllJobs(),
      error: err => console.error('failed to start project',err)
    });
  }



}
