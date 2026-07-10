import {Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';

export interface InvoiceRequestData{
  name: string;
  email: string;
  jobDetails: string;
}

@Injectable({
  providedIn: 'root'
})



export class PublicService {
  private baseUrl = `${environment.apiUrl}/api/public`;

  constructor(private http: HttpClient) {}

  requestInvoice(data: InvoiceRequestData): Observable<any> {
    return this.http.post(`${this.baseUrl}/request-invoice`, data);
  }




}

