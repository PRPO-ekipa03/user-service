# User Service Microservice

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Architecture](#architecture)
- [Setup and Installation](#setup-and-installation)
  - [Prerequisites](#prerequisites)
  - [Clone the Repository](#clone-the-repository)
  - [Build the Project](#build-the-project)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
  - [Locally](#locally)
  - [Using Docker](#using-docker)
- [API Documentation](#api-documentation)
- [Endpoints Overview](#endpoints-overview)
  - [Authentication Endpoints](#authentication-endpoints)
  - [User Management Endpoints](#user-management-endpoints)
- [Error Handling](#error-handling)


## Overview
The **User Service** is a Spring Boot-based microservice designed for user management and authentication. It provides functionalities such as user registration, login, account confirmation, password reset, and token validation. The service uses PostgreSQL for persistence, integrates with a notification service for email communication, and employs JWT for secure authentication.

## Features
- User registration, login, and profile management.
- Account confirmation via email.
- Password reset functionality.
- JWT-based authentication and token validation.
- Integration with a notification service for sending emails.
- Secure password storage using BCrypt.

## Technologies Used
- **Java 21** with **Spring Boot**
- **Spring Security** for authentication and authorization
- **Spring Data JPA** for ORM
- **PostgreSQL** as the database
- **JWT** for token-based authentication
- **OpenAPI/Swagger** for API documentation
- **Maven** for build management
- **Docker** for containerization

## Architecture
The service is structured into multiple layers:
- **Controller Layer**: Handles HTTP requests and responses.
- **Service Layer**: Contains business logic.
- **Repository Layer**: Interfaces with the database.
- **Security Layer**: Manages authentication and token validation.
- **Messaging Layer**: Sends email notifications via a notification service.

---

## Setup and Installation

### Prerequisites
- Java Development Kit (JDK) 21
- Maven 3.9+
- PostgreSQL
- Docker (optional)

### Clone the Repository
```bash
git clone https://github.com/PRPO-ekipa03/user-service.git
cd userservice
```

### Build the Project
```bash
mvn clean package -DskipTests
```

---

## Configuration
The application reads configuration from environment variables or a properties file. Key parameters include:

```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Notification Service
notification.service.url=http://${NOTIFICATION_SERVICE_HOST:localhost}:8086

# JWT Secret
jwt.secret=${JWT_SECRET}

# Server Configuration
server.port=8081
```

Set these variables as environment variables or modify `application.properties`.

---

## Running the Application

### Locally
Run the application with:
```bash
java -jar target/user-service.jar
```  
The service will start on port 8081 or the port specified in your configuration.

### Using Docker
To containerize the application:

1. **Build the Docker Image**:  
```bash
docker build -t user-service .
```  

2. **Run the Docker Container**:  
```bash
docker run -p 8081:8081 \
  -e DB_HOST=your_db_host \
  -e DB_PORT=your_db_port \
  -e DB_NAME=your_db_name \
  -e DB_USERNAME=your_db_username \
  -e DB_PASSWORD=your_db_password \
  -e NOTIFICATION_SERVICE_HOST=your_notification_service_host \
  -e JWT_SECRET=your_jwt_secret \
  user-service
```  

---

## API Documentation
The Swagger UI is available at:  
```  
http://localhost:8081/users/swagger-ui  
```

---

## Endpoints Overview

### Authentication Endpoints
- **Validate Token**: `POST /api/auth/validate-token`
- **Register User**: `POST /api/auth/register`
- **Confirm User**: `GET /api/auth/confirm`
- **Login User**: `POST /api/auth/login`
- **Request Password Reset**: `POST /api/auth/password-reset-request`
- **Reset Password**: `PATCH /api/auth/password-reset`

### User Management Endpoints
- **Get User by ID**: `GET /api/users/{id}`
- **Update User Information**: `PUT /api/users/{id}`
- **Delete User**: `DELETE /api/users/{id}`

---

## Error Handling
The service provides detailed error responses for common issues:
- **400 Bad Request**: Validation errors or invalid input.
- **401 Unauthorized**: Invalid credentials or token.
- **404 Not Found**: Resource not found.
- **409 Conflict**: User already exists.
- **500 Internal Server Error**: Unexpected server errors.

