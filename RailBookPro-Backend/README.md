# RailBook Pro — Backend

Production-style Railway Ticket Booking System REST API built with Spring Boot 3, Spring Data JPA, Spring Security (JWT), and MySQL.

## Tech Stack

- Java 17 (Spring Boot 3.x baseline; the original spec targeted Java 22 but this build runs on the installed JDK 17)
- Spring Boot 3.3.4 — Spring MVC, Spring Data JPA, Spring Security
- Hibernate ORM 6.5 (with community legacy dialect for MySQL 5.x hosts)
- MySQL (online / free tier)
- JWT authentication (jjwt), BCrypt password hashing
- Bean Validation, Lombok, Global exception handling, DTO + Mapper pattern
- springdoc OpenAPI (Swagger UI)

## Architecture

```
Controller  ->  Service  ->  Repository  ->  Database
                  |
              DTO <-> Mapper <-> Entity
```

Packages under `com.railbookpro`:

| Package       | Responsibility                                  |
|---------------|-------------------------------------------------|
| `domain`      | JPA entities + enums                            |
| `repository`  | Spring Data JPA repositories                    |
| `dto`         | Request/response DTOs (grouped by feature)      |
| `mapper`      | Entity <-> DTO mappers                           |
| `service`     | Business logic (transactional)                  |
| `controller`  | REST controllers                                |
| `security`    | JWT service, filter, user details, utils        |
| `config`      | Security, CORS, OpenAPI, data seeder            |
| `exception`   | Custom exceptions + `@RestControllerAdvice`     |
| `util`        | Helpers (PNR generator)                         |

## Configuration

Database and other settings live in `src/main/resources/application.properties`.
The provided online MySQL database is already configured. Key settings:

- `spring.datasource.*` — MySQL connection (freesqldatabase.com)
- `spring.jpa.hibernate.ddl-auto=update` — tables auto-created/updated on startup
- `railbook.seed-data=true` — seeds sample data once on an empty database
- `railbook.jwt.secret` / `railbook.jwt.expiration-ms` — JWT signing + TTL
- `railbook.cors.allowed-origins` — allowed frontend origins

> The free MySQL host runs **MySQL 5.5**, is slow to connect (~40s cold), and drops
> idle connections. The Hikari pool is tuned (short max-lifetime, keepalive,
> validation query) to tolerate this. First startup can take 60–110s; subsequent
> startups ~18–40s. This is a hosting limitation, not an app bug.

## Running

Prerequisites: JDK 17+, Maven 3.9+.

```bash
cd RailBookPro-Backend
mvn spring-boot:run
```

The API is served at: `http://localhost:8080/api`

Swagger UI: `http://localhost:8080/api/swagger-ui.html`

### Sample data

On first run against an empty DB, `DataSeeder` inserts roles, 5 users, 6 stations,
5 trains, 5 routes, 5 schedules, 5 bookings, and 5 notifications. It is skipped if
users already exist. Alternatively, run `schema.sql` then `data.sql` manually.

### Demo credentials

| Role      | Username | Password    |
|-----------|----------|-------------|
| Admin     | `admin`  | `admin123`  |
| Passenger | `rahul`  | `password123` |
| Passenger | `priya`  | `password123` |
| Passenger | `arjun`  | `password123` |
| Passenger | `sneha`  | `password123` |

## Authentication

1. `POST /api/auth/login` with `{ "usernameOrEmail": "...", "password": "..." }`
2. Response contains a `token`. Send it on protected requests:
   `Authorization: Bearer <token>`

## REST API Reference

Base path: `/api`. All responses are wrapped as
`{ "success": boolean, "message": string, "data": <payload> }`.

### Auth
| Method | Endpoint          | Access | Description                |
|--------|-------------------|--------|----------------------------|
| POST   | `/auth/register`  | Public | Register a new passenger   |
| POST   | `/auth/login`     | Public | Login, returns JWT         |

