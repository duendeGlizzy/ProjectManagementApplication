import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable, tap} from 'rxjs';
import {Router} from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';

  private currentEmployeeSubject = new BehaviorSubject<any>(null);
  private router = inject(Router);

  public currentEmployee$ = this.currentEmployeeSubject.asObservable();

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login`, credentials, {withCredentials: true}).pipe(
      tap(response =>  this.handleAuthenticationSuccess(response, 'EMPLOYEE'))
    );
  }

  adminLogin(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/login/admin`, credentials, {withCredentials: true}).pipe(
      tap(response =>  this.handleAuthenticationSuccess(response, 'ADMIN'))
    );
  }


  logout() {
    localStorage.removeItem('is_logged_in');
    localStorage.removeItem('user_email');
    this.currentEmployeeSubject.next(null);
    this.router.navigate(['homepage']);
  }

  isLoggedIn(): boolean {
    return localStorage.getItem('is_logged_in') === 'true';
  }

  updateGlobalEmployee(employee: any) {
    this.currentEmployeeSubject.next(employee);
  }


  private handleAuthenticationSuccess(response: any, fallbackRole: 'EMPLOYEE' | 'ADMIN') {
    if(response) {
      localStorage.setItem('is_logged_in', 'true');
      localStorage.setItem('user_email', response.email);

      const userRole = response.role || fallbackRole;

      if(userRole === 'ADMIN') {
        this.currentEmployeeSubject.next({
          firstName: 'System',
          lastName: 'Administrator',
          email: response.email
        });
        this.router.navigate(['/admin-dashboard']);
      }else{
        this.currentEmployeeSubject.next({
          firstName: 'Loading',
          lastName: 'Profile...',
          email: response.email
        });
        this.router.navigate(['/jobs'])
      }
    }
  }
}
