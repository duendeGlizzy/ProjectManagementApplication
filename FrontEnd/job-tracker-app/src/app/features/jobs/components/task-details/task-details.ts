import {Component, OnInit, ChangeDetectorRef, Input, numberAttribute} from '@angular/core';
import {CurrencyPipe, DatePipe, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import {MatCardModule } from '@angular/material/card';
import {MatChip, MatChipSet} from '@angular/material/chips';
import {MatIcon} from '@angular/material/icon';
import {MatProgressSpinner} from '@angular/material/progress-spinner';
import {RouterLink} from '@angular/router';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import {Subscription} from 'rxjs';
import {TaskService} from '../../services/task';
import { Task } from '../../models/task.model';
import { TaskStatus} from '../../models/task-status.enum';


@Component({
  selector: 'app-task-details',
  standalone: true,
  imports: [
    CurrencyPipe, DatePipe, MatButton, MatCardModule, MatChip,
    MatChipSet, MatIcon, MatProgressSpinner, NgIf, RouterLink,
     RouterModule
  ],
  templateUrl: './task-details.html',
  styleUrl: './task-details.css',
})
export class TaskDetails implements OnInit {

  jobId: number | undefined;
  task: Task | null = null;
  isLoading = true;
  errorMessage = '';
  routeSub!: Subscription;

  constructor(private taskService: TaskService,
              private router: Router,
              private route: ActivatedRoute,
              private cdr: ChangeDetectorRef,) {
  }

  ngOnInit() {
    this.routeSub = this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      const taskId = Number(idParam);

      if (taskId && !isNaN(taskId)) {
        this.loadTaskData(taskId);
      } else {
        this.errorMessage = 'Invalid Task Identifier';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });

    this.route.queryParamMap.subscribe(queryParams => {
      const jobParam = queryParams.get('jobId');
      if (jobParam) {
        this.jobId = Number(jobParam);
      }
    });
  }


  ngOnDestroy() {
    this.routeSub.unsubscribe();
  }


  private loadTaskData(taskId: number) {
    this.isLoading = true;
    this.errorMessage = '';
    this.cdr.detectChanges();
    this.taskService.getTaskById(taskId).subscribe({
      next: (data: Task) => {
        this.task = data;
        this.isLoading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        console.error('api Error', err)
        this.errorMessage = 'Invalid Task Identifier';
        this.task = null;
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }


  calculateTimeToComplete(): number {
    if (!this.task || !this.task.endDate || !this.task.startDate) {
      return 0;
    }
    const startDate: Date = new Date(this.task.startDate);
    startDate.setHours(0, 0, 0, 0);
    const endDate: Date = new Date(this.task.endDate);
    endDate.setHours(0, 0, 0, 0);
    const currentDate: Date = new Date()
    startDate.setHours(0, 0, 0, 0);

    let timeDifferenceMs = 0;


    if (currentDate > startDate) {
      timeDifferenceMs = endDate.getTime() - currentDate.getTime();
    } else {
      timeDifferenceMs = endDate.getTime() - startDate.getTime();
    }
    const days = Math.ceil(timeDifferenceMs / (1000 * 60 * 60 * 24));
    return days < 0 ? 0 : days;
  }


  progressTask(status: TaskStatus) {
    if (this.task && this.task.taskId != null) {
      if (this.task.status === "IN_QUEUE") {
        this.taskService.updateStatus(this.task.taskId, "STARTED").subscribe({
          next: () => {
            if (this.task && this.task.taskId != null) {
              this.loadTaskData(this.task.taskId);
            }
          },
          error: (err) => {
            console.error("Unable to execute state progression interface command:", err);
          },
        });
      } else if (this.task.status === "STARTED") {
        this.taskService.updateStatus(this.task.taskId, "COMPLETED").subscribe({
          next: () => {
            if (this.task && this.task.taskId != null) {
              this.loadTaskData(this.task.taskId);
            }
          },
          error: (err) => {
            console.error("Unable to execute state progression interface command:", err);
          }
        });
      }
    }
  }


}

