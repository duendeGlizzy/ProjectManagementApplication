import {inject, Injectable, Service} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Licence} from '../models/licence-model';

@Injectable({
  providedIn: 'root'
  })
export class LicenceService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/licences';


  getLicencesByEmployeeId(employeeId: number): Observable<Licence[]> {
    const params = new HttpParams().set('id', employeeId.toString());
    return this.http.get<Licence[]>(`${this.baseUrl}/employee/id`, { params });
  }

  getLicenceById(licenceId: number): Observable<Licence> {
    const params = new HttpParams().set('id', licenceId.toString());
    return this.http.get<Licence>(this.baseUrl, { params });
  }

  createLicence(licence: Licence, employeeId: number): Observable<Licence> {
    const params = new HttpParams().set('id', employeeId.toString());
    return this.http.post<Licence>(`${this.baseUrl}/id`, licence, { params });
  }

  updateLicence(licence: Licence, licenceId: number): Observable<Licence> {
    const params = new HttpParams().set('id', licenceId.toString());
    return this.http.put<Licence>(`${this.baseUrl}/id`, licence, { params });
  }

  deleteLicenceById(licenceId: number): Observable<void> {
    const params = new HttpParams().set('id', licenceId.toString());
    return this.http.delete<void>(`${this.baseUrl}/id`, { params });
  }

}
