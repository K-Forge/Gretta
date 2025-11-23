import { HttpInterceptorFn } from '@angular/common/http';
import { catchError } from 'rxjs/operators'; 
import { throwError } from 'rxjs';
export const httpInterceptor: HttpInterceptorFn = (req, next) => {

  const token = (typeof window !== 'undefined') ? localStorage.getItem('authToken') : null;
  
  if (token && token.trim() !== '') {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  return next(req).pipe(
    catchError((error) => {
      console.error('HTTP Error:', error);
      return throwError(() => error);
    })
  );
};
