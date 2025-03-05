# Library Management System

A Spring Boot application for library management system.

## Features

- User Authentication with JWT & User management (Create, Delete)
- Book Management (Add, Update, Delete, Search)
- Transaction Management (Borrow, Return)
- Simple Logging and Monitoring with Spring Actuator

## Tech Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL
- JWT Authentication
- Docker

## Prerequisites

- JDK 17+
- Maven
- Docker
- PostgreSQL

## Getting Started

### Clone the Repository

```bash
git clone <repository-url>
cd app
```

### Configure Application

Use `application.properties` 


### Build and Run

#### Using Maven

```bash
./mvnw clean install -DskipTests

```

#### Using Docker

```bash
# Build Docker image
docker build -f Dockerfile -t librarymanagementapplication .

# Run the container
docker run -p 8081:8080 librarymanagementapplication
```

## API Endpoints and DBs

Refer to the API documentation: [API Documentation](./LMS-API-Documentation.docx)




