import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpClient } from '@angular/common/http';
import { Vendor } from '../models/vendor.model'
import {environment} from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})

export class VendorService {
  private apiUrl = `${environment.apiUrl}/api/vendors`;

  constructor(private http: HttpClient) {}

  getAllVendors(): Observable<Vendor[]> {
    return this.http.get<Vendor[]>(this.apiUrl);
  }

  getVendorById(id: number): Observable<Vendor> {
    return this.http.get<Vendor>(`${this.apiUrl}/${id}`)
  }

  createVendor(vendor: Vendor): Observable<Vendor> {
    return this.http.post<Vendor>(this.apiUrl, vendor)
  }

  updateVendor(vendor: Vendor, id: number): Observable<Vendor> {
    return this.http.put<Vendor>(`${this.apiUrl}/${id}`, vendor)
  }

  deleteVendor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
  }


}
