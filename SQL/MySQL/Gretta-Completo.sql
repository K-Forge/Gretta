-- Eliminar la base de datos si existe y crearla nuevamente
DROP DATABASE IF EXISTS Gretta;
CREATE DATABASE Gretta;
USE Gretta;

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
    idProducto INT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    descripcion TEXT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    PRIMARY KEY (idProducto)
);

CREATE TABLE Ventas (
    idVenta INT AUTO_INCREMENT NOT NULL,
    idCliente INT NOT NULL,
    fechaVenta DATETIME NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idVenta),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE DetalleVenta (
    idDetalle INT AUTO_INCREMENT NOT NULL,
    idVenta INT NOT NULL,
    idProducto INT NOT NULL,
    cantidad INT NOT NULL,
    precioUnitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (idDetalle),
    FOREIGN KEY (idVenta) REFERENCES Ventas(idVenta),
    FOREIGN KEY (idProducto) REFERENCES Productos(idProducto)
);
-- ====================
-- 1. Usuarios (10 registros)
-- ====================
INSERT INTO Usuarios VALUES
(1, 'Juan', 'Pérez', 'juanp@example.com', 3001111111, 'clave1', 'C', 'WhatsApp', NOW()),
(2, 'María', 'López', 'marial@example.com', 3001111112, 'clave2', 'C', 'Email', NOW()),
(3, 'Pedro', 'Gómez', 'pedrog@example.com', 3001111113, 'clave3', 'E', 'SMS', NOW()),
(4, 'Laura', 'Martínez', 'lauram@example.com', 3001111114, 'clave4', 'C', 'WhatsApp', NOW()),
(5, 'Ana', 'Torres', 'anat@example.com', 3001111115, 'clave5', 'E', 'Email', NOW()),
(6, 'Carlos', 'Ruiz', 'carlosr@example.com', 3001111116, 'clave6', 'C', 'SMS', NOW()),
(7, 'Lucía', 'Hernández', 'luciah@example.com', 3001111117, 'clave7', 'C', 'WhatsApp', NOW()),
(8, 'Andrés', 'Morales', 'andresm@example.com', 3001111118, 'clave8', 'E', 'Email', NOW()),
(9, 'Paula', 'Castro', 'paulac@example.com', 3001111119, 'clave9', 'C', 'WhatsApp', NOW()),
(10, 'Mateo', 'Ramírez', 'mateor@example.com', 3001111120, 'clave10', 'E', 'SMS', NOW()),
(11, 'Sofía', 'Vargas', 'sofiav@example.com', 3001111121, 'clave11', 'C', 'WhatsApp', NOW()),
(12, 'Julián', 'Mendoza', 'julianm@example.com', 3001111122, 'clave12', 'E', 'Email', NOW()),
(13, 'Camila', 'Ortega', 'camilao@example.com', 3001111123, 'clave13', 'C', 'SMS', NOW()),
(14, 'Diego', 'García', 'diegog@example.com', 3001111124, 'clave14', 'E', 'WhatsApp', NOW()),
(15, 'Valentina', 'Suárez', 'valen@example.com', 3001111125, 'clave15', 'C', 'Email', NOW()),
(16, 'Daniel', 'Mejía', 'danielm@example.com', 3001111126, 'clave16', 'C', 'SMS', NOW()),
(17, 'Isabella', 'Cortés', 'isabel@example.com', 3001111127, 'clave17', 'E', 'WhatsApp', NOW()),
(18, 'Tomás', 'Reyes', 'tomasr@example.com', 3001111128, 'clave18', 'C', 'Email', NOW()),
(19, 'Gabriela', 'Jiménez', 'gabrie@example.com', 3001111129, 'clave19', 'C', 'WhatsApp', NOW()),
(20, 'Santiago', 'Luna', 'santi@example.com', 3001111130, 'clave20', 'E', 'SMS', NOW()),
(21, 'Mariana', 'Rojas', 'marianar@example.com', 3001111131, 'clave21', 'C', 'WhatsApp', NOW()),
(22, 'Felipe', 'Díaz', 'feliped@example.com', 3001111132, 'clave22', 'E', 'Email', NOW()),
(23, 'Natalia', 'Gil', 'nataliag@example.com', 3001111133, 'clave23', 'C', 'SMS', NOW()),
(24, 'Emilio', 'Paredes', 'emiliop@example.com', 3001111134, 'clave24', 'C', 'WhatsApp', NOW()),
(25, 'Martina', 'Cano', 'martinac@example.com', 3001111135, 'clave25', 'E', 'Email', NOW()),
(26, 'Sebastián', 'Molina', 'sebasm@example.com', 3001111136, 'clave26', 'C', 'WhatsApp', NOW()),
(27, 'Luciana', 'Guzmán', 'lucianag@example.com', 3001111137, 'clave27', 'E', 'SMS', NOW()),
(28, 'Samuel', 'Navarro', 'samueln@example.com', 3001111138, 'clave28', 'C', 'Email', NOW()),
(29, 'Laura', 'Pérez', 'laurap2@example.com', 3001111139, 'clave29', 'C', 'WhatsApp', NOW()),
(30, 'Nicolás', 'Herrera', 'nicolash@example.com', 3001111140, 'clave30', 'E', 'Email', NOW()),
(31, 'Alejandra', 'Vega', 'aleja@example.com', 3001111141, 'clave31', 'C', 'SMS', NOW()),
(32, 'Cristian', 'Moreno', 'cristim@example.com', 3001111142, 'clave32', 'E', 'WhatsApp', NOW()),
(33, 'Julieta', 'Campos', 'julietac@example.com', 3001111143, 'clave33', 'C', 'Email', NOW()),
(34, 'David', 'Núñez', 'davidn@example.com', 3001111144, 'clave34', 'C', 'WhatsApp', NOW()),
(35, 'Sara', 'Figueroa', 'saraf@example.com', 3001111145, 'clave35', 'E', 'SMS', NOW()),
(36, 'Manuel', 'Santos', 'manuels@example.com', 3001111146, 'clave36', 'C', 'WhatsApp', NOW()),
(37, 'Victoria', 'León', 'victo@example.com', 3001111147, 'clave37', 'E', 'Email', NOW()),
(38, 'Esteban', 'Prieto', 'estebanp@example.com', 3001111148, 'clave38', 'C', 'SMS', NOW()),
(39, 'Renata', 'Quintero', 'renataq@example.com', 3001111149, 'clave39', 'E', 'WhatsApp', NOW()),
(40, 'Adrián', 'Serrano', 'adrians@example.com', 3001111150, 'clave40', 'C', 'Email', NOW()),
(41, 'Elena', 'Salazar', 'elenas@example.com', 3001111151, 'clave41', 'C', 'WhatsApp', NOW()),
(42, 'Andrés', 'Rincón', 'andresr@example.com', 3001111152, 'clave42', 'E', 'SMS', NOW()),
(43, 'Carolina', 'Peña', 'carop@example.com', 3001111153, 'clave43', 'C', 'Email', NOW()),
(44, 'Jorge', 'Bermúdez', 'jorgeb@example.com', 3001111154, 'clave44', 'C', 'WhatsApp', NOW()),
(45, 'Lina', 'Osorio', 'linao@example.com', 3001111155, 'clave45', 'E', 'Email', NOW()),
(46, 'Andrés', 'Valdés', 'andresv@example.com', 3001111156, 'clave46', 'C', 'SMS', NOW()),
(47, 'Daniela', 'Pardo', 'danielap@example.com', 3001111157, 'clave47', 'C', 'WhatsApp', NOW()),
(48, 'Felipe', 'Zapata', 'felipez@example.com', 3001111158, 'clave48', 'E', 'Email', NOW()),
(49, 'Laura', 'Ceballos', 'laurace@example.com', 3001111159, 'clave49', 'C', 'SMS', NOW()),
(50, 'Simón', 'Orozco', 'simono@example.com', 3001111160, 'clave50', 'E', 'WhatsApp', NOW()),
(51, 'Catalina', 'Rosales', 'catar@example.com', 3001111161, 'clave51', 'C', 'Email', NOW()),
(52, 'Pablo', 'Silva', 'pablos@example.com', 3001111162, 'clave52', 'E', 'SMS', NOW()),
(53, 'Gabriela', 'Patiño', 'gabrip@example.com', 3001111163, 'clave53', 'C', 'WhatsApp', NOW()),
(54, 'Cristina', 'Ospina', 'crisao@example.com', 3001111164, 'clave54', 'C', 'Email', NOW()),
(55, 'Rafael', 'Mejía', 'rafaelm@example.com', 3001111165, 'clave55', 'E', 'WhatsApp', NOW()),
(56, 'María', 'Vallejo', 'mariav@example.com', 3001111166, 'clave56', 'C', 'SMS', NOW()),
(57, 'Héctor', 'Pérez', 'hectorp@example.com', 3001111167, 'clave57', 'C', 'WhatsApp', NOW()),
(58, 'Camila', 'Arango', 'camilaa@example.com', 3001111168, 'clave58', 'E', 'Email', NOW()),
(59, 'Nicolás', 'Montoya', 'nico2@example.com', 3001111169, 'clave59', 'C', 'SMS', NOW()),
(60, 'Lucía', 'Sierra', 'lucias@example.com', 3001111170, 'clave60', 'E', 'WhatsApp', NOW());
-- ====================
-- 2. Cliente (10 registros)
-- ====================
INSERT INTO Cliente VALUES
(1, 1, 10101),
(2, 2, 10102),
(3, 4, 10103),
(4, 6, 10104),
(5, 7, 10105),
(6, 9, 10106),
(7, 10, 10107),
(8, 1, 10108),
(9, 2, 10109),
(10, 4, 10110);
-- ====================
-- 3. Estilista (10 registros)
-- ====================
INSERT INTO Estilista VALUES
(1, 3, 'Cortes, Alisados, Peinados, Tintura, Tratamientos, Depilación, Maquillaje', 'Disponible'),
(2, 5, 'Uñas', 'Disponible'),
(3, 8, 'Alisados, Peinados, Tintura, Tratamientos, Depilación, Maquillaje', 'Disponible'),
(4, 10, 'Uñas', 'Disponible'),
(5, 14, 'Corte Hombre y Mujer', 'Disponible'),
(6, 17, 'Tintura y Tratamientos', 'Disponible'),
(7, 20, 'Maquillaje Profesional', 'Disponible'),
(8, 22, 'Depilación y Alisado', 'Disponible'),
(9, 25, 'Uñas Artísticas', 'Disponible'),
(10, 27, 'Tratamientos Capilares', 'Disponible');
-- ====================
-- 4. Servicios (12 registros)
-- ====================
INSERT INTO Servicios VALUES
(1, 'Corte Hombre', 'Corte de cabello para hombre', '00:30:00', 20000),
(2, 'Corte Mujer', 'Corte de cabello para mujer', '00:45:00', 30000),
(3, 'Tintura', 'Aplicación de tintura básica', '01:30:00', 80000),
(4, 'Peinado', 'Peinado sencillo', '00:40:00', 25000),
(5, 'Manicure', 'Servicio de manicure', '00:50:00', 35000),
(6, 'Pedicure', 'Servicio de pedicure', '00:50:00', 40000),
(7, 'Alisado', 'Alisado permanente', '02:30:00', 120000),
(8, 'Maquillaje', 'Maquillaje de fiesta', '01:00:00', 60000),
(9, 'Depilación', 'Depilación Cejas', '00:30:00', 8000),
(10, 'Tratamiento', 'Tratamiento capilar', '01:20:00', 70000),
(11, 'Depilación', 'Depilación Piernas', '00:30:00', 10000),
(12, 'Depilación', 'Depilación Intima', '00:30:00', 10000);
-- ====================
-- 5. PROMOCIONES (10 registros)
-- ====================
INSERT INTO Promociones VALUES
(1, 1, 'Año Nuevo Renovado', 'Corte y tratamiento capilar al 25% de descuento', 25, 1, '2025-01-05', '2025-01-20'),
(2, 2, 'San Valentín Glow', 'Uñas de corazones con 15% de descuento', 30, 2, '2025-02-10', '2025-02-20'),
(3, 3, 'Jojojo Navidad con color', 'Tintura y retoque con 15% de descuento', 15, 3, '2025-12-01', '2025-12-28'),
(4, 4, 'Diciembre Relajado', 'Uñas con diseños de arbolitos, con un 15% de descuento', 20, 4, '2025-04-05', '2025-04-25'),
(5, 5, 'Mayo para Mamá', 'Descuento especial en manicura y pedicura', 40, 5, '2025-05-01', '2025-05-31'),
(6, 6, 'Junio Premiado', 'El dia 15 de Junio, descuentos de 10%', 35, 6, '2025-06-15', '2025-06-15'),
(7, 7, 'Julio Refrescante', 'Si tienes facturas electronicas de 10 servicios o mas con nosotros en los ultimos 2 meses, 20% de descuento', 20, 7, '2025-07-01', '2025-07-20'),
(8, 8, 'Agosto Estilizado', 'Peinados con secado express gratis', 10, 8, '2025-08-05', '2025-08-31'),
(9, 9, 'Septiembre Rosa', 'Apoyo a la campaña contra el cáncer de mama', 15, 9, '2025-09-01', '2025-09-30'),
(10, 10, 'Octubre Halloween Look', 'Maquillaje artístico y temático', 35, 10, '2025-10-10', '2025-10-31');
-- ====================
-- 6. CITAS (10 registros)
-- ====================
INSERT INTO Citas (idCita, idCliente, idEstilista, idServicios, fechaCita, horaCita, canalReserva, fechaCreacion, estado, observaciones) VALUES
(1, 1, 1, 1, '2025-02-10', '10:00:00', 1, NOW(), 'Confirmada', 'Cliente puntual'),
(2, 2, 2, 2, '2025-02-12', '11:00:00', 1, NOW(), 'Confirmada', 'Primera cita'),
(3, 3, 3, 3, '2025-02-14', '14:00:00', 1, NOW(), 'Confirmada', 'Solicita tinte'),
(4, 4, 4, 4, '2025-02-15', '15:00:00', 1, NOW(), 'Confirmada', 'Peinado boda'),
(5, 5, 5, 5, '2025-02-16', '09:00:00', 1, NOW(), 'Confirmada', 'Manicure urgente'),
(6, 6, 6, 6, '2025-10-27', '13:00:00', 1, NOW(), 'Confirmada', 'Pedicure'),
(7, 7, 7, 7, '2025-02-18', '12:00:00', 1, NOW(), 'Confirmada', 'Alisado'),
(8, 8, 8, 8, '2025-10-27', '16:00:00', 1, NOW(), 'Confirmada', 'Maquillaje noche'),
(9, 9, 9, 9, '2025-02-20', '17:00:00', 1, NOW(), 'Confirmada', 'Depilación'),
(10, 10, 10, 10, '2025-02-21', '18:00:00', 1, NOW(), 'Confirmada', 'Tratamiento capilar');
-- ====================
-- 7. AGENDA ESTILISTA (10 registros)
-- ====================
INSERT INTO AgendaEstilista VALUES
(1, 1, 1, '2025-02-10', '10:00:00', '10:30:00', '2025-02-10'),
(2, 2, 2, '2025-02-12', '11:00:00', '11:45:00', '2025-02-12'),
(3, 3, 3, '2025-02-14', '14:00:00', '15:30:00', '2025-02-14'),
(4, 4, 4, '2025-02-15', '15:00:00', '15:40:00', '2025-02-15'),
(5, 5, 5, '2025-02-16', '09:00:00', '09:50:00', '2025-02-16'),
(6, 6, 6, '2025-02-17', '13:00:00', '13:50:00', '2025-02-17'),
(7, 7, 7, '2025-02-18', '12:00:00', '14:30:00', '2025-02-18'),
(8, 8, 8, '2025-02-19', '16:00:00', '17:00:00', '2025-02-19'),
(9, 9, 9, '2025-02-20', '17:00:00', '17:30:00', '2025-02-20'),
(10, 10, 10, '2025-02-21', '18:00:00', '19:20:00', '2025-02-21');
-- ====================
-- 8. NOTIFICACIONES (10 registros)
-- ====================
INSERT INTO Notificaciones VALUES
(1, 1, 1, 1, 'Recordatorio', 'Cita Corte', 'Recuerde su cita', '2025-02-09 10:00:00', 'Enviado'),
(2, 2, 2, 2, 'Promoción', 'Promo Corte Mujer', 'Aproveche descuento', '2025-02-10 11:00:00', 'Leído'),
(3, 3, 3, 3, 'Recordatorio', 'Tintura', 'No olvide cita', '2025-02-13 12:00:00', 'Enviado'),
(4, 4, 4, 4, 'Promoción', 'Promo Peinado', 'Oferta especial', '2025-02-14 09:00:00', 'Pendiente'),
(5, 5, 5, 5, 'Recordatorio', 'Manicure', 'Confirmar asistencia', '2025-02-15 08:00:00', 'Enviado'),
(6, 6, 6, 6, 'Recordatorio', 'Pedicure', 'Recuerde su cita', '2025-02-16 07:30:00', 'Leído'),
(7, 7, 7, 7, 'Promoción', 'Promo Alisado', 'Descuento del mes', '2025-02-17 13:00:00', 'Enviado'),
(8, 8, 8, 8, 'Recordatorio', 'Maquillaje', 'Confirmar cita', '2025-02-18 14:00:00', 'Pendiente'),
(9, 9, 9, 9, 'Promoción', 'Promo Depilación', 'Descuento limitado', '2025-02-19 15:00:00', 'Enviado'),
(10, 10, 10, 10, 'Recordatorio', 'Tratamiento', 'No falte a su cita', '2025-02-20 16:00:00', 'Leído');
-- ====================
-- 9. CHATBOT (10 registros)
-- ====================
INSERT INTO ChatbotWhatsApp VALUES
(1, 1, 1, '¿Hora de mi cita?', 'Su cita es a las 10:00 am', '2025-02-08 12:00:00'),
(2, 2, 2, '¿Cuánto cuesta?', 'Precio con descuento $25,500', '2025-02-09 13:00:00'),
(3, 3, 3, '¿Puedo cambiar fecha?', 'Sí, comuníquese al 300123456', '2025-02-10 09:00:00'),
(4, 4, 4, '¿Ubicación?', 'Estamos en Calle 123', '2025-02-11 15:00:00'),
(5, 5, 5, '¿Cuánto dura?', 'Duración 50 minutos', '2025-02-12 11:00:00'),
(6, 6, 6, '¿Incluye masaje?', 'Sí, en el paquete premium', '2025-02-13 17:00:00'),
(7, 7, 7, '¿Forma de pago?', 'Efectivo o tarjeta', '2025-02-14 18:00:00'),
(8, 8, 8, '¿Tienen maquillaje nude?', 'Sí, disponible', '2025-02-15 19:00:00'),
(9, 9, 9, '¿Puedo cancelar?', 'Sí, hasta 24h antes', '2025-02-16 20:00:00'),
(10, 10, 10, '¿Qué incluye tratamiento?', 'Lavado + nutrición', '2025-02-17 21:00:00');
-- ====================
-- 10. HISTORIAL CLIENTE (10 registros)
-- ====================
INSERT INTO HistorialCliente VALUES
(1, 1, 1, '2025-01-10 10:00:00'),
(2, 2, 2, '2025-01-12 11:00:00'),
(3, 3, 3, '2025-01-14 14:00:00'),
(4, 4, 4, '2025-01-15 15:00:00'),
(5, 5, 5, '2025-01-16 09:00:00'),
(6, 6, 6, '2025-01-17 13:00:00'),
(7, 7, 7, '2025-01-18 12:00:00'),
(8, 8, 8, '2025-01-19 16:00:00'),
(9, 9, 9, '2025-01-20 17:00:00'),
(10, 10, 10, '2025-01-21 18:00:00'),
(11, 1, 2, '2025-02-05 10:00:00'),
(12, 1, 3, '2025-03-10 09:30:00'),
(13, 2, 1, '2025-01-25 11:15:00'),
(14, 2, 2, '2025-03-05 10:45:00'),
(15, 3, 4, '2025-02-12 14:10:00'),
(16, 3, 5, '2025-03-08 15:00:00'),
(17, 4, 6, '2025-03-15 16:00:00'),
(18, 5, 7, '2025-01-22 09:10:00'),
(19, 5, 8, '2025-02-18 10:20:00'),
(20, 5, 9, '2025-03-12 11:00:00');

