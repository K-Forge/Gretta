-- ==========================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS Y TABLAS
-- Base de datos: Gretta
-- ==========================================

-- Eliminar la base de datos si existe y crearla nuevamente
DROP DATABASE IF EXISTS Gretta;
CREATE DATABASE Gretta;
USE Gretta;

-- ==========================================
-- TABLAS PRINCIPALES
-- ==========================================

-- Crear tabla Usuarios
CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    apellido VARCHAR(40) NOT NULL,
    correo VARCHAR(20) NULL,
    telefono BIGINT NOT NULL,
    contrasena VARCHAR(20) NOT NULL,
    rol CHAR NULL,
    canalPreferido VARCHAR(25) NULL,
    fechaRegistro DATETIME NULL,
    PRIMARY KEY (idUsuario)
);

-- Crear tabla Cliente
CREATE TABLE Cliente (
    idCliente INT NOT NULL,
    idUsuario INT NOT NULL,
    DocId INT NOT NULL,
    PRIMARY KEY (idCliente)
);

-- Crear tabla Estilista
CREATE TABLE Estilista (
    idEstilista INT NOT NULL,
    idUsuario INT NOT NULL,
    Especialidad VARCHAR(100) NULL,
    disponibilidad VARCHAR(30) NOT NULL,
    PRIMARY KEY (idEstilista)
);

-- Crear tabla Servicios
CREATE TABLE Servicios (
    idServicios INT NOT NULL,
    nombreServicios VARCHAR(20) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion TIME NOT NULL,
    precio INT NOT NULL,
    PRIMARY KEY (idServicios)
);

-- Crear tabla Promociones
CREATE TABLE Promociones (
    idPromocion INT NOT NULL,
    idServicios INT NOT NULL,
    titulo VARCHAR(25) NULL,
    description TEXT NULL,
    descuento NUMERIC NULL,
    servicioAsociado INT NULL,
    fechaInicio DATETIME NULL,
    fechaFin DATETIME NULL,
    PRIMARY KEY (idPromocion)
);

-- Crear tabla Citas
CREATE TABLE Citas (
    idCita INT NOT NULL,
    idCliente INT NOT NULL,
    idEstilista INT NOT NULL,
    idServicios INT NOT NULL,
    fechaCita DATETIME NOT NULL,
    horaCita TIME NOT NULL,
    canalReserva INT NOT NULL,
    fechaCreacion DATETIME NOT NULL,
    estado VARCHAR(40) NOT NULL,
    observaciones VARCHAR(40) NULL,
    PRIMARY KEY (idCita)
);

-- Crear tabla AgendaEstilista
CREATE TABLE AgendaEstilista (
    idAgenda INT NOT NULL,
    idEstilista INT NOT NULL,
    idCita INT NOT NULL,
    fecha DATETIME NOT NULL,
    horarioInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    disponibilidad DATETIME NOT NULL,
    PRIMARY KEY (idAgenda)
);

-- Crear tabla Notificaciones
CREATE TABLE Notificaciones (
    idNotificacion INT NOT NULL,
    idUsuario INT NOT NULL,
    idCita INT NULL,
    idPromociones INT NULL,
    tipo VARCHAR(30) NULL,
    asunto VARCHAR(25) NULL,
    mensaje TEXT NULL, 
    fechaEnvio DATETIME NULL,
    estado VARCHAR(20) NULL,
    PRIMARY KEY (idNotificacion)
);

-- Crear tabla ChatbotWhatsApp
CREATE TABLE ChatbotWhatsApp (
    idChat INT NOT NULL,
    idCita INT NOT NULL,
    idNotificacion INT NOT NULL,
    mensajeCliente VARCHAR(40) NOT NULL,
    respuestaChatbot VARCHAR(40) NOT NULL,
    fechaMensaje DATETIME NOT NULL,
    PRIMARY KEY (idChat)
);

-- Crear tabla HistorialCliente
CREATE TABLE HistorialCliente (
    idHistorial INT NOT NULL,
    idUsuario INT NOT NULL,
    idServicios INT NOT NULL,
    fechaServicio DATETIME NOT NULL,
    PRIMARY KEY (idHistorial)
);

-- Crear tabla Productos
CREATE TABLE Productos (
    idProducto INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (idProducto)
);

-- Crear tabla Ventas
CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT NOT NULL,
    idCliente INT NOT NULL,
    fechaVenta DATETIME NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idVenta)
);

-- Crear tabla DetalleVenta
CREATE TABLE DetalleVenta (
    idDetalle INT AUTO_INCREMENT NOT NULL,
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idDetalle)
);

-- ==========================================
-- TABLAS AUXILIARES Y DE CATÁLOGO
-- ==========================================

