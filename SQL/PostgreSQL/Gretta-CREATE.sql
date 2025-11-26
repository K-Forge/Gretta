-- Eliminar la base de datos si existe y crearla nuevamente
DROP DATABASE IF EXISTS gretta;
CREATE DATABASE gretta;

-- Crear tipos enumerados
CREATE TYPE rol_usuario AS ENUM ('CLIENTE', 'ESTILISTA');
CREATE TYPE estado_cita AS ENUM ('PENDIENTE', 'CONFIRMADA', 'COMPLETADA', 'CANCELADA');
CREATE TYPE canal_comunicacion AS ENUM ('WHATSAPP', 'EMAIL', 'SMS');
CREATE TYPE estado_notificacion AS ENUM ('PENDIENTE', 'ENVIADA', 'ENTREGADA', 'FALLIDA');
CREATE TYPE tipo_mensaje AS ENUM ('CLIENTE', 'CHATBOT');
CREATE TYPE tipo_documento AS ENUM ('CEDULA', 'PASAPORTE', 'NIT', 'CEDULA_EXTRANJERIA');
CREATE TYPE tipo_notificacion AS ENUM ('RECORDATORIO', 'CONFIRMACION', 'PROMOCION', 'CANCELACION');

-- Crear tabla Usuarios
CREATE TABLE Usuarios (
    idUsuario SERIAL NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    apellido VARCHAR(40) NOT NULL,
    correo VARCHAR(255) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL CHECK (telefono ~ '^[0-9+\-\s()]+$'),
    contrasena VARCHAR(255) NOT NULL,
    tipoDocumento tipo_documento NOT NULL,
    numeroDocumento VARCHAR(50) NOT NULL UNIQUE,
    rol rol_usuario NOT NULL,
    canalPreferido canal_comunicacion NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaModificacion TIMESTAMP NULL,
    creadoPor INT NULL,
    modificadoPor INT NULL,
    PRIMARY KEY (idUsuario)
);

-- Crear tabla Cliente
CREATE TABLE Cliente (
    idCliente SERIAL NOT NULL,
    idUsuario INT NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idCliente)
);

-- Crear tabla Estilista
CREATE TABLE Estilista (
    idEstilista SERIAL NOT NULL,
    idUsuario INT NOT NULL,
    especialidad VARCHAR(100) NULL,
    disponibilidad VARCHAR(30) NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idEstilista)
);

-- Crear tabla Servicios
CREATE TABLE Servicios (
    idServicio SERIAL NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    duracion TIME NOT NULL,
    precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idServicio)
);

-- Crear tabla Promociones
CREATE TABLE Promociones (
    idPromocion SERIAL NOT NULL,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NULL,
    descuento DECIMAL(5,2) NOT NULL CHECK (descuento >= 0 AND descuento <= 100),
    fechaInicio TIMESTAMP NOT NULL,
    fechaFin TIMESTAMP NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (idPromocion),
    CHECK (fechaFin > fechaInicio)
);

-- Crear tabla Citas
CREATE TABLE Citas (
    idCita SERIAL NOT NULL,
    idCliente INT NOT NULL,
    idEstilista INT NOT NULL,
    idServicio INT NOT NULL,
    fechaCita TIMESTAMP NOT NULL,
    horaCita TIME NOT NULL,
    canalReserva canal_comunicacion NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fechaModificacion TIMESTAMP NULL,
    estado estado_cita NOT NULL DEFAULT 'PENDIENTE',
    observaciones TEXT NULL,
    PRIMARY KEY (idCita)
);

-- Crear tabla Notificaciones
CREATE TABLE Notificaciones (
    idNotificacion SERIAL NOT NULL,
    idUsuario INT NOT NULL,
    idCita INT NULL,
    idPromocion INT NULL,
    tipo tipo_notificacion NOT NULL,
    asunto VARCHAR(100) NOT NULL,
    mensaje TEXT NOT NULL,
    fechaEnvio TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado estado_notificacion NOT NULL DEFAULT 'PENDIENTE',
    PRIMARY KEY (idNotificacion)
);

-- Crear tabla HistorialCliente
CREATE TABLE HistorialCliente (
    idHistorial SERIAL NOT NULL,
    idCliente INT NOT NULL,
    idServicio INT NOT NULL,
    fechaServicio TIMESTAMP NOT NULL,
    PRIMARY KEY (idHistorial)
);

-- Foreign keys originales
ALTER TABLE Cliente
ADD CONSTRAINT FK_Cliente_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

ALTER TABLE Estilista
ADD CONSTRAINT FK_Estilista_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

ALTER TABLE Citas
ADD CONSTRAINT FK_Citas_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
ADD CONSTRAINT FK_Citas_Estilista FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_Citas_Servicio FOREIGN KEY (idServicio) REFERENCES Servicios(idServicio);

ALTER TABLE Notificaciones
ADD CONSTRAINT FK_Notificaciones_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
ADD CONSTRAINT FK_Notificaciones_Cita FOREIGN KEY (idCita) REFERENCES Citas(idCita),
ADD CONSTRAINT FK_Notificaciones_Promocion FOREIGN KEY (idPromocion) REFERENCES Promociones(idPromocion);

ALTER TABLE HistorialCliente
ADD CONSTRAINT FK_Historial_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
ADD CONSTRAINT FK_Historial_Servicio FOREIGN KEY (idServicio) REFERENCES Servicios(idServicio);

-- Nuevas tablas para productos y ventas
CREATE TABLE Productos (
    idProducto SERIAL NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (idProducto)
);

CREATE TABLE Ventas (
    idVenta SERIAL NOT NULL,
    idCliente INT NOT NULL,
    fechaVenta TIMESTAMP NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idVenta),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE DetalleVenta (
    idDetalle SERIAL NOT NULL,
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idDetalle),
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);

-- ========================================
-- Reestructurar Chatbot en Conversación y Mensajes
-- ========================================
CREATE TABLE ChatConversacion (
    idConversacion SERIAL PRIMARY KEY,
    idCliente INT NOT NULL,
    fechaInicio TIMESTAMP NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE ChatMensaje (
    idMensaje SERIAL PRIMARY KEY,
    idConversacion INT NOT NULL,
    tipoMensaje tipo_mensaje NOT NULL,
    contenido TEXT NOT NULL,
    fechaMensaje TIMESTAMP NOT NULL,
    FOREIGN KEY (idConversacion) REFERENCES ChatConversacion(idConversacion)
);

-- ========================================
-- Historial del estilista (visitas o trabajos vistos)
-- ========================================
CREATE TABLE HistorialEstilista (
    idHistorial SERIAL PRIMARY KEY,
    idEstilista INT NOT NULL,
    idCliente INT NOT NULL,
    fechaVisita TIMESTAMP NOT NULL,
    FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

-- ========================================
-- NUEVA TABLA: PromocionServicios
-- ========================================
CREATE TABLE PromocionServicios (
    idPromocionServicio SERIAL PRIMARY KEY,
    idPromocion INT NOT NULL,
    idServicio INT NOT NULL,
    FOREIGN KEY (idPromocion) REFERENCES Promociones(idPromocion),
    FOREIGN KEY (idServicio) REFERENCES Servicios(idServicio)
);
