# Script para ejecutar notification-service
Write-Host "Iniciando Notification Service en el puerto 8007..." -ForegroundColor Cyan

# Asegurarse de estar en el directorio correcto
if (-not (Test-Path "pom.xml")) {
    Write-Host "Error: No se encuentra pom.xml. Asegúrate de ejecutar este script desde la carpeta del servicio." -ForegroundColor Red
    exit
}

# Cargar variables de entorno desde .env si existe
if (Test-Path ".env") {
    Write-Host "Cargando variables desde .env..." -ForegroundColor Yellow
    Get-Content .env | Where-Object { $_ -match "=" -and $_ -notmatch "^#" } | ForEach-Object {
        $name, $value = $_.Split('=', 2)
        [System.Environment]::SetEnvironmentVariable($name, $value, [System.EnvironmentVariableTarget]::Process)
    }
}

mvn spring-boot:run
