# Script para aplicar el fix de ENUM a VARCHAR en PostgreSQL local
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Convirtiendo tipos ENUM a VARCHAR" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$PGPASSWORD = "postgres"
$env:PGPASSWORD = $PGPASSWORD

$sqlCommands = @"
ALTER TABLE Usuarios ALTER COLUMN tipoDocumento TYPE VARCHAR(50);
ALTER TABLE Usuarios ALTER COLUMN rol TYPE VARCHAR(20);
ALTER TABLE Usuarios ALTER COLUMN canalPreferido TYPE VARCHAR(20);
ALTER TABLE Citas ALTER COLUMN canalReserva TYPE VARCHAR(20);
ALTER TABLE Citas ALTER COLUMN estado TYPE VARCHAR(20);
ALTER TABLE Notificaciones ALTER COLUMN tipo TYPE VARCHAR(30);
ALTER TABLE Notificaciones ALTER COLUMN estado TYPE VARCHAR(20);
ALTER TABLE ChatMensaje ALTER COLUMN tipoMensaje TYPE VARCHAR(20);

SELECT 'Conversión completada exitosamente' as resultado;
"@

try {
    Write-Host "Conectando a PostgreSQL local (puerto 5432)..." -ForegroundColor Yellow
    $sqlCommands | psql -U postgres -d gretta -h localhost -p 5432
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "========================================" -ForegroundColor Green
        Write-Host "Conversión completada exitosamente" -ForegroundColor Green
        Write-Host "========================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Las columnas ENUM han sido convertidas a VARCHAR." -ForegroundColor Green
        Write-Host "Ahora puedes iniciar el backend de Spring Boot." -ForegroundColor Green
    } else {
        Write-Host ""
        Write-Host "ERROR: Hubo un problema al ejecutar el script SQL." -ForegroundColor Red
        Write-Host "Verifica que la base de datos 'gretta' exista." -ForegroundColor Red
    }
} catch {
    Write-Host ""
    Write-Host "ERROR: No se pudo conectar a PostgreSQL." -ForegroundColor Red
    Write-Host "Detalles: $_" -ForegroundColor Red
    Write-Host ""
    Write-Host "Asegúrate de que:" -ForegroundColor Yellow
    Write-Host "1. PostgreSQL esté instalado" -ForegroundColor Yellow
    Write-Host "2. El comando 'psql' esté disponible en el PATH" -ForegroundColor Yellow
    Write-Host "3. La base de datos 'gretta' exista" -ForegroundColor Yellow
    Write-Host "4. El usuario 'postgres' tenga la contraseña 'postgres'" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Presiona cualquier tecla para continuar..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
