import {inject, Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

export interface EmailMessage {
  id: string;
  subject: string;
  receivedDateTime: string;
  bodyPreview: string;
  from: {
    emailAddress: {
      name: string;
      address: string;
    };
  };
}

export interface GraphEmailResponse {
  value: EmailMessage[];
}

@Injectable({
  providedIn: 'root'
})
export class EmailBrowserService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/emails';

  getRecentEmails(): Observable<GraphEmailResponse> {
    return this.http.get<GraphEmailResponse>(this.baseUrl);
  }

}
