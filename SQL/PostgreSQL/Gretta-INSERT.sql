-- ====================
-- SCRIPT DE INSERCIÓN DE DATOS - PostgreSQL
-- Base de datos: Gretta
-- ACTUALIZADO con nueva estructura
-- NOTA: Todas las contraseñas de los usuarios son: 123123
-- ====================

-- ====================
-- 1. Usuarios (60 registros)
-- ====================
INSERT INTO Usuarios (nombre, apellido, correo, telefono, contrasena, tipoDocumento, numeroDocumento, rol, canalPreferido) VALUES
('Juan', 'Pérez', 'juanp@example.com', '3001111111', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111111', 'CLIENTE', 'WHATSAPP'),
('María', 'López', 'marial@example.com', '3001111112', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111112', 'CLIENTE', 'EMAIL'),
('Pedro', 'Gómez', 'pedrog@example.com', '3001111113', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111113', 'ESTILISTA', 'SMS'),
('Laura', 'Martínez', 'lauram@example.com', '3001111114', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111114', 'CLIENTE', 'WHATSAPP'),
('Ana', 'Torres', 'anat@example.com', '3001111115', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111115', 'ESTILISTA', 'EMAIL'),
('Carlos', 'Ruiz', 'carlosr@example.com', '3001111116', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111116', 'CLIENTE', 'SMS'),
('Lucía', 'Hernández', 'luciah@example.com', '3001111117', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111117', 'CLIENTE', 'WHATSAPP'),
('Andrés', 'Morales', 'andresm@example.com', '3001111118', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111118', 'ESTILISTA', 'EMAIL'),
('Paula', 'Castro', 'paulac@example.com', '3001111119', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111119', 'CLIENTE', 'WHATSAPP'),
('Mateo', 'Ramírez', 'mateor@example.com', '3001111120', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111120', 'ESTILISTA', 'SMS'),
('Sofía', 'Vargas', 'sofiav@example.com', '3001111121', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111121', 'CLIENTE', 'WHATSAPP'),
('Julián', 'Mendoza', 'julianm@example.com', '3001111122', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111122', 'ESTILISTA', 'EMAIL'),
('Camila', 'Ortega', 'camilao@example.com', '3001111123', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111123', 'CLIENTE', 'SMS'),
('Diego', 'García', 'diegog@example.com', '3001111124', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111124', 'ESTILISTA', 'WHATSAPP'),
('Valentina', 'Suárez', 'valen@example.com', '3001111125', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111125', 'CLIENTE', 'EMAIL'),
('Daniel', 'Mejía', 'danielm@example.com', '3001111126', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111126', 'CLIENTE', 'SMS'),
('Isabella', 'Cortés', 'isabel@example.com', '3001111127', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111127', 'ESTILISTA', 'WHATSAPP'),
('Tomás', 'Reyes', 'tomasr@example.com', '3001111128', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111128', 'CLIENTE', 'EMAIL'),
('Gabriela', 'Jiménez', 'gabrie@example.com', '3001111129', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111129', 'CLIENTE', 'WHATSAPP'),
('Santiago', 'Luna', 'santi@example.com', '3001111130', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111130', 'ESTILISTA', 'SMS'),
('Mariana', 'Rojas', 'marianar@example.com', '3001111131', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111131', 'CLIENTE', 'WHATSAPP'),
('Felipe', 'Díaz', 'feliped@example.com', '3001111132', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111132', 'ESTILISTA', 'EMAIL'),
('Natalia', 'Gil', 'nataliag@example.com', '3001111133', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111133', 'CLIENTE', 'SMS'),
('Emilio', 'Paredes', 'emiliop@example.com', '3001111134', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111134', 'CLIENTE', 'WHATSAPP'),
('Martina', 'Cano', 'martinac@example.com', '3001111135', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111135', 'ESTILISTA', 'EMAIL'),
('Sebastián', 'Molina', 'sebasm@example.com', '3001111136', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111136', 'CLIENTE', 'WHATSAPP'),
('Luciana', 'Guzmán', 'lucianag@example.com', '3001111137', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111137', 'ESTILISTA', 'SMS'),
('Samuel', 'Navarro', 'samueln@example.com', '3001111138', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111138', 'CLIENTE', 'EMAIL'),
('Laura', 'Pérez', 'laurap2@example.com', '3001111139', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111139', 'CLIENTE', 'WHATSAPP'),
('Nicolás', 'Herrera', 'nicolash@example.com', '3001111140', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111140', 'ESTILISTA', 'EMAIL'),
('Alejandra', 'Vega', 'aleja@example.com', '3001111141', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111141', 'CLIENTE', 'SMS'),
('Cristian', 'Moreno', 'cristim@example.com', '3001111142', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111142', 'ESTILISTA', 'WHATSAPP'),
('Julieta', 'Campos', 'julietac@example.com', '3001111143', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111143', 'CLIENTE', 'EMAIL'),
('David', 'Núñez', 'davidn@example.com', '3001111144', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111144', 'CLIENTE', 'WHATSAPP'),
('Sara', 'Figueroa', 'saraf@example.com', '3001111145', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111145', 'ESTILISTA', 'SMS'),
('Manuel', 'Santos', 'manuels@example.com', '3001111146', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111146', 'CLIENTE', 'WHATSAPP'),
('Victoria', 'León', 'victo@example.com', '3001111147', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111147', 'ESTILISTA', 'EMAIL'),
('Esteban', 'Prieto', 'estebanp@example.com', '3001111148', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111148', 'CLIENTE', 'SMS'),
('Renata', 'Quintero', 'renataq@example.com', '3001111149', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111149', 'ESTILISTA', 'WHATSAPP'),
('Adrián', 'Serrano', 'adrians@example.com', '3001111150', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111150', 'CLIENTE', 'EMAIL'),
('Elena', 'Salazar', 'elenas@example.com', '3001111151', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111151', 'CLIENTE', 'WHATSAPP'),
('Andrés', 'Rincón', 'andresr@example.com', '3001111152', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111152', 'ESTILISTA', 'SMS'),
('Carolina', 'Peña', 'carop@example.com', '3001111153', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111153', 'CLIENTE', 'EMAIL'),
('Jorge', 'Bermúdez', 'jorgeb@example.com', '3001111154', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111154', 'CLIENTE', 'WHATSAPP'),
('Lina', 'Osorio', 'linao@example.com', '3001111155', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111155', 'ESTILISTA', 'EMAIL'),
('Andrés', 'Valdés', 'andresv@example.com', '3001111156', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111156', 'CLIENTE', 'SMS'),
('Daniela', 'Pardo', 'danielap@example.com', '3001111157', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111157', 'CLIENTE', 'WHATSAPP'),
('Felipe', 'Zapata', 'felipez@example.com', '3001111158', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111158', 'ESTILISTA', 'EMAIL'),
('Laura', 'Ceballos', 'laurace@example.com', '3001111159', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111159', 'CLIENTE', 'SMS'),
('Simón', 'Orozco', 'simono@example.com', '3001111160', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111160', 'ESTILISTA', 'WHATSAPP'),
('Catalina', 'Rosales', 'catar@example.com', '3001111161', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111161', 'CLIENTE', 'EMAIL'),
('Pablo', 'Silva', 'pablos@example.com', '3001111162', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111162', 'ESTILISTA', 'SMS'),
('Gabriela', 'Patiño', 'gabrip@example.com', '3001111163', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111163', 'CLIENTE', 'WHATSAPP'),
('Cristina', 'Ospina', 'crisao@example.com', '3001111164', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111164', 'CLIENTE', 'EMAIL'),
('Rafael', 'Mejía', 'rafaelm@example.com', '3001111165', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111165', 'ESTILISTA', 'WHATSAPP'),
('María', 'Vallejo', 'mariav@example.com', '3001111166', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111166', 'CLIENTE', 'SMS'),
('Héctor', 'Pérez', 'hectorp@example.com', '3001111167', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111167', 'CLIENTE', 'WHATSAPP'),
('Camila', 'Arango', 'camilaa@example.com', '3001111168', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111168', 'ESTILISTA', 'EMAIL'),
('Nicolás', 'Montoya', 'nico2@example.com', '3001111169', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111169', 'CLIENTE', 'SMS'),
('Lucía', 'Sierra', 'lucias@example.com', '3001111170', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOcd7.odEqJ62', 'CEDULA', '1001111170', 'ESTILISTA', 'WHATSAPP');

-- ====================
-- 2. Cliente (10 registros - sin duplicados, SERIAL auto-genera IDs)
-- ====================
INSERT INTO Cliente (idUsuario) VALUES
(1), (2), (4), (6), (7), (9), (10), (11), (12), (13);

-- ====================
-- 3. Estilista (10 registros - SERIAL auto-genera IDs)
-- ====================
INSERT INTO Estilista (idUsuario, especialidad, disponibilidad) VALUES
(3, 'Cortes, Alisados, Peinados, Tintura, Tratamientos, Depilación, Maquillaje', 'Disponible'),
(5, 'Uñas', 'Disponible'),
(8, 'Alisados, Peinados, Tintura, Tratamientos, Depilación, Maquillaje', 'Disponible'),
(10, 'Uñas', 'Disponible'),
(14, 'Corte Hombre y Mujer', 'Disponible'),
(17, 'Tintura y Tratamientos', 'Disponible'),
(20, 'Maquillaje Profesional', 'Disponible'),
(22, 'Depilación y Alisado', 'Disponible'),
(25, 'Uñas Artísticas', 'Disponible'),
(27, 'Tratamientos Capilares', 'Disponible');

-- ====================
-- 4. Servicios (12 registros - SERIAL auto-genera IDs)
-- ====================
INSERT INTO Servicios (nombre, descripcion, duracion, precio) VALUES
('Corte Hombre', 'Corte de cabello para hombre', '00:30:00', 20000.00),
('Corte Mujer', 'Corte de cabello para mujer', '00:45:00', 30000.00),
('Tintura', 'Aplicación de tintura básica', '01:30:00', 80000.00),
('Peinado', 'Peinado sencillo', '00:40:00', 25000.00),
('Manicure', 'Servicio de manicure', '00:50:00', 35000.00),
('Pedicure', 'Servicio de pedicure', '00:50:00', 40000.00),
('Alisado', 'Alisado permanente', '02:30:00', 120000.00),
('Maquillaje', 'Maquillaje de fiesta', '01:00:00', 60000.00),
('Depilación Cejas', 'Depilación de cejas', '00:30:00', 8000.00),
('Tratamiento Capilar', 'Tratamiento capilar profundo', '01:20:00', 70000.00),
('Depilación Piernas', 'Depilación de piernas completas', '00:30:00', 10000.00),
('Depilación Íntima', 'Depilación de zona íntima', '00:30:00', 10000.00);

-- ====================
-- 5. PROMOCIONES (10 registros - Sin idServicio, SERIAL auto-genera IDs)
-- ====================
INSERT INTO Promociones (titulo, descripcion, descuento, fechaInicio, fechaFin) VALUES
('Año Nuevo Renovado', 'Corte y tratamiento capilar al 25% de descuento', 25.00, '2025-01-05', '2025-01-20'),
('San Valentín Glow', 'Uñas de corazones con 30% de descuento', 30.00, '2025-02-10', '2025-02-20'),
('Jojojo Navidad con color', 'Tintura y retoque con 15% de descuento', 15.00, '2025-12-01', '2025-12-28'),
('Diciembre Relajado', 'Uñas con diseños de arbolitos, con un 20% de descuento', 20.00, '2025-04-05', '2025-04-25'),
('Mayo para Mamá', 'Descuento especial en manicura y pedicura', 40.00, '2025-05-01', '2025-05-31'),
('Junio Premiado', 'El día 15 de Junio, descuentos de 35%', 35.00, '2025-06-15', '2025-06-16'),
('Julio Refrescante', 'Si tienes facturas electrónicas de 10 servicios o más en los últimos 2 meses, 20% de descuento', 20.00, '2025-07-01', '2025-07-20'),
('Agosto Estilizado', 'Peinados con secado express gratis', 10.00, '2025-08-05', '2025-08-31'),
('Septiembre Rosa', 'Apoyo a la campaña contra el cáncer de mama', 15.00, '2025-09-01', '2025-09-30'),
('Octubre Halloween Look', 'Maquillaje artístico y temático', 35.00, '2025-10-10', '2025-10-31');

-- ====================
-- 6. PromocionServicios (Relación many-to-many)
-- ====================
INSERT INTO PromocionServicios (idPromocion, idServicio) VALUES
(1, 1), (1, 10),  -- Año Nuevo: Corte + Tratamiento
(2, 5),           -- San Valentín: Manicure
(3, 3),           -- Navidad: Tintura
(4, 5),           -- Diciembre: Manicure
(5, 5), (5, 6),   -- Mayo Mamá: Manicure + Pedicure
(6, 1), (6, 2), (6, 3), (6, 4), (6, 5), (6, 6), (6, 7), (6, 8), (6, 9), (6, 10), (6, 11), (6, 12),  -- Junio: Todos
(7, 7),           -- Julio: Alisado
(8, 4),           -- Agosto: Peinado
(9, 9), (9, 11), (9, 12),  -- Septiembre: Depilaciones
(10, 8);          -- Halloween: Maquillaje

-- ====================
-- 7. CITAS (10 registros - idServicio en singular, canal ENUM, SERIAL)
-- ====================
INSERT INTO Citas (idCliente, idEstilista, idServicio, fechaCita, horaCita, canalReserva, estado, observaciones) VALUES
(1, 1, 1, '2025-02-10', '10:00:00', 'WHATSAPP', 'CONFIRMADA', 'Cliente puntual'),
(2, 2, 2, '2025-02-12', '11:00:00', 'WHATSAPP', 'CONFIRMADA', 'Primera cita'),
(3, 3, 3, '2025-02-14', '14:00:00', 'WHATSAPP', 'CONFIRMADA', 'Solicita tinte oscuro'),
(4, 4, 4, '2025-02-15', '15:00:00', 'WHATSAPP', 'CONFIRMADA', 'Peinado para boda'),
(5, 5, 5, '2025-02-16', '09:00:00', 'WHATSAPP', 'CONFIRMADA', 'Manicure urgente'),
(6, 6, 6, '2025-10-27', '13:00:00', 'WHATSAPP', 'CONFIRMADA', 'Pedicure spa'),
(7, 7, 7, '2025-02-18', '12:00:00', 'WHATSAPP', 'CONFIRMADA', 'Alisado japonés'),
(8, 8, 8, '2025-10-27', '16:00:00', 'WHATSAPP', 'CONFIRMADA', 'Maquillaje para noche'),
(9, 9, 9, '2025-02-20', '17:00:00', 'WHATSAPP', 'CONFIRMADA', 'Depilación de cejas'),
(10, 10, 10, '2025-02-21', '18:00:00', 'WHATSAPP', 'CONFIRMADA', 'Tratamiento capilar profundo');

-- ====================
-- 8. NOTIFICACIONES (10 registros - tipo ENUM, idPromocion singular, SERIAL)
-- ====================
INSERT INTO Notificaciones (idUsuario, idCita, idPromocion, tipo, asunto, mensaje, estado) VALUES
(1, 1, NULL, 'RECORDATORIO', 'Recordatorio de Cita - Corte', 'Recuerde su cita programada para mañana a las 10:00 AM', 'ENVIADA'),
(2, 2, 2, 'PROMOCION', 'Promoción San Valentín', 'Aproveche nuestro 30% de descuento en manicure', 'ENTREGADA'),
(3, 3, NULL, 'RECORDATORIO', 'Recordatorio de Tintura', 'No olvide su cita de tintura mañana', 'ENVIADA'),
(4, 4, 4, 'PROMOCION', 'Promoción Especial Peinados', 'Oferta especial en peinados este mes', 'PENDIENTE'),
(5, 5, NULL, 'RECORDATORIO', 'Recordatorio Manicure', 'Por favor confirme su asistencia', 'ENVIADA'),
(6, 6, NULL, 'RECORDATORIO', 'Recordatorio Pedicure', 'Recuerde su cita de pedicure', 'ENTREGADA'),
(7, 7, 7, 'PROMOCION', 'Promoción Alisado', 'Descuento del mes en alisados', 'ENVIADA'),
(8, 8, NULL, 'RECORDATORIO', 'Recordatorio Maquillaje', 'Confirme su cita de maquillaje', 'PENDIENTE'),
(9, 9, 9, 'PROMOCION', 'Campaña Septiembre Rosa', 'Descuento solidario en depilaciones', 'ENVIADA'),
(10, 10, NULL, 'RECORDATORIO', 'Recordatorio Tratamiento', 'No falte a su cita de tratamiento capilar', 'ENTREGADA');

-- ====================
-- 9. HISTORIAL CLIENTE (20 registros - idCliente en lugar de idUsuario, idServicio singular, SERIAL)
-- ====================
INSERT INTO HistorialCliente (idCliente, idServicio, fechaServicio) VALUES
(1, 1, '2025-01-10 10:00:00'),
(2, 2, '2025-01-12 11:00:00'),
(3, 3, '2025-01-14 14:00:00'),
(4, 4, '2025-01-15 15:00:00'),
(5, 5, '2025-01-16 09:00:00'),
(6, 6, '2025-01-17 13:00:00'),
(7, 7, '2025-01-18 12:00:00'),
(8, 8, '2025-01-19 16:00:00'),
(1, 2, '2025-02-05 10:00:00'),
(1, 3, '2025-03-10 09:30:00'),
(2, 1, '2025-01-25 11:15:00'),
(2, 2, '2025-03-05 10:45:00'),
(3, 4, '2025-02-12 14:10:00'),
(3, 5, '2025-03-08 15:00:00'),
(4, 6, '2025-03-15 16:00:00'),
(5, 7, '2025-01-22 09:10:00'),
(5, 8, '2025-02-18 10:20:00'),
(6, 9, '2025-02-20 11:00:00'),
(7, 10, '2025-03-01 14:00:00'),
(8, 1, '2025-03-12 15:30:00');

-- ====================
-- 10. PRODUCTOS (10 registros ejemplo)
-- ====================
INSERT INTO Productos (nombre, descripcion, precio, stock) VALUES
('Shampoo Profesional', 'Shampoo para cabello tratado', 45000.00, 50),
('Acondicionador Nutritivo', 'Acondicionador hidratante profundo', 48000.00, 45),
('Mascarilla Capilar', 'Tratamiento intensivo para cabello', 65000.00, 30),
('Sérum Anti-Frizz', 'Control de frizz y brillo', 55000.00, 25),
('Aceite de Argán', 'Aceite natural para cabello', 70000.00, 20),
('Crema para Peinar', 'Styling cream profesional', 38000.00, 40),
('Spray Fijador', 'Fijación fuerte sin residuos', 42000.00, 35),
('Tinte Profesional', 'Coloración permanente', 85000.00, 60),
('Esmalte de Uñas', 'Esmalte larga duración', 18000.00, 100),
('Kit Manicure', 'Set completo para manicure en casa', 95000.00, 15);

-- ====================
-- 11. VENTAS (5 registros ejemplo)
-- ====================
INSERT INTO Ventas (idCliente, fechaVenta, total) VALUES
(1, '2025-02-10 10:30:00', 93000.00),
(2, '2025-02-12 11:45:00', 113000.00),
(3, '2025-02-14 14:30:00', 48000.00),
(4, '2025-02-15 15:40:00', 55000.00),
(5, '2025-02-16 09:50:00', 140000.00);

-- ====================
-- 12. DETALLE VENTA (15 registros ejemplo)
-- ====================
INSERT INTO DetalleVenta (idVenta, idProducto, cantidad, precioUnitario, subtotal) VALUES
(1, 1, 1, 45000.00, 45000.00),
(1, 2, 1, 48000.00, 48000.00),
(2, 3, 1, 65000.00, 65000.00),
(2, 4, 1, 48000.00, 48000.00),
(3, 2, 1, 48000.00, 48000.00),
(4, 4, 1, 55000.00, 55000.00),
(5, 5, 2, 70000.00, 140000.00);

-- ====================
-- 13. CHAT CONVERSACION (Ejemplos sin datos de ChatbotWhatsApp)
-- ====================
INSERT INTO ChatConversacion (idCliente, fechaInicio) VALUES
(1, '2025-02-08 12:00:00'),
(2, '2025-02-09 13:00:00'),
(3, '2025-02-10 09:00:00'),
(4, '2025-02-11 15:00:00'),
(5, '2025-02-12 11:00:00');

-- ====================
-- 14. CHAT MENSAJE (Ejemplos de conversaciones)
-- ====================
INSERT INTO ChatMensaje (idConversacion, tipoMensaje, contenido, fechaMensaje) VALUES
(1, 'CLIENTE', '¿A qué hora es mi cita de mañana?', '2025-02-08 12:00:00'),
(1, 'CHATBOT', 'Su cita está programada para mañana a las 10:00 AM', '2025-02-08 12:00:30'),
(2, 'CLIENTE', '¿Cuánto cuesta el corte de mujer?', '2025-02-09 13:00:00'),
(2, 'CHATBOT', 'El corte de cabello para mujer tiene un valor de $30,000', '2025-02-09 13:00:45'),
(3, 'CLIENTE', '¿Puedo cambiar la fecha de mi cita?', '2025-02-10 09:00:00'),
(3, 'CHATBOT', 'Claro, por favor comuníquese al 300-123-4567 para reagendar', '2025-02-10 09:01:00'),
(4, 'CLIENTE', '¿Dónde están ubicados?', '2025-02-11 15:00:00'),
(4, 'CHATBOT', 'Estamos ubicados en Calle 123 #45-67, Centro Comercial Plaza', '2025-02-11 15:00:20'),
(5, 'CLIENTE', '¿Cuánto dura el servicio de manicure?', '2025-02-12 11:00:00'),
(5, 'CHATBOT', 'El servicio de manicure tiene una duración aproximada de 50 minutos', '2025-02-12 11:00:35');

-- ====================
-- 15. HISTORIAL ESTILISTA (10 registros ejemplo)
-- ====================
INSERT INTO HistorialEstilista (idEstilista, idCliente, fechaVisita) VALUES
(1, 1, '2025-02-10 10:00:00'),
(2, 2, '2025-02-12 11:00:00'),
(3, 3, '2025-02-14 14:00:00'),
(4, 4, '2025-02-15 15:00:00'),
(5, 5, '2025-02-16 09:00:00'),
(6, 6, '2025-10-27 13:00:00'),
(7, 7, '2025-02-18 12:00:00'),
(8, 8, '2025-10-27 16:00:00'),
(1, 1, '2025-01-10 10:00:00'),
(2, 2, '2025-01-12 11:00:00');

