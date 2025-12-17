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
        Write-Host "Building Docker image: $ImageName" -ForegroundColor Green
        docker build -t $ImageName .
        
        Write-Host "$ServiceName build completed successfully!" -ForegroundColor Green
    }
    finally {
        Pop-Location
    }
}

#Build-Microservice "auth-microservice" "./auth-microservice/demo" "authmicro2"
#Build-Microservice "user-microservice" "./user-microservice/demo" "usermicro2"
#Build-Microservice "device-microservice" "./device-microservice/demo" "devicemicro2"
#Build-Microservice "monitoring-microservice" "./monitoring-microservice/demo" "monitoringmicro2"
#Build-Microservice "chat-microservice" "./chat-microservice/demo" "chatmicro2"
Build-Microservice "websocket-microservice" "./websocket-microservice/demo" "websocketmicro2"
#Build-Microservice "load-balancer" "./load-balancer/demo" "loadbalancer"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "All microservices rebuilt successfully!" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

