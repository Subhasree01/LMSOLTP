# Library Management System

A Spring Boot application for library management system.

## Features

- User Authentication with JWT & User management (Create, Delete)
- Book Management (Add, Update, Delete, Search)
- Transaction Management (Borrow, Return)
- Opentelemetry integration & Jeager for generating/collecting logs, traces.
- Sample testcases provided with Talent API collections. (Instead of swagger/open API)
- Redis caching for frequently accessed book details

## Tech Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL
- JWT Authentication
- Docker
- Opentelemetry
- Jeager
- Redis

## Prerequisites

- JDK 17+
- Maven
- Docker
- PostgreSQL
- Google Chrome (Add Talent API Tester extension)
- Install Redis

## Getting Started

### Clone the Repository

```bash
git clone git@github.com:Subhasree01/LMSOLTP.git
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
# Build Docker image & Run
docker compose up --build
```

## API Endpoints and DBs

1. Connect the Postgres in any DB tool such as Dbeaver or PGAdmin using below credentials.

      - POSTGRES_DB=LMSDatabase
      - POSTGRES_USER=LMSDatabase
      - POSTGRES_PASSWORD=LMSDatabase
      - port: 5432

2. Run the DB scripts before start testing API (find the scripts in the below API documentation)

Refer to the API documentation: [API Documentation](./LMS-API-Documentation.docx)

## API Testing

Refer Talent API collections for interactive testing [API Collections](./LibraryManagementCollections.json) 

Import above Json collection from Talent API Tester.

## View Traces

Hit the API request from Talent API tester and verify the traces collected by Jeager.

http://localhost:16686/search




