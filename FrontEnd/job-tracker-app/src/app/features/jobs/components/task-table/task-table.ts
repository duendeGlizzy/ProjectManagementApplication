import {
  Component, ChangeDetectorRef, signal,
  Input, numberAttribute, OnChanges, SimpleChanges } from '@angular/core';
import {CurrencyPipe, NgClass, NgIf} from '@angular/common';
import {  RouterModule} from '@angular/router';
import {TaskService} from '../../services/task';

import { Task } from '../../models/task.model'

import { MatTableModule } from '@angular/material/table';
import {MatButton} from '@angular/material/button';


@Component({
  selector: 'app-task-table',
  standalone: true,
  imports: [
    CurrencyPipe,
    MatTableModule,
    NgIf,
    RouterModule,
    NgClass,
    MatButton,
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