### Users
| Method | Endpoint                    | Access        | Description             |
|--------|-----------------------------|---------------|-------------------------|
| GET    | `/users`                    | ADMIN         | List all users          |
| GET    | `/users/search?q=`          | ADMIN         | Search users            |
| GET    | `/users/{id}`               | ADMIN         | Get user by id          |
| POST   | `/users`                    | ADMIN         | Create user             |
| PUT    | `/users/{id}`               | ADMIN         | Update user             |
| DELETE | `/users/{id}`               | ADMIN         | Delete user             |
| GET    | `/users/me`                 | Authenticated | Current profile         |
| PUT    | `/users/me`                 | Authenticated | Update own profile      |
| POST   | `/users/me/change-password` | Authenticated | Change own password     |

### Stations / Trains / Routes / Schedules
| Method       | Endpoint                       | Access | Description                 |
|--------------|--------------------------------|--------|-----------------------------|
| GET          | `/stations`, `/stations/{id}`  | Public | Read stations               |
| POST/PUT/DEL | `/stations`, `/stations/{id}`  | ADMIN  | Manage stations             |
| GET          | `/trains`, `/trains/{id}`      | Public | Read trains                 |
| GET          | `/trains/search`               | Public | Search (source, destination, date, trainNumber, trainName) |
| POST/PUT/DEL | `/trains`, `/trains/{id}`      | ADMIN  | Manage trains               |
| GET          | `/routes`, `/routes/{id}`      | Public | Read routes                 |
| POST/PUT/DEL | `/routes`, `/routes/{id}`      | ADMIN  | Manage routes               |
| GET          | `/schedules`, `/schedules/{id}`| Public | Read schedules              |
| GET          | `/schedules/train/{trainId}`   | Public | Schedules for a train       |
| POST/PUT/DEL | `/schedules`, `/schedules/{id}`| ADMIN  | Manage schedules            |

### Bookings
| Method | Endpoint                 | Access        | Description                     |
|--------|--------------------------|---------------|---------------------------------|
| GET    | `/bookings`              | ADMIN         | All bookings                    |
| GET    | `/bookings/me`           | Authenticated | Own booking history             |
| GET    | `/bookings/{id}`         | Owner/ADMIN   | Booking detail                  |
| POST   | `/bookings`              | Authenticated | Create booking (generates PNR)  |
| POST   | `/bookings/{id}/cancel`  | Owner/ADMIN   | Cancel booking, restores seats  |

### Notifications
| Method | Endpoint                  | Access        | Description         |
|--------|---------------------------|---------------|---------------------|
| GET    | `/notifications/me`       | Authenticated | Own notifications   |
| POST   | `/notifications/{id}/read`| Authenticated | Mark as read        |
| DELETE | `/notifications/{id}`     | Authenticated | Delete              |

### Admin dashboard & reports
| Method | Endpoint                    | Access | Description                 |
|--------|-----------------------------|--------|-----------------------------|
| GET    | `/admin/dashboard`          | ADMIN  | Aggregated dashboard stats  |
| GET    | `/admin/reports/bookings`   | ADMIN  | Booking report              |
| GET    | `/admin/reports/trains`     | ADMIN  | Train report                |
| GET    | `/admin/reports/users`      | ADMIN  | User report                 |
| GET    | `/admin/reports/summary`    | ADMIN  | Summary (revenue, counts)   |

## Example: create a booking

```bash
# 1. Login
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"usernameOrEmail":"rahul","password":"password123"}' \
  | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# 2. Book a ticket
curl -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "trainId": 1,
    "journeyDate": "2026-08-15",
    "travelClass": "SL",
    "seatCount": 1,
    "passengers": [ { "name": "Rahul Sharma", "age": 30, "gender": "MALE" } ]
  }'
```

## Travel-class fare multipliers

Base fare is per train; total = `baseFare × classMultiplier × seatCount`:

| Class | Meaning              | Multiplier |
|-------|----------------------|-----------:|
| SL    | Sleeper              | 1.0        |
| CC    | Chair Car            | 1.3        |
| AC3   | AC 3-Tier            | 1.6        |
| AC2   | AC 2-Tier            | 2.2        |
| EC    | Executive Chair Car  | 2.8        |
| AC1   | AC First Class       | 3.5        |

## Notes

- This backend is standalone; it does not depend on the frontend.
- No payment gateway or third-party booking APIs (per spec).
- Passwords are BCrypt-hashed; JWT is stateless (no server sessions).
