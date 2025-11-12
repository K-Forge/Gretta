-- Eliminar la base de datos si existe y crearla nuevamente
DROP DATABASE IF EXISTS gretta;
CREATE DATABASE gretta;
\c gretta;

-- Crear tabla Usuarios
CREATE TABLE Usuarios (
    idUsuario SERIAL NOT NULL,
    nombre VARCHAR(40) NOT NULL,
    apellido VARCHAR(40) NOT NULL,
    correo VARCHAR(20) NULL,
    telefono BIGINT NOT NULL,
    contrasena VARCHAR(20) NOT NULL,
    rol CHAR NULL,
    canalPreferido VARCHAR(25) NULL,
    fechaRegistro TIMESTAMP NULL,
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
    fechaInicio TIMESTAMP NULL,
    fechaFin TIMESTAMP NULL,
    PRIMARY KEY (idPromocion)
);

-- Crear tabla Citas
CREATE TABLE Citas (
    idCita INT NOT NULL,
    idCliente INT NOT NULL,
    idEstilista INT NOT NULL,
    idServicios INT NOT NULL,
    fechaCita TIMESTAMP NOT NULL,
    horaCita TIME NOT NULL,
    canalReserva INT NOT NULL,
    fechaCreacion TIMESTAMP NOT NULL,
    estado VARCHAR(40) NOT NULL,
    observaciones VARCHAR(40) NULL,
    PRIMARY KEY (idCita)
);

-- Crear tabla AgendaEstilista
CREATE TABLE AgendaEstilista (
    idAgenda INT NOT NULL,
    idEstilista INT NOT NULL,
    idCita INT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    horarioInicio TIME NOT NULL,
    horaFin TIME NOT NULL,
    disponibilidad TIMESTAMP NOT NULL,
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
    fechaEnvio TIMESTAMP NULL,
    estado VARCHAR(20) NULL,  -- estado de la cita (reservada, confirmada, en curso, etc...)
    PRIMARY KEY (idNotificacion)
);

-- Crear tabla ChatbotWhatsApp
CREATE TABLE ChatbotWhatsApp (
    idChat INT NOT NULL,
    idCita INT NOT NULL,
    idNotificacion INT NOT NULL,
    mensajeCliente VARCHAR(40) NOT NULL,
    respuestaChatbot VARCHAR(40) NOT NULL,
    fechaMensaje TIMESTAMP NOT NULL,
    PRIMARY KEY (idChat)
);

-- Crear tabla HistorialCliente
CREATE TABLE HistorialCliente (
    idHistorial INT NOT NULL,
    idUsuario INT NOT NULL,
    idServicios INT NOT NULL,
    fechaServicio TIMESTAMP NOT NULL,
    PRIMARY KEY (idHistorial)
);

-- Foreign keys originales
ALTER TABLE Cliente
ADD CONSTRAINT FK_Cliente_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

ALTER TABLE Estilista
ADD CONSTRAINT FK_Estilista_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario);

ALTER TABLE Promociones
ADD CONSTRAINT FK_Promociones_Servicios FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

ALTER TABLE Citas
ADD CONSTRAINT FK_Citas_Cliente FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente),
ADD CONSTRAINT FK_Citas_Estilista FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_Citas_Servicios FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

ALTER TABLE AgendaEstilista
ADD CONSTRAINT FK_AgendaEstilista_Estilista FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
ADD CONSTRAINT FK_AgendaEstilista_Cita FOREIGN KEY (idCita) REFERENCES Citas(idCita);

ALTER TABLE Notificaciones
ADD CONSTRAINT FK_Notificaciones_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
ADD CONSTRAINT FK_Notificaciones_Cita FOREIGN KEY (idCita) REFERENCES Citas(idCita),
ADD CONSTRAINT FK_Notificaciones_Promociones FOREIGN KEY (idPromociones) REFERENCES Promociones(idPromocion);

ALTER TABLE ChatbotWhatsApp
ADD CONSTRAINT FK_Chatbot_Cita FOREIGN KEY (idCita) REFERENCES Citas(idCita),
ADD CONSTRAINT FK_Chatbot_Notificacion FOREIGN KEY (idNotificacion) REFERENCES Notificaciones(idNotificacion);

ALTER TABLE HistorialCliente
ADD CONSTRAINT FK_Historial_Usuario FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario),
ADD CONSTRAINT FK_Historial_Servicios FOREIGN KEY (idServicios) REFERENCES Servicios(idServicios);

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
-- NUEVAS TABLAS PARA AJUSTE AL TALLER 4
-- ========================================

-- Catálogo de tipos de notificación
CREATE TABLE TipoNotificacion (
    idTipoNotificacion SERIAL PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

ALTER TABLE Notificaciones
ADD COLUMN idTipoNotificacion INT NULL,
ADD CONSTRAINT FK_Notificaciones_Tipo
    FOREIGN KEY (idTipoNotificacion) REFERENCES TipoNotificacion(idTipoNotificacion);

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
    tipoMensaje VARCHAR(10) NOT NULL CHECK (tipoMensaje IN ('Cliente', 'Chatbot')),
    contenido TEXT NOT NULL,
    fechaMensaje TIMESTAMP NOT NULL,
    FOREIGN KEY (idConversacion) REFERENCES ChatConversacion(idConversacion)
);

-- ========================================
-- Portafolio del estilista (visitas o trabajos vistos)
-- ========================================
CREATE TABLE PortafolioEstilista (
    idPortafolio SERIAL PRIMARY KEY,
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
    FOREIGN KEY (idServicio) REFERENCES Servicios(idServicios)
);
