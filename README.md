# HosDoc Backend

Monolithic Spring Boot backend service for HosDoc Hospital Management System.

## Tech Stack

- Java 17 (LTS)
- Spring Boot 3.4.0
- Spring Data JPA
- MySQL 8.0
- Spring Security OAuth2 Resource Server (JWT)
- Flyway (Database Migrations)
- Eureka (Service Discovery)
- OpenAPI/Swagger

## Features

- **Specialty Management**: Browse specialties and doctors
- **Doctor Management**: Search doctors, view details, check availability
- **Patient Management**: Manage patient members (family members)
- **Appointment Booking**: Book, cancel, and reschedule appointments
- **Report Management**: Upload and manage medical reports
- **JWT Authentication**: Secure API endpoints with JWT tokens

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Docker (optional, for containerized deployment)

## Configuration

### Database Setup

1. Create MySQL database:
```sql
CREATE DATABASE hosdoc;
```

2. Update `application.properties` with your database credentials:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hosdoc
spring.datasource.username=root
spring.datasource.password=your_password
```

### JWT Configuration

The service expects JWT tokens from the auth service. Configure the JWK Set URI:
```properties
app.auth.jwk-set-uri=http://localhost:8081/auth/.well-known/jwks.json
```

## Running the Application

### Local Development

1. Start MySQL database
2. Start Eureka Server (port 8761)
3. Start Auth Service (port 8081)
4. Run the backend service:
```bash
mvn spring-boot:run
```

The service will start on port 8082.

### Docker

```bash
docker-compose up -d
```

## API Documentation

Once the application is running, access Swagger UI at:
```
http://localhost:8082/swagger-ui.html
```

API Docs (OpenAPI JSON):
```
http://localhost:8082/v3/api-docs
```

## API Endpoints

### Public Endpoints (No Auth Required)

- `GET /api/v1/specialties` - Get all specialties
- `GET /api/v1/specialties/{id}` - Get specialty by ID
- `GET /api/v1/specialties/{id}/doctors` - Get doctors by specialty
- `GET /api/v1/doctors` - Search doctors (with filters)
- `GET /api/v1/doctors/{doctorId}` - Get doctor details
- `GET /api/v1/doctors/{doctorId}/availability` - Get doctor availability

### Authenticated Endpoints (JWT Required)

#### Patients & Members
- `GET /api/v1/patients/me/members` - Get all members
- `POST /api/v1/patients/me/members` - Create a new member
- `PUT /api/v1/patients/me/members/{memberId}` - Update member
- `DELETE /api/v1/patients/me/members/{memberId}` - Delete member

#### Appointments
- `POST /api/v1/appointments` - Book appointment
- `GET /api/v1/appointments/{appointmentUid}` - Get appointment by UID
- `GET /api/v1/patients/me/appointments` - Get user's appointments
- `PUT /api/v1/appointments/{appointmentUid}/cancel` - Cancel appointment
- `PUT /api/v1/appointments/{appointmentUid}/reschedule` - Reschedule appointment

#### Reports
- `POST /api/v1/reports` - Upload report (multipart/form-data)
- `GET /api/v1/reports/{reportId}` - Get report by ID
- `GET /api/v1/patients/me/reports` - Get user's reports

## Database Migrations

Flyway migrations are located in `src/main/resources/db/migration/`. Migrations run automatically on application startup.

## File Uploads

Report files are stored in `./uploads/reports/` by default. Configure the upload directory in `application.properties`:
```properties
app.file.upload-dir=./uploads/reports
```

For production, consider using S3 or GCS by updating the `StorageProvider` enum and implementing cloud storage in `ReportServiceImpl`.

## Testing

Run tests:
```bash
mvn test
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/kalex/hosdoc_backend/
│   │       ├── config/          # Configuration classes
│   │       ├── controller/      # REST controllers
│   │       ├── dto/             # Data Transfer Objects
│   │       ├── entity/          # JPA entities
│   │       ├── enums/           # Enumerations
│   │       ├── exception/       # Exception handlers
│   │       ├── repository/      # JPA repositories
│   │       ├── service/         # Service interfaces and implementations
│   │       └── util/            # Utility classes
│   └── resources/
│       ├── db/migration/        # Flyway migrations
│       └── application.properties
└── test/
```

## License

This project is part of the HosDoc Hospital Management System.

