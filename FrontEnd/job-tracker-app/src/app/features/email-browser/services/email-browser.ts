import {inject, Injectable, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../../environments/environment';

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
  private baseUrl = `${environment.apiUrl}/api/emails`;

  getRecentEmails(): Observable<GraphEmailResponse> {
    return this.http.get<GraphEmailResponse>(this.baseUrl);
  }

}
