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
    
    echo "Running mvn clean package..."
    mvn clean package -DskipTests
    
    echo "Running mvn install..."
    mvn install -DskipTests
    
    echo "Building Docker image: $IMAGE_NAME"
    docker build -t "$IMAGE_NAME" .
    
    echo "$SERVICE_NAME build completed successfully!"
    cd "$SCRIPT_DIR"
}

build_microservice "auth-microservice" "./auth-microservice/demo" "auth-microservice2"
build_microservice "user-microservice" "./user-microservice/demo" "user-microservice2"
build_microservice "device-microservice" "./device-microservice/demo" "device-microservice2"

echo ""
echo "=========================================="
echo "All microservices rebuilt successfully!"
echo "=========================================="