-- ====================
-- 11. Productos (5 registros)
-- ====================
INSERT INTO Productos (nombre, descripcion, precio, stock) VALUES
('Shampoo Hidratante', 'Shampoo para cabello seco', 25000.00, 50),
('Crema para Manos', 'Crema hidratante para manos', 15000.00, 30),
('Tinte Rubio', 'Tinte para cabello rubio', 45000.00, 20),
('Máscara Capilar', 'Máscara nutritiva', 35000.00, 40),
('Esmalte Rojo', 'Esmalte de uñas rojo', 12000.00, 60);

-- ====================
-- 12. Ventas (3 registros)
-- ====================
INSERT INTO Ventas (idCliente, fechaVenta, total) VALUES
(1, '2025-01-15 10:00:00', 40000.00),
(2, '2025-01-20 11:00:00', 60000.00),
(3, '2025-02-01 12:00:00', 57000.00);
-- ====================
-- 13. Detalle (6 registros)
-- ====================
INSERT INTO DetalleVenta (idVenta, idProducto, cantidad, precioUnitario, subtotal) VALUES
(1, 1, 1, 25000.00, 25000.00),
(1, 2, 1, 15000.00, 15000.00),
(2, 3, 1, 45000.00, 45000.00),
(2, 4, 1, 15000.00, 15000.00),
(3, 5, 3, 12000.00, 36000.00),
(3, 1, 1, 21000.00, 21000.00); 


