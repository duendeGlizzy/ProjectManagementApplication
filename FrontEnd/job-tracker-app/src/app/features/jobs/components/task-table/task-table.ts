import {
  Component, OnInit, ChangeDetectorRef, OnDestroy, signal,
  Input, numberAttribute, OnChanges, SimpleChanges } from '@angular/core';
import {CurrencyPipe, NgClass, NgIf} from '@angular/common';
import {MatButton} from '@angular/material/button';
import { ActivatedRoute, RouterModule, Router } from '@angular/router';
import {TaskService} from '../../services/task';
import {Job} from '../../models/job.model';
import {Subscription} from 'rxjs';
import { Task } from '../../models/task.model'

import { MatTableModule } from '@angular/material/table';


@Component({
  selector: 'app-task-table',
  standalone: true,
  imports: [
    CurrencyPipe,
    MatButton,
    MatTableModule,
    NgIf,
    RouterModule,
    NgClass,
  ],
  templateUrl: './task-table.html',
  styleUrl: './task-table.css',
})
export class TaskTable implements OnChanges {
  taskList = signal<Task[]>([]) ;
   errorMessage= '';

  @Input({transform: numberAttribute}) jobId!: number;

  statusOptions = [
    { value: 'IN_QUEUE', label: 'In Queue'},
    { value: 'STARTED', label: 'Started'},
    {value: 'COMPLETED', label: 'Completed'},
  ]

  displayedColumns: string[] = [
    'taskId',
    'description',
    'totalPrice',
    'isSubContracted',
    'status',
  ];

  constructor(private taskService: TaskService,
              private cdr: ChangeDetectorRef,) {}

  ngOnChanges(changes:SimpleChanges): void {

    if(changes['jobId'] && this.jobId && !isNaN(this.jobId)) {
      this.loadTasks(this.jobId)
    }
  }



  private loadTasks(jobId: number) {
    this.errorMessage = '';

    this.taskService.getTasksByJobId(jobId).subscribe({
      next: (data: Task[]) =>{
        this.taskList.set(data);
        this.cdr.detectChanges();
      },
      error: error => {
        console.error('api error');
        this.errorMessage = 'error finding tasks';
        this.cdr.detectChanges();
      }
    });
  }




}
