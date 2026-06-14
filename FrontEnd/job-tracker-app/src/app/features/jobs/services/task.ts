import {Injectable} from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {Observable} from 'rxjs';
import { Task } from '../models/task.model';
import {TaskStatus} from '../models/task-status.enum';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = 'http://localhost:8080/api/tasks'

  constructor(private http: HttpClient) { }

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}`)
  }

  getTaskById(id: number): Observable<Task> {
    return this.http.get<Task>(`${this.apiUrl}/${id}`)
  }

  createTask(task: Task, subContractorId: number | null | undefined, jobId: number): Observable<Task> {
    let params = new HttpParams().set('jobId', jobId.toString())

      if (subContractorId != null) {
        params = params.set('subContractorId', subContractorId.toString())
      }

    return this.http.post<Task>(this.apiUrl, task, {params});
  }

  updateTask(taskId: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${taskId}/update`, task);
  }

  updateStatus(id: number, status: TaskStatus): Observable<Task> {
    return this.http.put<Task>(`${this.apiUrl}/${id}/status`, status);
  }

  deleteTask(id: number): Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTotalCost(taskId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${taskId}/totalCost`);
  }

  getTotalPayments(taskId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${taskId}/totalPayments`);
  }

  getNetProfit(taskId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${taskId}/netProfit`);

  }




}
