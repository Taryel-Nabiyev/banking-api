# Banking API

A RESTful Banking API built with Spring Boot, featuring JWT authentication,
role-based authorization, and containerized with Docker.

## Tech Stack

- Java 17
- Spring Boot
- Spring Security + JWT
- PostgreSQL
- Docker & Docker Compose
- MapStruct
- JUnit 5 + Mockito

## Features

- User registration and login with JWT authentication
- Password hashing with BCrypt
- Role-based authorization (ADMIN, USER)
- Bank account creation
- Money transfers between accounts
- Transaction history
- Global exception handling
- Unit tests for core business logic

## How to Run

Make sure you have Docker installed, then:

```bash
docker-compose up --build
```

App runs on `http://localhost:8080`

## API Endpoints

### Auth
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login and get JWT token

### Account
- `POST /api/account/create` - Create bank account
- `POST /api/account/transfer` - Transfer money
- `GET /api/account/transactions` - Transaction history

### Admin
- `GET /api/admin/users` - Get all users (ADMIN only)