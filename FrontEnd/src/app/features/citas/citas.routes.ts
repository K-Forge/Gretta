import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./lista-citas/lista-citas.component').then(m => m.ListaCitasComponent)
  }
];
