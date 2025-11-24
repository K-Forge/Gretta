@echo off
echo ========================================
echo Convirtiendo tipos ENUM a VARCHAR
echo ========================================
echo.
echo Este script convierte las columnas ENUM de PostgreSQL a VARCHAR
echo para resolver el problema de compatibilidad con Hibernate.
echo.

REM Verificar si Docker está corriendo
docker ps >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Docker no está corriendo. Por favor inicia Docker Desktop.
    pause
    exit /b 1
)

echo Conectando a la base de datos PostgreSQL...
echo.

docker exec -i gretta-postgres-1 psql -U postgres -d gretta << EOF
-- Convertir tipos ENUM a VARCHAR
ALTER TABLE Usuarios ALTER COLUMN tipoDocumento TYPE VARCHAR(50);
ALTER TABLE Usuarios ALTER COLUMN rol TYPE VARCHAR(20);
ALTER TABLE Usuarios ALTER COLUMN canalPreferido TYPE VARCHAR(20);
ALTER TABLE Citas ALTER COLUMN canalReserva TYPE VARCHAR(20);
ALTER TABLE Citas ALTER COLUMN estado TYPE VARCHAR(20);
ALTER TABLE Notificaciones ALTER COLUMN tipo TYPE VARCHAR(30);
ALTER TABLE Notificaciones ALTER COLUMN estado TYPE VARCHAR(20);
ALTER TABLE ChatMensaje ALTER COLUMN tipoMensaje TYPE VARCHAR(20);

-- Verificar los cambios
SELECT column_name, data_type, character_maximum_length
FROM information_schema.columns
WHERE table_name = 'usuarios' 
  AND column_name IN ('tipodocumento', 'rol', 'canalpreferido');
EOF

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo Conversión completada exitosamente
    echo ========================================
    echo.
    echo Las columnas ENUM han sido convertidas a VARCHAR.
    echo Ahora puedes reiniciar el backend de Spring Boot.
) else (
    echo.
    echo ERROR: Hubo un problema al ejecutar el script SQL.
    echo Verifica que el contenedor gretta-postgres-1 esté corriendo.
)

echo.
pause
