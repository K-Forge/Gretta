import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lista-citas',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 40px;">
      <h1>Mis Citas</h1>
      <p>Lista de citas agendadas</p>
      <p style="color: #666; margin-top: 20px;">Próximamente: Calendario y gestión de citas</p>
    </div>
  `,
  styles: [`
    h1 { color: #667eea; }
  `]
})
export class ListaCitasComponent {}