-- ========================================
-- NUEVAS TABLAS PARA AJUSTE AL TALLER 4
-- ========================================

-- Catálogo de tipos de notificación
CREATE TABLE TipoNotificacion (
    idTipoNotificacion INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL
);

INSERT INTO TipoNotificacion (nombre) VALUES
('WhatsApp'),
('Email'),
('SMS');

ALTER TABLE Notificaciones
ADD COLUMN idTipoNotificacion INT NULL,
ADD CONSTRAINT FK_Notificaciones_Tipo
    FOREIGN KEY (idTipoNotificacion) REFERENCES TipoNotificacion(idTipoNotificacion);

-- ========================================
-- Reestructurar Chatbot en Conversación y Mensajes
-- ========================================
CREATE TABLE ChatConversacion (
    idConversacion INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    fechaInicio DATETIME NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);

CREATE TABLE ChatMensaje (
    idMensaje INT AUTO_INCREMENT PRIMARY KEY,
    idConversacion INT NOT NULL,
    tipoMensaje ENUM('Cliente', 'Chatbot') NOT NULL,
    contenido TEXT NOT NULL,
    fechaMensaje DATETIME NOT NULL,
    FOREIGN KEY (idConversacion) REFERENCES ChatConversacion(idConversacion)
);

-- ========================================
-- Portafolio del estilista (visitas o trabajos vistos)
-- ========================================
CREATE TABLE PortafolioEstilista (
    idPortafolio INT AUTO_INCREMENT PRIMARY KEY,
    idEstilista INT NOT NULL,
    idCliente INT NOT NULL,
    fechaVisita DATETIME NOT NULL,
    FOREIGN KEY (idEstilista) REFERENCES Estilista(idEstilista),
    FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)
);
-- ========================================
-- NUEVA TABLA: PromocionServicios
-- ========================================
CREATE TABLE PromocionServicios (
    idPromocionServicio INT AUTO_INCREMENT PRIMARY KEY,
    idPromocion INT NOT NULL,
    idServicio INT NOT NULL,
    FOREIGN KEY (idPromocion) REFERENCES Promociones(idPromocion),
    FOREIGN KEY (idServicio) REFERENCES Servicios(idServicios)
);

