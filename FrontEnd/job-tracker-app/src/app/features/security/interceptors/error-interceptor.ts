import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../service/auth-service';
import {Router} from '@angular/router';
import {catchError, throwError} from 'rxjs';


export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if(error.status === 401){
        console.warn('session expired. re login!');

        authService.logout();

        router.navigate(['/login']);
      }
      return throwError(()=> error);

    })
  );
};
