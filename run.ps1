# Script para ejecutar notification-service
Write-Host "Iniciando Notification Service en el puerto 8007..." -ForegroundColor Cyan

# Asegurarse de estar en el directorio correcto
if (-not (Test-Path "pom.xml")) {
    Write-Host "Error: No se encuentra pom.xml. Asegúrate de ejecutar este script desde la carpeta del servicio." -ForegroundColor Red
    exit
}

mvn spring-boot:run
