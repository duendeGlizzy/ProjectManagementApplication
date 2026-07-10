import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import { Task } from '../models/task.model';
import {TaskStatus} from '../models/task-status.enum';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  private apiUrl = `${environment.apiUrl}/api/tasks`

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

  updateTask(taskId: number, task: Task, subContractorId?: number | null): Observable<Task> {
    let params = new HttpParams();
    if (subContractorId != null) {
      params = params.set('subContractorId', subContractorId.toString());
    }
    return this.http.put<Task>(`${this.apiUrl}/${taskId}/update`, task, {params});
  }

  updateStatus(id: number, status: TaskStatus): Observable<Task> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<Task>(`${this.apiUrl}/${id}/status`, JSON.stringify(status), { headers });  }

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

  getTasksByJobId(jobId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.apiUrl}/tasksByJob/${jobId}`)
  }




}
