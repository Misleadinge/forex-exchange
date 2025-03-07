# Forex Exchange Service

**Date:** 07.03.2025

## Overview
A Spring Boot application that provides foreign exchange rate services and currency conversion operations.

## Features
- Real-time exchange rate retrieval
- Currency conversion with transaction history
- RESTful API with Swagger documentation
- In-memory H2 database for transaction storage
- Caching support using Caffeine

## Tech Stack
- Java 21
- Spring Boot 3.2.3
- Spring OpenFeign
- H2 Database
- Swagger/OpenAPI
- JUnit 5 & Mockito
- Maven
- Docker
- Lombok
- Spring Data JPA
- Spring Boot Devtools

## API Endpoints
- `GET /api/v1/forex/rate` - Get exchange rate between two currencies
- `POST /api/v1/forex/convert` - Convert amount between currencies
- `GET /api/v1/forex/history` - Retrieve conversion history

## Getting Started

### Prerequisites
- Java 21
- Maven 3.6+
- Docker (optional)

### Running Locally
```bash
# Clone the repository
git clone [repository-url]

# Build the project
./mvnw clean package

# Run the application
./mvnw spring-boot:run
```

### Running with Docker
```bash
# Build and run with Docker Compose
docker-compose up --build
```

## API Documentation
Access Swagger UI at: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Testing
Run tests using:
```bash
./mvnw test
```

## Configuration
Key application properties:
```properties
# Exchange Rate API Configuration
forex.api.key=[api-key]
forex.api.url=http://api.currencylayer.com

# Database Configuration
spring.datasource.url=jdbc:h2:mem:forexdb
```
```properties
# Caching Configuration
cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(100) # This can be altered to change aximum array size
                .expireAfterWrite(1, TimeUnit.MINUTES) # This can be altered to change cache clean-up time
                .recordStats());
```


