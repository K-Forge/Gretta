import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../core/services/auth.service';
import { ApiService } from '../../core/services/api.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  currentView: string = 'resumen';
  data: any[] = [];
  loading: boolean = false;
  
  // Modal state
  showModal: boolean = false;
  isEditing: boolean = false;
  selectedItem: any = {};
  modalTitle: string = '';
  searchTerm: string = '';

  // Column definitions for each view
  config: any = {
    usuarios: {
      table: [
        { key: 'idUsuario', label: 'ID' },
        { key: 'nombre', label: 'Nombre' },
        { key: 'apellido', label: 'Apellido' },
        { key: 'correo', label: 'Correo' },
        { key: 'rol', label: 'Rol' }
      ],
      form: [
        { key: 'nombre', label: 'Nombre', type: 'text' },
        { key: 'apellido', label: 'Apellido', type: 'text' },
        { key: 'correo', label: 'Correo', type: 'email' },
        { key: 'telefono', label: 'Teléfono', type: 'text' },
        { 
          key: 'tipoDocumento', 
          label: 'Tipo Documento', 
          type: 'select',
          options: [
            { value: 'CEDULA', label: 'Cédula' },
            { value: 'PASAPORTE', label: 'Pasaporte' },
            { value: 'NIT', label: 'NIT' },
            { value: 'CEDULA_EXTRANJERIA', label: 'Cédula de Extranjería' }
          ]
        },
        { key: 'numeroDocumento', label: 'No. Documento', type: 'text' },
        { 
          key: 'rol', 
          label: 'Rol', 
          type: 'select',
          options: [
            { value: 'CLIENTE', label: 'Cliente' },
            { value: 'ESTILISTA', label: 'Estilista' }
          ]
        }
      ]
    },
    citas: {
      table: [
        { key: 'idCita', label: 'ID' },
        { key: 'fechaCita', label: 'Fecha' },
        { key: 'horaCita', label: 'Hora' },
        { key: 'estado', label: 'Estado' },
        { key: 'nombreCliente', label: 'Cliente' }
      ],
      form: [
        { key: 'fechaCita', label: 'Fecha', type: 'date' },
        { key: 'horaCita', label: 'Hora', type: 'time' },
        { 
          key: 'estado', 
          label: 'Estado', 
          type: 'select',
          options: [
            { value: 'PENDIENTE', label: 'Pendiente' },
            { value: 'CONFIRMADA', label: 'Confirmada' },
            { value: 'COMPLETADA', label: 'Completada' },
            { value: 'CANCELADA', label: 'Cancelada' }
          ]
        },
        { key: 'observaciones', label: 'Observaciones', type: 'text' },
        { key: 'idCliente', label: 'ID Cliente', type: 'number' },
        { key: 'idEstilista', label: 'ID Estilista', type: 'number' },
        { key: 'idServicio', label: 'ID Servicio', type: 'number' }
      ]
    },
    servicios: {
      table: [
        { key: 'idServicio', label: 'ID' },
        { key: 'nombre', label: 'Nombre' },
        { key: 'precio', label: 'Precio' },
        { key: 'duracion', label: 'Duración' }
      ],
      form: [
        { key: 'nombre', label: 'Nombre', type: 'text' },
        { key: 'descripcion', label: 'Descripción', type: 'text' },
        { key: 'precio', label: 'Precio', type: 'number' },
        { key: 'duracion', label: 'Duración (HH:mm:ss)', type: 'text' }
      ]
    },
    promociones: {
      table: [
        { key: 'idPromocion', label: 'ID' },
        { key: 'titulo', label: 'Título' },
        { key: 'descuento', label: 'Descuento %' },
        { key: 'fechaFin', label: 'Vence' }
      ],
      form: [
        { key: 'titulo', label: 'Título', type: 'text' },
        { key: 'descripcion', label: 'Descripción', type: 'text' },
        { key: 'descuento', label: 'Descuento %', type: 'number' },
        { key: 'fechaInicio', label: 'Fecha Inicio', type: 'date' },
        { key: 'fechaFin', label: 'Fecha Fin', type: 'date' }
      ]
    },
    productos: {
      table: [
        { key: 'idProducto', label: 'ID' },
        { key: 'nombre', label: 'Nombre' },
        { key: 'precio', label: 'Precio' },
        { key: 'stock', label: 'Stock' }
      ],
      form: [
        { key: 'nombre', label: 'Nombre', type: 'text' },
        { key: 'descripcion', label: 'Descripción', type: 'text' },
        { key: 'precio', label: 'Precio', type: 'number' },
        { key: 'stock', label: 'Stock', type: 'number' }
      ]
    },
    ventas: {
      table: [
        { key: 'idVenta', label: 'ID' },
        { key: 'fechaVenta', label: 'Fecha' },
        { key: 'total', label: 'Total' },
        { key: 'nombreCliente', label: 'Cliente' }
      ],
      form: [
        { key: 'fechaVenta', label: 'Fecha', type: 'datetime-local' },
        { key: 'total', label: 'Total', type: 'number' },
        { key: 'idCliente', label: 'ID Cliente', type: 'number' }
      ]
    }
  };

  constructor(
    private authService: AuthService,
    private apiService: ApiService
  ) {}

  ngOnInit() {
    this.loadData();
  }

  setView(view: string) {
    this.currentView = view;
    this.searchTerm = ''; // Clear search when switching views
    this.loadData();
  }

  getTitle(): string {
    switch(this.currentView) {
      case 'resumen': return 'Resumen General';
      case 'usuarios': return 'Gestión de Usuarios';
      case 'citas': return 'Gestión de Citas';
      case 'servicios': return 'Catálogo de Servicios';
      case 'promociones': return 'Promociones Activas';
      case 'productos': return 'Inventario de Productos';
      case 'ventas': return 'Registro de Ventas';
      default: return 'Dashboard';
    }
  }

  loadData() {
    if (this.currentView === 'resumen') return;

    this.loading = true;
    // Map view names to API endpoints if they differ
    const endpoint = this.currentView; 
    
    this.apiService.get<any[]>(endpoint).subscribe({
      next: (response) => {
        this.data = response;
        this.filterData();
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading data', err);
        this.loading = false;
        this.data = [];
        this.filterData();
      }
    });
  }

  getColumns() {
    return this.config[this.currentView]?.table || [];
  }

  getFormFields() {
    return this.config[this.currentView]?.form || [];
  }

  filteredData: any[] = [];

  filterData() {
    if (!this.searchTerm) {
      this.filteredData = [...this.data];
      return;
    }
    const lowerTerm = this.searchTerm.toLowerCase();
    this.filteredData = this.data.filter(item => {
      const columns = this.getColumns();
      return columns.some((col: any) => {
        const val = item[col.key];
        return (val !== null && val !== undefined) ? String(val).toLowerCase().includes(lowerTerm) : false;
      });
    });
  }


  // CRUD Operations
  crearNuevo() {
    this.isEditing = false;
    this.selectedItem = {};
    this.modalTitle = `Nuevo ${this.getTitle().split(' ').pop()}`; // Simple heuristic for title
    this.showModal = true;
  }

  editarItem(item: any) {
    this.isEditing = true;
    this.selectedItem = { ...item }; // Clone to avoid direct mutation
    
    // Format time for input if needed
    if (this.currentView === 'citas') {
      if (this.selectedItem.horaCita) {
        const time = this.selectedItem.horaCita.toString();
        if (time.length > 5) {
          this.selectedItem.horaCita = time.substring(0, 5); // HH:mm
        }
      }
      if (this.selectedItem.fechaCita) {
        this.selectedItem.fechaCita = this.selectedItem.fechaCita.toString().split('T')[0];
      }
    }

    this.modalTitle = `Editar ${this.getTitle().split(' ').pop()}`;
    this.showModal = true;
  }

  eliminarItem(item: any) {
    if (!confirm('¿Estás seguro de que deseas eliminar este elemento?')) return;

    const idKey = this.getIdKey();
    const id = item[idKey];
    
    this.apiService.delete(`${this.currentView}/${id}`).subscribe({
      next: () => {
        this.loadData(); // Reload data
        alert('Elemento eliminado correctamente');
      },
      error: (err) => {
        console.error('Error deleting item', err);
        if (err.error?.detalle?.includes('viola la llave foránea')) {
          alert('No se puede eliminar este elemento porque está siendo utilizado en otros registros (ej. tiene citas asociadas).');
        } else {
          alert('Error al eliminar el elemento: ' + (err.error?.mensaje || 'Intente nuevamente'));
        }
      }
    });
  }

  guardarItem() {
    const idKey = this.getIdKey();
    
    // Prepare data for sending
    const dataToSend = { ...this.selectedItem };

    // Fix time format for Citas (Backend expects HH:mm:ss)
    if (this.currentView === 'citas') {
      if (dataToSend.horaCita) {
        const time = dataToSend.horaCita.toString();
        if (time.length === 5) { // HH:mm
          dataToSend.horaCita = time + ':00';
        }
      }
      // Fix date format for Citas (Backend expects LocalDateTime)
      if (dataToSend.fechaCita) {
        const date = dataToSend.fechaCita.toString();
        if (date.length === 10) { // YYYY-MM-DD
          dataToSend.fechaCita = date + 'T00:00:00';
        }
      }
    }

    if (this.isEditing) {
      const id = this.selectedItem[idKey];
      this.apiService.put(`${this.currentView}/${id}`, dataToSend).subscribe({
        next: () => {
          this.closeModal();
          this.loadData();
          alert('Actualizado correctamente');
        },
        error: (err) => {
          console.error('Error updating', err);
          alert('Error al actualizar: ' + (err.error?.mensaje || 'Intente nuevamente'));
        }
      });
    } else {
      this.apiService.post(this.currentView, dataToSend).subscribe({
        next: () => {
          this.closeModal();
          this.loadData();
          alert('Creado correctamente');
        },
        error: (err) => {
          console.error('Error creating', err);
          alert('Error al crear: ' + (err.error?.mensaje || 'Intente nuevamente'));
        }
      });
    }
  }

  closeModal() {
    this.showModal = false;
    this.selectedItem = {};
  }

  private getIdKey(): string {
    // Helper to get the ID property name based on the view
    switch(this.currentView) {
      case 'usuarios': return 'idUsuario';
      case 'citas': return 'idCita';
      case 'servicios': return 'idServicio';
      case 'promociones': return 'idPromocion';
      case 'productos': return 'idProducto';
      case 'ventas': return 'idVenta';
      default: return 'id';
    }
  }

  logout() {
    this.authService.logout();
  }
}
