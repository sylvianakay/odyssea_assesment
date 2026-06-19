# Job Skill Matcher

Small full-stack app that matches a user with jobs that overlap with at least one saved skill.

## Why This Stack

- **Spring Boot + Java**: strong structure for layered APIs (controller/service/repository), built-in validation, and security support.
- **React + TypeScript (Vite)**: fast setup for a minimal UI, type safety for API contracts, and simple routing for auth-protected pages.
- **PostgreSQL**: reliable relational model for users, skills, jobs, and many-to-many links.
- **Docker Compose**: reproducible local database setup for evaluator and development.

## Short Approach Summary

The backend is organized around clear responsibilities:

- `auth`: registration, login, password hashing, JWT generation/validation
- `skill`: save and read user skills with normalization
- `job`: load static job catalog and compute matches by skill overlap
- `config/common`: security filter chain, JWT filter, and consistent API error responses

The frontend focuses on a minimal but usable flow:

- register page
- login page
- protected skills and matches pages
- logout and route guard behavior

## Core Functionality Implemented

- User can register and log in.
- User can save/update their skills.
- App returns jobs that share at least one user skill.
- Minimal frontend supports full flow end-to-end.

## Architecture and Data Model

Main tables:

- `users` (`id`, `email`, `password_hash`)
- `skills` (`id`, `name`)
- `jobs` (`id`, `title`, `company`, `description`)
- `user_skills` (`user_id`, `skill_id`)
- `job_skills` (`job_id`, `skill_id`)

Matching rule:

- Normalize user and job skills (trim + lowercase).
- Return jobs where overlap is `>= 1`.
- Include matched skills in response.

## Assumptions

- Single user role (no admin role needed for this exercise).
- Jobs are read-only and come from static `jobs.json`.
- Skill list update replaces the full list (`PUT` semantics).

## Trade-Offs

- Used direct many-to-many mappings for `User<->Skill` and `Job<->Skill` to keep the model simple for this scope.
- In production, I would likely use explicit join entities (`UserSkill`, `JobSkill`) for better extensibility (metadata, ranking, timestamps).
- JWT is stateless and simple for this app; token revocation and refresh token flow are not implemented.
- Skill matching currently loads jobs and filters in service logic; for very large datasets this should move toward query-level optimization.

## API Overview

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/users/me/skills`
- `PUT /api/users/me/skills`
- `GET /api/jobs/matches`

Protected endpoints require:

- `Authorization: Bearer <token>`

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- Docker Desktop

## Setup and Run

### 1) Start PostgreSQL

From repository root:

```bash
docker compose up -d
docker compose ps
```

Configured values:

- Database: `jobmatcher`
- User: `postgres`
- Password: `postgres`
- Port: `5433`

### 2) Run backend

```bash
cd backend/demo
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`.

### 3) Run frontend

In a second terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`.

## Validation / Test Commands

Backend tests:

```bash
cd backend/demo
mvn test
```

Frontend build check:

```bash
cd frontend
npm run build
```

## Notes for Evaluator

- CORS is explicitly configured for frontend origin `http://localhost:5173`.
- Database connection is configured for PostgreSQL on `localhost:5433`.
- Job seed data is loaded once (idempotent) when jobs table is empty.

