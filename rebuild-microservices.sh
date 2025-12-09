#!/bin/bash

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo "=========================================="
echo "Rebuilding all microservices"
echo "=========================================="

build_microservice() {
    local SERVICE_NAME=$1
    local SERVICE_DIR=$2
    local IMAGE_NAME=$3
    
    echo ""
    echo "----------------------------------------"
    echo "Building $SERVICE_NAME"
    echo "----------------------------------------"
    
    cd "$SERVICE_DIR"
    
    echo "Building Docker image: $IMAGE_NAME"
    docker build -t "$IMAGE_NAME" .
    
    echo "$SERVICE_NAME build completed successfully!"
    cd "$SCRIPT_DIR"
}

#build_microservice "auth-microservice" "./auth-microservice/demo" "authmicro2"
#build_microservice "user-microservice" "./user-microservice/demo" "usermicro2"
#build_microservice "device-microservice" "./device-microservice/demo" "devicemicro2"
build_microservice "chat-microservice" "./chat-microservice/demo" "chatmicro2"
build_microservice "websocket-microservice" "./websocket-microservice/demo" "websocketmicro2"
#build_microservice "monitoring-microservice" "./monitoring-microservice/demo" "monitoringmicro2"

echo ""
echo "=========================================="
echo "All microservices rebuilt successfully!"
echo "=========================================="

