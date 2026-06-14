import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Job } from '../models/job.model';
import { JobStatus } from '../models/job-status.enum';
import { Task } from '../models/task.model';

@Injectable({
  providedIn: 'root'
})
export class JobService {

  private apiUrl = 'http://localhost:8080/api/jobs'

  constructor(private http: HttpClient) { }

  getAllJobs(): Observable<Job[]> {
    return this.http.get<Job[]>(`${this.apiUrl}`);
  }

  getJobById(id: number): Observable<Job> {
    return this.http.get<Job>(`${this.apiUrl}/${id}`)
  }

  createNewJob(job: Job, clientID: number, primeContractorId: number): Observable<Job> {
    const params = new HttpParams()
      .set('clientId', clientID.toString())
      .set('primeContractorId', primeContractorId.toString())

    return this.http.post<Job>(this.apiUrl, job, {params});
  }

  startJob(id: number): Observable<Job> {
    return this.http.put<Job>(`${this.apiUrl}/${id}/start`, {})
  }

  updateJob(id: number, job: Job, clientID: number, primeContractorId: number): Observable<Job> {
    const params = new HttpParams()
    .set('clientId', clientID.toString())
    .set('primeContractorId', primeContractorId.toString());
    return this.http.put<Job>(`${this.apiUrl}/${id}/update`, job, {params});
  }

  completeJob(id: number): Observable<Job> {
    return this.http.put<Job>(`${this.apiUrl}/${id}/complete`, {})
  }

  addTaskToJob(id: number, task: Task): Observable<Job> {
    return this.http.put<Job>(`${this.apiUrl}/${id}/addTask`, task)
  }

  searchJobs(filters: {lastName?: string, companyName?: string, status?: JobStatus}): Observable<Job[]> {
    let params = new HttpParams();

    if (filters.lastName) params = params.set('lastName', filters.lastName);
    if (filters.companyName) params = params.set('companyName', filters.companyName);
    if (filters.status) params = params.set('status', filters.status);
    return this.http.get<Job[]>(`${this.apiUrl}/search`, {params});
  }

  getJobTotalPayments(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/totalPayments`);
  }

  getJobTotalCost(id: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/${id}/totalCost`);
  }

  getJobNetProfit(id: number): Observable<number>{
    return this.http.get<number>(`${this.apiUrl}/${id}/netProfit`)
  }



}
