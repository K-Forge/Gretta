import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormsModule } from '@angular/forms';
import { CitaService } from '../../../core/services/cita.service';
import { AuthService } from '../../../core/services/auth.service';
import { CitaResponse, CitaRequest, EstadoCita, CanalComunicacion, Servicio } from '../../../core/models';

@Component({
  selector: 'app-lista-citas',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule],
  templateUrl: './lista-citas.component.html',
  styleUrls: ['./lista-citas.component.scss']
})
export class ListaCitasComponent implements OnInit {
  citas: CitaResponse[] = [];
  filteredCitas: CitaResponse[] = [];
  searchTerm: string = '';
  servicios: Servicio[] = [];
  estilistas: any[] = [];
  
  showModal = false;
  isEditing = false;
  selectedCitaId: number | null = null;
  citaForm: FormGroup;
  
  constructor(
    private citaService: CitaService,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.citaForm = this.fb.group({
      idServicio: ['', Validators.required],
      idEstilista: ['', Validators.required],
      fechaCita: ['', Validators.required],
      horaCita: ['', [Validators.required, Validators.pattern(/^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$/)]],
      observaciones: ['']
    });
  }

  ngOnInit(): void {
    this.loadCitas();
    this.loadCatalogos();
  }

  loadCitas(): void {
    const clientId = this.authService.getClientId();
    if (clientId) {
      this.citaService.getCitasPorCliente(clientId).subscribe({
        next: (data) => {
          this.citas = data;
          this.filterCitas();
        },
        error: (err) => console.error('Error loading citas', err)
      });
    }
  }

  filterCitas(): void {
    if (!this.searchTerm) {
      this.filteredCitas = [...this.citas];
      return;
    }
    const lower = this.searchTerm.toLowerCase();
    this.filteredCitas = this.citas.filter(cita => 
      (cita.nombreServicio && cita.nombreServicio.toLowerCase().includes(lower)) ||
      (cita.nombreEstilista && cita.nombreEstilista.toLowerCase().includes(lower)) ||
      (cita.estado && cita.estado.toLowerCase().includes(lower)) ||
      (cita.fechaCita && cita.fechaCita.toString().includes(lower))
    );
  }

  loadCatalogos(): void {
    this.citaService.getServiciosActivos().subscribe(data => this.servicios = data);
    this.citaService.getEstilistas().subscribe(data => this.estilistas = data);
  }

  openCreateModal(): void {
    this.isEditing = false;
    this.selectedCitaId = null;
    this.citaForm.reset();
    this.showModal = true;
  }

  openEditModal(cita: CitaResponse): void {
    this.isEditing = true;
    this.selectedCitaId = cita.idCita;
    
    // Format date and time for input
    // Use string manipulation to avoid timezone issues with Date object
    const dateStr = cita.fechaCita.toString().split('T')[0];
    
    // Ensure HH:mm format for time input
    let timeStr = cita.horaCita;
    // If format is H:mm (e.g. 9:00), add leading zero
    if (timeStr.indexOf(':') === 1) {
      timeStr = '0' + timeStr;
    }
    // If format has seconds (HH:mm:ss), take first 5 chars
    if (timeStr.length > 5) {
      timeStr = timeStr.substring(0, 5);
    }
    
    this.citaForm.patchValue({
      idServicio: cita.idServicio,
      idEstilista: cita.idEstilista,
      fechaCita: dateStr,
      horaCita: timeStr,
      observaciones: cita.observaciones
    });
    
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
  }

  onSubmit(): void {
    if (this.citaForm.invalid) {
      console.log('Form invalid', this.citaForm.errors);
      return;
    }

    const formValue = this.citaForm.value;
    const clientId = this.authService.getClientId();
    
    if (!clientId) {
      alert('Error: No se pudo identificar al cliente. Por favor, inicie sesión nuevamente.');
      return;
    }

    // Combine date and time
    const fechaHora = `${formValue.fechaCita}T${formValue.horaCita}:00`;

    let estado = EstadoCita.PENDIENTE;
    if (this.isEditing && this.selectedCitaId) {
      const originalCita = this.citas.find(c => c.idCita === this.selectedCitaId);
      if (originalCita) {
        estado = originalCita.estado;
      }
    }

    const request: CitaRequest = {
      idCliente: clientId,
      idEstilista: Number(formValue.idEstilista),
      idServicio: Number(formValue.idServicio),
      fechaCita: fechaHora,
      horaCita: `${formValue.horaCita}:00`,
      canalReserva: CanalComunicacion.WHATSAPP,
      observaciones: formValue.observaciones,
      estado: estado
    };

    console.log('Sending request', request);

    if (this.isEditing && this.selectedCitaId) {
      this.citaService.updateCita(this.selectedCitaId, request).subscribe({
        next: () => {
          this.loadCitas();
          this.closeModal();
          alert('Cita actualizada con éxito');
        },
        error: (err) => {
          console.error('Error updating cita', err);
          if (err.error?.detalle?.includes('entre las 8:00 AM y 8:00 PM')) {
            alert('Alerta, no se puede agendar cita porque se encuentra fuera del horario del estilista');
          } else {
            alert('Error al actualizar la cita: ' + (err.error?.mensaje || 'Intente nuevamente'));
          }
        }
      });
    } else {
      this.citaService.createCita(request).subscribe({
        next: () => {
          this.loadCitas();
          this.closeModal();
          alert('Cita agendada con éxito');
        },
        error: (err) => {
          console.error('Error creating cita', err);
          if (err.error?.detalle?.includes('entre las 8:00 AM y 8:00 PM')) {
            alert('Alerta, no se puede agendar cita porque se encuentra fuera del horario del estilista');
          } else {
            alert('Error al agendar la cita: ' + (err.error?.mensaje || 'Intente nuevamente'));
          }
        }
      });
    }
  }

  cancelarCita(): void {
    if (this.selectedCitaId) {
      if (confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
        this.citaService.cancelCita(this.selectedCitaId).subscribe({
          next: () => {
            this.loadCitas();
            this.closeModal();
          },
          error: (err) => console.error('Error cancelling cita', err)
        });
      }
    }
  }
  
  getEstadoClass(estado: string): string {
    switch (estado) {
      case 'CONFIRMADA': return 'confirmada';
      case 'PENDIENTE': return 'pendiente';
      case 'CANCELADA': return 'cancelada';
      case 'COMPLETADA': return 'completada';
      default: return '';
    }
  }
}
