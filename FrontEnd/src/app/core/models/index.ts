// Enums
export enum RolUsuario {
  CLIENTE = 'CLIENTE',
  ESTILISTA = 'ESTILISTA'
}

export enum TipoDocumento {
  CEDULA = 'CEDULA',
  PASAPORTE = 'PASAPORTE',
  NIT = 'NIT',
  CEDULA_EXTRANJERIA = 'CEDULA_EXTRANJERIA'
}

export enum CanalComunicacion {
  WHATSAPP = 'WHATSAPP',
  EMAIL = 'EMAIL',
  SMS = 'SMS'
}

export enum EstadoCita {
  PENDIENTE = 'PENDIENTE',
  CONFIRMADA = 'CONFIRMADA',
  COMPLETADA = 'COMPLETADA',
  CANCELADA = 'CANCELADA'
}

export enum EstadoNotificacion {
  PENDIENTE = 'PENDIENTE',
  ENVIADA = 'ENVIADA',
  ENTREGADA = 'ENTREGADA',
  FALLIDA = 'FALLIDA'
}

export enum TipoNotificacion {
  RECORDATORIO = 'RECORDATORIO',
  CONFIRMACION = 'CONFIRMACION',
  PROMOCION = 'PROMOCION',
  CANCELACION = 'CANCELACION'
}

export enum TipoMensaje {
  CLIENTE = 'CLIENTE',
  CHATBOT = 'CHATBOT'
}

// Interfaces de Entidades
export interface Usuario {
  idUsuario: number;
  nombre: string;
  apellido: string;
  correo: string;
  telefono: string;
  tipoDocumento: TipoDocumento;
  numeroDocumento: string;
  rol: RolUsuario;
  canalPreferido?: CanalComunicacion;
  activo: boolean;
  fechaCreacion: Date;
  fechaModificacion?: Date;
}

export interface Cliente {
  idCliente: number;
  idUsuario: number;
  usuario?: Usuario;
  fechaCreacion: Date;
}

export interface Estilista {
  idEstilista: number;
  idUsuario: number;
  usuario?: Usuario;
  especialidad?: string;
  disponibilidad: string;
  fechaCreacion: Date;
}

export interface Servicio {
  idServicio: number;
  nombre: string;
  descripcion?: string;
  duracion: number;
  precio: number;
  activo: boolean;
  fechaCreacion: Date;
  fechaModificacion?: Date;
}

export interface Producto {
  idProducto: number;
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  stockMinimo: number;
  activo: boolean;
  fechaCreacion: Date;
  fechaModificacion?: Date;
}

export interface Promocion {
  idPromocion: number;
  titulo: string;
  descripcion?: string;
  descuento: number;
  fechaInicio: Date;
  fechaFin: Date;
  activo: boolean;
  fechaCreacion: Date;
  fechaModificacion?: Date;
}

export interface Cita {
  idCita: number;
  idCliente: number;
  idEstilista: number;
  idServicio: number;
  nombreCliente?: string;
  nombreEstilista?: string;
  nombreServicio?: string;
  fechaCita: Date;
  horaCita: string;
  canalReserva: CanalComunicacion;
  estado: EstadoCita;
  observaciones?: string;
  fechaCreacion: Date;
  fechaModificacion?: Date;
}

export interface DetalleVenta {
  idDetalle: number;
  idProducto: number;
  nombreProducto?: string;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
}

export interface Venta {
  idVenta: number;
  idCliente: number;
  nombreCliente?: string;
  fechaVenta: Date;
  total: number;
  detalles: DetalleVenta[];
}

export interface Notificacion {
  idNotificacion: number;
  idUsuario: number;
  tipo: TipoNotificacion;
  titulo: string;
  mensaje: string;
  canal: CanalComunicacion;
  estado: EstadoNotificacion;
  fechaCreacion: Date;
  fechaEnvio?: Date;
}

export interface ChatMensaje {
  idMensaje: number;
  idConversacion: number;
  contenido: string;
  tipo: TipoMensaje;
  fechaEnvio: Date;
}

export interface ChatConversacion {
  idConversacion: number;
  idCliente: number;
  nombreCliente?: string;
  fechaInicio: Date;
  fechaUltimoMensaje?: Date;
  activa: boolean;
  mensajes?: ChatMensaje[];
}

// DTOs de Request
export interface LoginRequest {
  correo: string;
  contrasena: string;
}

export interface RegisterRequest {
  nombre: string;
  apellido: string;
  correo: string;
  telefono: string;
  contrasena: string;
  tipoDocumento: TipoDocumento;
  numeroDocumento: string;
  rol: RolUsuario;
  canalPreferido?: CanalComunicacion;
  especialidad?: string;
  disponibilidad?: string;
}

export interface CitaRequest {
  idCliente: number;
  idEstilista: number;
  idServicio: number;
  fechaCita: string;
  horaCita: string;
  canalReserva: CanalComunicacion;
  estado?: EstadoCita;
  observaciones?: string;
}

export interface ProductoRequest {
  nombre: string;
  descripcion?: string;
  precio: number;
  stock: number;
  stockMinimo: number;
  activo: boolean;
}

export interface VentaRequest {
  idCliente: number;
  detalles: DetalleVentaRequest[];
}

export interface DetalleVentaRequest {
  idProducto: number;
  cantidad: number;
}

export interface PromocionRequest {
  titulo: string;
  descripcion?: string;
  descuento: number;
  fechaInicio: string;
  fechaFin: string;
  activo: boolean;
}

export interface ServicioRequest {
  nombre: string;
  descripcion?: string;
  duracion: number;
  precio: number;
  activo: boolean;
}

export interface ActualizarPerfilRequest {
  nombre: string;
  apellido: string;
  telefono: string;
  canalPreferido?: CanalComunicacion;
}

export interface CambiarContrasenaRequest {
  contrasenaActual: string;
  nuevaContrasena: string;
}

// DTOs de Response
export interface AuthResponse {
  token: string;
  usuario: Usuario;
  idCliente?: number;
  idEstilista?: number;
}

export interface DashboardStats {
  totalClientes: number;
  totalCitas: number;
  totalVentas: number;
  ingresosDelMes: number;
}

export interface CitaResponse {
  idCita: number;
  idCliente: number;
  nombreCliente: string;
  idEstilista: number;
  nombreEstilista: string;
  idServicio: number;
  nombreServicio: string;
  fechaCita: string; // LocalDateTime comes as string
  horaCita: string;
  canalReserva: CanalComunicacion;
  estado: EstadoCita;
  observaciones: string;
  fechaCreacion: string;
  fechaModificacion: string;
}
