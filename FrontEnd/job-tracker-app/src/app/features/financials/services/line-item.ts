import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { LineItem} from '../models/line-item.model';

@Injectable({
  providedIn: "root",
})

export class LineItemService {

  private apiUrl = 'http://localhost:8080/api/line-items';

  constructor(private http: HttpClient) {}

  getAllLineItems(): Observable<LineItem[]> {
    return this.http.get<LineItem[]>(this.apiUrl);
  }

  getLineItem(id: number): Observable<LineItem> {
    return this.http.get<LineItem>(`${this.apiUrl}/${id}`);
  }

  createLineItem(lineItem: LineItem, billId: number): Observable<LineItem> {
    let params = new HttpParams()
      .set('billId', billId.toString());
    return this.http.post<LineItem>(this.apiUrl, lineItem, {params});
  }

  updateLineItem(id: number, lineItem: LineItem): Observable<LineItem> {
    return this.http.put<LineItem>(`${this.apiUrl}/${id}`, lineItem);
  }

  deleteLineItem(id: number) {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getLineItemsByBillId(billId: number): Observable<LineItem[]> {
    return this.http.get<LineItem[]>(`${this.apiUrl}/search/${billId}`);

  }



}
