$ErrorActionPreference = "Stop"

$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $ScriptDir

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "Rebuilding all microservices" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

function Build-Microservice {
    param(
        [string]$ServiceName,
        [string]$ServiceDir,
        [string]$ImageName
    )
    
    Write-Host ""
    Write-Host "----------------------------------------" -ForegroundColor Yellow
    Write-Host "Building $ServiceName" -ForegroundColor Yellow
    Write-Host "----------------------------------------" -ForegroundColor Yellow
    
    Push-Location $ServiceDir
    
    try {
        Write-Host "Running mvn clean package..." -ForegroundColor Green
        mvn clean package -DskipTests
        
        Write-Host "Running mvn install..." -ForegroundColor Green
        mvn install -DskipTests
        
        Write-Host "Building Docker image: $ImageName" -ForegroundColor Green
        docker build -t $ImageName .
        
        Write-Host "$ServiceName build completed successfully!" -ForegroundColor Green
    }
    finally {
        Pop-Location
    }
}

Build-Microservice "auth-microservice" "./auth-microservice/demo" "auth-microservice2"
Build-Microservice "user-microservice" "./user-microservice/demo" "user-microservice2"
Build-Microservice "device-microservice" "./device-microservice/demo" "device-microservice2"
Build-Microservice "monitoring-microservice" "./monitoring-microservice/demo" "monitoringmicro2"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "All microservices rebuilt successfully!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

