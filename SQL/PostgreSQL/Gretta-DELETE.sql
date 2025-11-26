-- ====================
-- SCRIPT DE ELIMINACIÓN DE DATOS - PostgreSQL
-- Base de datos: Gretta
-- Elimina todos los datos de las tablas para permitir una reinserción limpia.
-- ====================

-- Desactivar restricciones de claves foráneas temporalmente (opcional en algunos entornos, pero TRUNCATE CASCADE lo maneja)
-- SET session_replication_role = 'replica';

-- Limpiar tablas usando TRUNCATE con CASCADE para manejar las relaciones
-- El orden no es estrictamente necesario con CASCADE, pero se mantiene por claridad.

TRUNCATE TABLE 
    PromocionServicios,
    HistorialEstilista,
    ChatMensaje,
    ChatConversacion,
    DetalleVenta,
    Ventas,
    Productos,
    HistorialCliente,
    Notificaciones,
    Citas,
    Promociones,
    Servicios,
    Estilista,
    Cliente,
    Usuarios
RESTART IDENTITY CASCADE;

-- Reactivar restricciones (si se desactivaron)
-- SET session_replication_role = 'origin';

-- Mensaje de confirmación (solo visible en clientes que soporten salida de texto)
-- DO $$ BEGIN RAISE NOTICE 'Datos eliminados correctamente.'; END $$;
