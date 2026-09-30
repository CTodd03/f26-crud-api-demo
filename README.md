# f26-crud-api-demo

# Plant Tracker API

A REST API for tracking houseplants, built with Spring Boot using a layered architecture. Data is stored in PostgreSQL (Neon).

## Features

- Create, read, update, and delete plants
- Partial, case-insensitive search by name
- Filter by light requirements
- Bean validation with clear HTTP status codes (400, 404)
- Configured entirely through environment variables (no secrets in the repo)

## Tech Stack

| Area | Technology |
|------|------------|
| Language | Java 25 |
| Framework | Spring Boot (Web, Data JPA, Validation) |
| Database | PostgreSQL on [Neon](https://neon.com) |
| Build | Maven |

Generate a starter project at [start.spring.io](https://start.spring.io) with: **Spring Web, Spring Data JPA, Validation, PostgreSQL Driver**.

## Architecture

The app uses four layers. Each layer talks only to the one below it.

```
HTTP request
    |
Controller   - maps URLs to methods, validates input, returns HTTP responses
    |
Service      - business logic and orchestration, throws domain exceptions
    |
Repository   - data access (Spring Data JPA), generates SQL
    |
Entity       - Java class mapped to the `plants` table
    |
PostgreSQL (Neon)
```

### Project Structure

```
src/main/java/com/csc340/plants/
  PlantController.java
  PlantService.java
  PlantRepository.java
  Plant.java
  LightNeeds.java (enum)
  PlantNotFoundException.java
src/main/resources/
  application.properties
```

## Data Model

**Plant** (table `plants`)

| Field | Type | Rules |
|-------|------|-------|
| `id` | Long | Generated automatically |
| `name` | String | Required, not blank |
| `lightNeeds` | Enum: `LOW`, `MEDIUM`, `BRIGHT` | Required |
| `lastWatered` | Date (`yyyy-MM-dd`) | Required |
| `wateringIntervalDays` | int | Must be positive |
| `toxicToPets` | boolean | Defaults to false |

## API Reference

Base path: `/api/plants`

| Method | Path | Description | Success |
|--------|------|-------------|---------|
| GET | `/api/plants` | List all plants (supports filters below) | 200 |
| GET | `/api/plants/{id}` | Get one plant | 200 |
| POST | `/api/plants` | Create a plant | 201 |
| PUT | `/api/plants/{id}` | Replace a plant's fields | 200 |
| DELETE | `/api/plants/{id}` | Delete a plant | 204 |

### Query parameters for `GET /api/plants`

| Parameter | Example | Behavior |
|-----------|---------|----------|
| `name` | `?name=mon` | Partial, case-insensitive match ("mon" finds "Monstera") |
| `light` | `?light=LOW` | Exact match on `LOW`, `MEDIUM`, or `BRIGHT` |
| both | `?name=mon&light=MEDIUM` | Both filters must match |

Blank or missing parameters are ignored.

### Example request

```http
POST /api/plants
Content-Type: application/json

{
  "name": "Monstera",
  "lightNeeds": "MEDIUM",
  "lastWatered": "2026-09-20",
  "wateringIntervalDays": 7,
  "toxicToPets": true
}
```

### Status codes

| Code | Meaning |
|------|---------|
| 200 | OK |
| 201 | Created |
| 204 | Deleted, no content |
| 400 | Validation failed or malformed request (for example, an invalid `lightNeeds` value) |
| 404 | Plant with that id does not exist |

## Configuration

The app reads its database settings from environment variables. Spring Boot maps these names automatically, so nothing secret is stored in `application.properties`.

| Variable | Example |
|----------|---------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<neon-host>/<db>?sslmode=require` |
| `PORT` | Set by Render; defaults to `8080` locally |

## Running Locally

1. Create a Neon project and gather your connection details.
2. Set the  `SPRING_DATASOURCE_URL` environment variable.
    - Mac/Linux: `export SPRING_DATASOURCE_URL="jdbc:postgresql://<neon-host>/<db>?sslmode=require"`
    - Windows: `set SPRING_DATASOURCE_URL=jdbc:postgresql://<neon-host>/<db>?sslmode=require`
    - PowerShell: `$env:SPRING_DATASOURCE_URL="jdbc:postgresql://<neon-host>/<db>?sslmode=require"`
3. Start the app:

```bash
./mvnw spring-boot:run        # Mac / Linux
.\mvnw.cmd spring-boot:run    # Windows
```

4. Visit `http://localhost:8080/api/plants`. A fresh database returns `[]`.

Hibernate creates the `plants` table automatically on first run (`spring.jpa.hibernate.ddl-auto=update`).