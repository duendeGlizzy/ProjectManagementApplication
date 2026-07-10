import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient, HttpParams } from "@angular/common/http";
import { PrimeContractor} from '../models/prime-contractor.model';
import {environment} from '../../../../environments/environment';


@Injectable({
  providedIn: "root",
})
export class PrimeContractorService {

  private apiUrl = `${environment.apiUrl}/api/prime-contractors`;

  constructor(private http: HttpClient) {}

  getAllPrimeContractors(): Observable<PrimeContractor[]> {
    return this.http.get<PrimeContractor[]>(this.apiUrl)
  }

  getPrimeContractor(id: number): Observable<PrimeContractor> {
    return this.http.get<PrimeContractor>(`${this.apiUrl}/${id}`)
  }

  createPrimeContractor(primeContractor: PrimeContractor): Observable<PrimeContractor> {
    return this.http.post<PrimeContractor>(this.apiUrl, primeContractor)
  }

  updatePrimeContractor(id: number, primeContractor: PrimeContractor): Observable<PrimeContractor> {
    return this.http.put<PrimeContractor>(`${this.apiUrl}/${id}`, primeContractor)
  }

  deletePrimeContractor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }



}
