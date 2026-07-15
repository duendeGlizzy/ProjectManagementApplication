import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Payment} from '../models/payment.model';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private apiUrl = `${environment.apiUrl}/api/payments`;

  constructor(private http: HttpClient) {}

  getAllPayments(): Observable<Payment[]> {
    return this.http.get<Payment[]>(this.apiUrl);
  }

  getPaymentById(id: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/${id}`);
  }

  processPayment(payment: Payment, billId: number, taskId: number): Observable<Payment> {
    let params = new HttpParams()
      .set('billId', billId.toString())
      .set('taskId', taskId.toString());

    return this.http.post<Payment>(this.apiUrl, payment, {params});
  }

  uploadPaymentWithCheck(formData: FormData, jobId: number): Observable<Payment> {
    let params = new HttpParams()
      .set('jobId', jobId.toString());

    return this.http.post<Payment>(this.apiUrl, formData, {params});

  }







}
