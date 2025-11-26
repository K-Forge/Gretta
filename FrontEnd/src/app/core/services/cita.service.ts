import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApiService } from './api.service';
import { CitaResponse, CitaRequest, EstadoCita, Servicio, Usuario } from '../models';

@Injectable({
  providedIn: 'root'
})
export class CitaService {

  constructor(private apiService: ApiService) { }

  getCitasPorCliente(idCliente: number): Observable<CitaResponse[]> {
    return this.apiService.get<CitaResponse[]>(`citas/cliente/${idCliente}`);
  }

  getCita(id: number): Observable<CitaResponse> {
    return this.apiService.get<CitaResponse>(`citas/${id}`);
  }

  createCita(cita: CitaRequest): Observable<CitaResponse> {
    return this.apiService.post<CitaResponse>('citas', cita);
  }

  updateCita(id: number, cita: CitaRequest): Observable<CitaResponse> {
    return this.apiService.put<CitaResponse>(`citas/${id}`, cita);
  }

  cancelCita(id: number): Observable<CitaResponse> {
    return this.apiService.patch<CitaResponse>(`citas/${id}/estado`, {}, { estado: EstadoCita.CANCELADA });
  }

  // Helper methods to get data for the form
  getServiciosActivos(): Observable<Servicio[]> {
    return this.apiService.get<Servicio[]>('servicios/activos');
  }

  getEstilistas(): Observable<any[]> { // Returns Estilista[]
    return this.apiService.get<any[]>('estilistas');
  }
}
