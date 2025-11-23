import { inject } from '@angular/core'; 
import { CanActivateFn, Router } from '@angular/router';
export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);  
  const token = (typeof window !== 'undefined') ? localStorage.getItem('authToken') : null;
  
  if (token && token.trim() !== '') {  
    return true; 
  }
  router.navigate(['/login']);
  return false; 
};
