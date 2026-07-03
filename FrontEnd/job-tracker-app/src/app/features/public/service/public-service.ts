import {Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface InvoiceRequestData{
  name: string;
  email: string;
  jobDetails: string;
}

@Injectable({
  providedIn: 'root'
})



export class PublicService {
  private baseUrl = 'http://localhost:8080/api/public';

  constructor(private http: HttpClient) {}

  requestInvoice(data: InvoiceRequestData): Observable<any> {
    return this.http.post(`${this.baseUrl}/request-invoice`, data);
  }




}

