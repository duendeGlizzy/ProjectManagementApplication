import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { SubContractor } from '../models/sub-contractor.model';


@Injectable({
  providedIn: "root",
})

export class SubContractorService {

  private apiUrl = 'http://localhost:8080/api/sub-contractors';

  constructor(private http: HttpClient) {}

  getAllSubContractors(): Observable<SubContractor[]> {
    return this.http.get<SubContractor[]>(this.apiUrl)
  }

  getSubContractorById(id: number): Observable<SubContractor> {
    return this.http.get<SubContractor>(`${this.apiUrl}/${id}`)
  }

  createSubContractor(subContractor: SubContractor): Observable<SubContractor> {
    return this.http.post<SubContractor>(this.apiUrl, subContractor)
  }

  updateSubContractor(subContractor: SubContractor, id: number): Observable<SubContractor> {
    return this.http.put<SubContractor>(`${this.apiUrl}/${id}`, subContractor)
  }

  updateSubContractorPrice(price: number, id: number): Observable<SubContractor> {
    return this.http.put<SubContractor>(`${this.apiUrl}/${id}/updatePrice`, price)
  }

  deleteSubContractor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }


}
