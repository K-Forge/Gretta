-- Script para convertir los tipos ENUM de PostgreSQL a VARCHAR
-- Esto resuelve el problema de compatibilidad con Hibernate

-- 1. Modificar columna tipoDocumento en tabla Usuarios
ALTER TABLE Usuarios ALTER COLUMN tipoDocumento TYPE VARCHAR(50);

-- 2. Modificar columna rol en tabla Usuarios
ALTER TABLE Usuarios ALTER COLUMN rol TYPE VARCHAR(20);

-- 3. Modificar columna canalPreferido en tabla Usuarios
ALTER TABLE Usuarios ALTER COLUMN canalPreferido TYPE VARCHAR(20);

-- 4. Modificar columna canalReserva en tabla Citas
ALTER TABLE Citas ALTER COLUMN canalReserva TYPE VARCHAR(20);

-- 5. Modificar columna estado en tabla Citas
ALTER TABLE Citas ALTER COLUMN estado TYPE VARCHAR(20);

-- 6. Modificar columna tipo en tabla Notificaciones
ALTER TABLE Notificaciones ALTER COLUMN tipo TYPE VARCHAR(30);

-- 7. Modificar columna estado en tabla Notificaciones
ALTER TABLE Notificaciones ALTER COLUMN estado TYPE VARCHAR(20);

-- 8. Modificar columna tipoMensaje en tabla ChatMensaje
ALTER TABLE ChatMensaje ALTER COLUMN tipoMensaje TYPE VARCHAR(20);

-- Opcional: Eliminar los tipos ENUM si ya no se usan
-- DROP TYPE IF EXISTS tipo_documento CASCADE;
-- DROP TYPE IF EXISTS rol_usuario CASCADE;
-- DROP TYPE IF EXISTS canal_comunicacion CASCADE;
-- DROP TYPE IF EXISTS estado_cita CASCADE;
-- DROP TYPE IF EXISTS estado_notificacion CASCADE;
-- DROP TYPE IF EXISTS tipo_notificacion CASCADE;
-- DROP TYPE IF EXISTS tipo_mensaje CASCADE;

COMMIT;