-- Catálogo de tipos de notificación
CREATE TABLE TipoNotificacion (
    idTipoNotificacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

-- Reestructuración de Chatbot: Conversación
CREATE TABLE ChatConversacion (
    idConversacion INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    fechaInicio DATETIME NOT NULL
);

-- Reestructuración de Chatbot: Mensajes
CREATE TABLE ChatMensaje (
    idMensaje INT AUTO_INCREMENT PRIMARY KEY,
    idConversacion INT NOT NULL,
    tipoMensaje ENUM('Cliente', 'Chatbot') NOT NULL,
    contenido TEXT NOT NULL,
    fechaMensaje DATETIME NOT NULL
);

-- Portafolio del estilista
CREATE TABLE PortafolioEstilista (
    idPortafolio INT AUTO_INCREMENT PRIMARY KEY,
    idEstilista INT NOT NULL,
    idCliente INT NOT NULL,
    fechaVisita DATETIME NOT NULL
);

-- Tabla de relación Promoción-Servicios
CREATE TABLE PromocionServicios (
    idPromocionServicio INT AUTO_INCREMENT PRIMARY KEY,
    idPromocion INT NOT NULL,
    idServicio INT NOT NULL
);

-- ==========================================
-- CLAVES FORÁNEAS - TABLAS PRINCIPALES
-- ==========================================

-- Foreign keys de Cliente
ALTER TABLE Cliente
ADD CONSTRAINT FK_Cliente_Usuario 
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

-- Foreign keys de Estilista
ALTER TABLE Estilista
ADD CONSTRAINT FK_Estilista_Usuario 
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

-- Foreign keys de Promociones
ALTER TABLE Promociones
ADD CONSTRAINT FK_Promociones_Servicios 
    FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

-- Foreign keys de Citas
ALTER TABLE Citas
ADD CONSTRAINT FK_Citas_Cliente 
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
ADD CONSTRAINT FK_Citas_Estilista 
    FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_Citas_Servicios 
    FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

-- Foreign keys de AgendaEstilista
ALTER TABLE AgendaEstilista
ADD CONSTRAINT FK_AgendaEstilista_Estilista 
    FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_AgendaEstilista_Cita 
    FOREIGN KEY (idCita) REFERENCES Citas(idCita);

-- Foreign keys de Notificaciones
ALTER TABLE Notificaciones
ADD CONSTRAINT FK_Notificaciones_Usuario 
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
ADD CONSTRAINT FK_Notificaciones_Cita 
    FOREIGN KEY (idCita) REFERENCES Citas(idCita),
ADD CONSTRAINT FK_Notificaciones_Promociones 
    FOREIGN KEY (idPromociones) REFERENCES Promociones(idPromocion);

-- Foreign keys de ChatbotWhatsApp
ALTER TABLE ChatbotWhatsApp
ADD CONSTRAINT FK_Chatbot_Cita 
    FOREIGN KEY (idCita) REFERENCES Citas(idCita),
ADD CONSTRAINT FK_Chatbot_Notificacion 
    FOREIGN KEY (idNotificacion) REFERENCES Notificaciones(idNotificacion);

-- Foreign keys de HistorialCliente
ALTER TABLE HistorialCliente
ADD CONSTRAINT FK_Historial_Usuario 
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
ADD CONSTRAINT FK_Historial_Servicios 
    FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

-- Foreign keys de Ventas
ALTER TABLE Ventas
ADD CONSTRAINT FK_Ventas_Cliente
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente);

-- Foreign keys de DetalleVenta
ALTER TABLE DetalleVenta
ADD CONSTRAINT FK_DetalleVenta_Venta
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
ADD CONSTRAINT FK_DetalleVenta_Producto
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto);

-- ==========================================
-- CLAVES FORÁNEAS - TABLAS AUXILIARES
-- ==========================================

-- Foreign key para TipoNotificacion
ALTER TABLE Notificaciones
ADD COLUMN idTipoNotificacion INT NULL,
ADD CONSTRAINT FK_Notificaciones_Tipo
    FOREIGN KEY (idTipoNotificacion) REFERENCES TipoNotificacion(idTipoNotificacion);

-- Foreign keys de ChatConversacion
ALTER TABLE ChatConversacion
ADD CONSTRAINT FK_ChatConversacion_Cliente
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente);

-- Foreign keys de ChatMensaje
ALTER TABLE ChatMensaje
ADD CONSTRAINT FK_ChatMensaje_Conversacion
    FOREIGN KEY (idConversacion) REFERENCES ChatConversacion(idConversacion);

-- Foreign keys de PortafolioEstilista
ALTER TABLE PortafolioEstilista
ADD CONSTRAINT FK_PortafolioEstilista_Estilista
    FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_PortafolioEstilista_Cliente
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente);

-- Foreign keys de PromocionServicios
ALTER TABLE PromocionServicios
ADD CONSTRAINT FK_PromocionServicios_Promocion
    FOREIGN KEY (idPromocion) REFERENCES Promociones(idPromocion),
ADD CONSTRAINT FK_PromocionServicios_Servicio
    FOREIGN KEY (idServicio) REFERENCES Servicios(idServicios);
