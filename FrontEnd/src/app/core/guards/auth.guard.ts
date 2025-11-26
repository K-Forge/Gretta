import { inject } from '@angular/core';
import { CanActivateFn, Router, ActivatedRouteSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { RolUsuario } from '../models';

/**
 * Guard de autenticación mejorado con validación de roles
 * 
 * Uso en rutas:
 * { path: 'admin', canActivate: [authGuard], data: { roles: [RolUsuario.ESTILISTA] } }
 * { path: 'perfil', canActivate: [authGuard] } // Solo requiere autenticación
 */
export const authGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  // Verificar si está autenticado
  if (!authService.isLoggedIn()) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // Verificar si el token está expirado
  if (authService.isTokenExpired()) {
    authService.logout();
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // Verificar roles si están especificados en la ruta
  const requiredRoles = route.data['roles'] as RolUsuario[] | undefined;
  
  if (requiredRoles && requiredRoles.length > 0) {
    const currentUser = authService.getCurrentUser();
    
    if (!currentUser || !requiredRoles.includes(currentUser.rol)) {
      // Usuario no tiene el rol requerido
      router.navigate(['/unauthorized']);
      return false;
    }
  }

  return true;
};
