import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { RolUsuario } from './core/models';

export const routes: Routes = [
  // Rutas públicas
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register/register.component').then(m => m.RegisterComponent)
  },

  // Rutas protegidas (requieren autenticación)
  {
    path: 'perfil',
    canActivate: [authGuard],
    loadComponent: () => import('./features/profile/profile.component').then(m => m.ProfileComponent)
  },

  // Rutas solo para ESTILISTA
  {
    path: 'dashboard',
    canActivate: [authGuard],
    data: { roles: [RolUsuario.ESTILISTA] },
    loadChildren: () => import('./features/dashboard/dashboard.routes').then(m => m.routes)
  },

  // Rutas para CLIENTE y ESTILISTA
  {
    path: 'citas',
    canActivate: [authGuard],
    loadChildren: () => import('./features/citas/citas.routes').then(m => m.routes)
  },

  // Página de no autorizado
  {
    path: 'unauthorized',
    loadComponent: () => import('./shared/components/unauthorized/unauthorized.component').then(m => m.UnauthorizedComponent)
  },

  // Redirecciones
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
