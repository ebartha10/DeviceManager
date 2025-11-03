# NEXUS - Device Management Platform

A modern, microservices-based device management platform built with Spring Boot and React. NEXUS provides secure user authentication, device management, and administrative controls through a sleek, cyberpunk-themed interface.

## 🏗️ Architecture

NEXUS follows a microservices architecture with the following components:

- **Auth Microservice**: Handles user authentication, registration, and JWT token generation
- **User Microservice**: Manages user data and profiles
- **Device Microservice**: Manages devices and user-device associations
- **Frontend**: React-based single-page application with admin console and user dashboard
- **Traefik**: Reverse proxy and load balancer for routing and CORS handling
- **PostgreSQL**: Separate database for each microservice

## 🛠️ Technology Stack

### Backend
- **Java 21**
- **Spring Boot 4.0** (Spring Web, Spring Data JPA, Spring Security)
- **PostgreSQL** (Database)
- **JWT** (JSON Web Tokens for authentication)
- **Maven** (Build tool)
- **Lombok** (Boilerplate reduction)

### Frontend
- **React 19** with **React Router 7**
- **TypeScript**
- **Material-UI (MUI)** v7
- **Emotion** (CSS-in-JS)
- **Tailwind CSS**
- **Vite** (Build tool)

### Infrastructure
- **Docker** & **Docker Compose**
- **Traefik v3.0** (Reverse proxy, load balancer)
- **PostgreSQL** (Containerized databases)

## 📋 Prerequisites

Before building and running the project, ensure you have the following installed:

- **Docker** (version 20.10+)
- **Docker Compose** (version 2.0+)
- **Maven** (version 3.8+)
- **Node.js** (version 20+)
- **npm** (version 9+)
- **Java 21 JDK**

## 🔨 Build Instructions

**Important**: Docker images must be built separately before running the application with Docker Compose.

### Building Backend Microservices

Each microservice needs to be built into a Docker image. Navigate to each microservice directory and build:

```bash
# Build Auth Microservice
cd auth-microservice/demo
mvn clean package
docker build -t auth-microservice .

# Build User Microservice
cd ../../user-microservice/demo
mvn clean package
docker build -t user-microservice .

# Build Device Microservice
cd ../../device-microservice/demo
mvn clean package
docker build -t device-microservice .
```

### Building Frontend

The frontend is built as part of the Docker image, but you can also build it locally:

```bash
cd frontend
npm install
npm run build
```

To build the frontend Docker image:

```bash
cd frontend
docker build -t frontend .
```

### Quick Build Script (Optional)

You can create a build script to automate the process:

```bash
#!/bin/bash
# build-all.sh

echo "Building Auth Microservice..."
cd auth-microservice/demo && mvn clean package && docker build -t auth-microservice . && cd ../..

echo "Building User Microservice..."
cd user-microservice/demo && mvn clean package && docker build -t user-microservice . && cd ../..

echo "Building Device Microservice..."
cd device-microservice/demo && mvn clean package && docker build -t device-microservice . && cd ../..

echo "Building Frontend..."
cd frontend && docker build -t frontend . && cd ..

echo "All images built successfully!"
```

## 🚀 Execution Instructions

### Using Docker Compose (Recommended)

After building all Docker images, start all services:

```bash
# Start all services
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Stop and remove volumes (fresh start)
docker-compose down -v
```

### Accessing the Application

Once all services are running:

- **Frontend**: http://localhost
- **Admin Console**: http://localhost/admin (requires ADMIN role)
- **User Dashboard**: http://localhost/dashboard (requires USER role)
- **Traefik Dashboard**: http://localhost:8080

### Service Ports

- **Frontend**: Port 3000 (internal), exposed via Traefik on port 80
- **Auth Service**: Port 8080 (internal), exposed via Traefik at `/api/auth`
- **User Service**: Port 8080 (internal), exposed via Traefik at `/users`
- **Device Service**: Port 8080 (internal), exposed via Traefik at `/device`
- **Databases**: 
  - Auth DB: Port 5435
  - User DB: Port 5434
  - Device DB: Port 5433

## ⚙️ Configuration

### Environment Variables

#### Auth Microservice
- `DB_IP`: Database host (default: `db-auth`)
- `DB_PORT`: Database port (default: `5432`)
- `DB_DBNAME`: Database name (default: `auth-microservice`)
- `DB_USER`: Database user (default: `postgres`)
- `DB_PASSWORD`: Database password (default: `admin`)
- `PORT`: Service port (default: `8080`)
- `USER_MICROSERVICE_BASE_URL`: User microservice URL

