import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div style="padding: 40px; text-align: center;">
      <h1>Dashboard</h1>
      <p>Panel de control del estilista</p>
      <p style="color: #666; margin-top: 20px;">Próximamente: Estadísticas y gráficos</p>
    </div>
  `,
  styles: [`
    h1 { color: #667eea; }
  `]
})
export class DashboardComponent {}
