import {inject, Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileBrowserService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/files';

  getFiles(): Observable<string[]> {
    return this.http.get<string[]>(this.baseUrl);
  }

  getFileDownloadUrl(fileKey: string): Observable<{url: string}> {
    return this.http.get<{url: string}>(`${this.baseUrl}/download`, { params: { key : fileKey } });
  }


}
