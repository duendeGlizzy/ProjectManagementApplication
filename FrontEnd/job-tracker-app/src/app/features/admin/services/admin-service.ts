import {inject, Injectable, Service} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Employee} from '../../employee/models/employee-model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/admins'

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.baseUrl);
  }

  getEmployeeById(id: number): Observable<Employee> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.get<Employee>(`${this.baseUrl}/id`, { params });
  }

  createEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.baseUrl, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    const params = new HttpParams().set('id', id.toString());
    return this.http.delete<void>(`${this.baseUrl}/id`, { params });
  }

  updateEmployeePassword(id: number, newPassword: string): Observable<Employee> {
    const params = new HttpParams().set('id', id.toString());

    const body = { password: newPassword };

    return this.http.put<Employee>(`${this.baseUrl}/password`, body, { params });
  }


}
