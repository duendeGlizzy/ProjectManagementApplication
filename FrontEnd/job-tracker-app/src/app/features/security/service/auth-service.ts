import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, credentials, {withCredentials: true}).pipe(
      tap(response => {
        if(response) {
          localStorage.setItem('is_logged_in', 'true');
          localStorage.setItem('user_email', response.email);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('is_logged_in');
    localStorage.removeItem('user_email');
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('is_logged_in') === 'true';
  }






}
