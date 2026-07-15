import {inject, Injectable, Service} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from "../../../../environments/environment";


export interface StorageItem {
  name: string;
  fullName: string;
  isFolder: boolean;
}

export interface StorageResponse {
  folders: StorageItem[];
  files: StorageItem[];
}
@Injectable({
  providedIn: 'root'
})
export class FileBrowserService {
  private http = inject(HttpClient);
  private baseUrl = `${environment.apiUrl}/api/files`;


  getContents(prefix: string): Observable<StorageResponse>{
    const params = new HttpParams().set('prefix', prefix);
    return this.http.get<StorageResponse>(this.baseUrl, {params});
  }

  uploadFile(file: File, prefix: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('prefix', prefix);
    return this.http.post(`${this.baseUrl}/upload`, formData)
  }

  deleteFile(key: string): Observable<any> {
    const params = new HttpParams().set('key', key);
    return this.http.delete(`${this.baseUrl}/delete`, {params});
  }

  getDownloadLink(key: string): Observable<{ url: string }> {
    const params = new HttpParams().set('key', key);
    return this.http.get<{ url : string }>(`${this.baseUrl}/download`, {params});
  }



}