-- Insertar datos basados en las relaciones existentes
-- (replicamos las asociaciones actuales de Promociones.idServicios)
INSERT INTO PromocionServicios (idPromocion, idServicio)
SELECT idPromocion, idServicios FROM Promociones;
-- 1️⃣ Crear una conversación por cada cliente que aparece en ChatbotWhatsApp
INSERT INTO ChatConversacion (idCliente, fechaInicio)
SELECT DISTINCT c.idCliente, MIN(cb.fechaMensaje)
FROM ChatbotWhatsApp cb
JOIN Citas ci ON cb.idCita = ci.idCita
JOIN Cliente c ON ci.idCliente = c.idCliente
GROUP BY c.idCliente;

-- Insertar los mensajes (cliente + chatbot)
INSERT INTO ChatMensaje (idConversacion, tipoMensaje, contenido, fechaMensaje)
SELECT 
    cc.idConversacion,
    'Cliente',
    cb.mensajeCliente,
    cb.fechaMensaje
FROM ChatbotWhatsApp cb
JOIN Citas ci ON cb.idCita = ci.idCita
JOIN Cliente cl ON ci.idCliente = cl.idCliente
JOIN ChatConversacion cc ON cc.idCliente = cl.idCliente;

INSERT INTO ChatMensaje (idConversacion, tipoMensaje, contenido, fechaMensaje)
SELECT 
    cc.idConversacion,
    'Chatbot',
    cb.respuestaChatbot,
    DATE_ADD(cb.fechaMensaje, INTERVAL 1 MINUTE)
FROM ChatbotWhatsApp cb
JOIN Citas ci ON cb.idCita = ci.idCita
JOIN Cliente cl ON ci.idCliente = cl.idCliente
JOIN ChatConversacion cc ON cc.idCliente = cl.idCliente;

-- ========================================
-- CONSULTA: Citas con servicios en promociones activas de diciembre
-- ========================================
SELECT c.idCliente, c.idCita, p.titulo AS tituloPromocion
FROM Citas c
INNER JOIN PromocionServicios ps ON c.idServicios = ps.idServicio
INNER JOIN Promociones p ON ps.idPromocion = p.idPromocion
WHERE MONTH(p.fechaInicio) = 12 
   OR MONTH(p.fechaFin) = 12
   OR (p.fechaInicio <= '2025-12-31' AND p.fechaFin >= '2025-12-01');