#### User Microservice
- `DB_IP`: Database host (default: `db-user`)
- `DB_PORT`: Database port (default: `5432`)
- `DB_DBNAME`: Database name (default: `user-microservice`)
- `DB_USER`: Database user (default: `postgres`)
- `DB_PASSWORD`: Database password (default: `admin`)
- `DEVICE_MS_BASE_URL`: Device microservice URL

#### Device Microservice
- `DB_IP`: Database host (default: `db-device`)
- `DB_PORT`: Database port (default: `5432`)
- `DB_DBNAME`: Database name (default: `device-microservice`)
- `DB_USER`: Database user (default: `postgres`)
- `DB_PASSWORD`: Database password (default: `admin`)

#### Frontend
- `VITE_API_BASE_URL`: API base URL (default: `http://localhost`)

### Traefik Configuration

Traefik routing is configured in:
- `traefik.yml`: Static configuration
- `dynamic/path.yml`: Dynamic routing rules and middleware

Key routes:
- `/api/auth/*` → Auth Service
- `/users/*` → User Service
- `/device/*` → Device Service
- `/` → Frontend (default route)

## ✨ Features

### User Features
- **User Registration & Authentication**: Secure JWT-based authentication
- **Role-Based Access Control**: ADMIN and USER roles
- **User Dashboard**: View and manage assigned devices
- **Profile Management**: Update user information

### Admin Features
- **User Management**: Create, edit, delete users
- **Device Management**: Create, edit, delete devices
- **User-Device Associations**: Assign and remove devices from users
- **System Overview**: Dashboard with statistics and summaries
- **Real-time Updates**: Automatic refresh of data after changes

### Security Features
- **JWT Authentication**: Secure token-based authentication
- **CORS Support**: Configured for cross-origin requests
- **Role-Based Authorization**: Different access levels for users and admins
- **Secure Password Storage**: BCrypt password hashing

## 🔌 API Endpoints

### Authentication (`/api/auth`)
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login
- `GET /api/auth/validate` - Validate JWT token

### Users (`/users`)
- `GET /users/get-all` - Get all users (requires authentication)
- `GET /users?id={id}` - Get user by ID (requires authentication)
- `POST /users` - Create user (ADMIN only)
- `PUT /users` - Update user (requires authentication)
- `DELETE /users?id={id}` - Delete user (ADMIN only)

### Devices (`/device`)
- `GET /device/get-all` - Get all devices (requires authentication)
- `GET /device?id={id}` - Get device by ID (requires authentication)
- `POST /device` - Create device (ADMIN only)
- `PUT /device` - Update device (ADMIN only)
- `DELETE /device?id={id}` - Delete device (ADMIN only)
- `GET /device/user-device?userId={id}` - Get devices for user (requires authentication)
- `POST /device/user-device` - Assign device to user (requires authentication)
- `DELETE /device/user-device?userId={id}&deviceId={id}` - Remove device from user (requires authentication)

## 🧪 Development

### Running Locally (Without Docker)

#### Backend Services
```bash
# Auth Microservice
cd auth-microservice/demo
mvn spring-boot:run

# User Microservice (in another terminal)
cd user-microservice/demo
mvn spring-boot:run

# Device Microservice (in another terminal)
cd device-microservice/demo
mvn spring-boot:run
```

#### Frontend
```bash
cd frontend
npm install
npm run dev
```

**Note**: When running locally, you'll need to configure the frontend to point to the correct API endpoints and handle CORS appropriately.

## 🐛 Troubleshooting

### Port Conflicts
If ports are already in use, modify the port mappings in `docker-compose.yml`.

### Database Connection Issues
Ensure PostgreSQL containers are running:
```bash
docker-compose ps
```

### CORS Errors
CORS is handled by Traefik. If you encounter CORS issues:
1. Check `dynamic/path.yml` for CORS middleware configuration
2. Verify Traefik is routing requests correctly
3. Check browser console for specific error messages

### Image Not Found Errors
If Docker Compose complains about missing images:
```bash
# Verify images exist
docker images

# Rebuild missing images
# See "Build Instructions" section above
```

## 📝 Notes

- The application uses JWT tokens stored in browser localStorage
- Default admin credentials should be created through the registration endpoint
- Database schemas are auto-generated on first startup (Hibernate `create` mode)
- Traefik configuration supports hot-reloading on Windows (dynamic config)

## 📄 License

This project is part of a university assignment.

## 👥 Contributors

Developed for Software Design course assignment.

---