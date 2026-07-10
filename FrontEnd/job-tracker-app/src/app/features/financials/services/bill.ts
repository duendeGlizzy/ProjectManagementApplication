import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient,HttpParams } from "@angular/common/http";
import { Bill} from '../models/bill.model';
import {BillStatus} from '../models/bill-status.enum';
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: "root",
})

export class BillService {

  private apiUrl = `${environment.apiUrl}/api/bills`;

  constructor(private http: HttpClient) {}

  getAllBills(): Observable<Bill[]> {
    return this.http.get<Bill[]>(this.apiUrl)
  }

  getBillById(id: number): Observable<Bill> {
    return this.http.get<Bill>(`${this.apiUrl}/${id}`)
  }

  createBill(bill: Bill, vendorId: number, taskId: number): Observable<Bill> {

    let params = new HttpParams()
      .set('vendorId', vendorId.toString())
      .set('taskId', taskId.toString());

    return this.http.post<Bill>(this.apiUrl, bill, {params});
  }

  updateBill(id: number, bill: Bill): Observable<Bill> {
    return this.http.put<Bill>(`${this.apiUrl}/${id}`, bill)
  }

  updateBillStatus(id: number, status: BillStatus): Observable<Bill> {
    let params = new HttpParams()
      .set('status', status.toString());
    return this.http.put<Bill>(`${this.apiUrl}/${id}/updateStatus`, {}, {params});

  }
  uploadBillWithReceipt(formData: FormData, vendorId: number, jobId: number): Observable<Bill> {
    let params = new HttpParams()
      .set('vendorId', vendorId.toString())
      .set('jobId', jobId.toString());

    return this.http.post<Bill>(this.apiUrl, formData, { params });
  }


}
