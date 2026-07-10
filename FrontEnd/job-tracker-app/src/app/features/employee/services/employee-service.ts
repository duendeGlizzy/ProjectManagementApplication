import {Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Employee} from '../models/employee-model';
import {environment} from "../../../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private apiUrl = `${environment.apiUrl}/api/employees`;

  constructor(private http: HttpClient) {}


  getLoggedInEmployee(): Observable<Employee>{
    return this.http.get<Employee>(this.apiUrl + '/me');
  }

  updateEmployee(employee: Employee): Observable<Employee>{
    return this.http.put<Employee>(this.apiUrl + '/me', employee);
  }


}
