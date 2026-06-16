# Job Skill Matcher

A full-stack app that matches users with jobs based on shared skills.

## Tech Stack

- Backend: Spring Boot (Java 17), Spring Data JPA, Maven
- Frontend: React + TypeScript + Vite
- Database: PostgreSQL (Docker Compose)

## Project Structure

- `backend/demo` - Spring Boot API
- `frontend` - React app
- `docker-compose.yml` - PostgreSQL setup

## Prerequisites

- Java 17+
- Maven 3.9+
- Node.js 18+
- Docker Desktop

## Run the Project

### 1) Start PostgreSQL

From repository root:

```bash
docker compose up -d
docker compose ps
```

Expected DB values:

- Database: `jobmatcher`
- User: `postgres`
- Password: `postgres`
- Port: `5432`

### 2) Run backend

```bash
cd backend/demo
mvn spring-boot:run
```

### 3) Run frontend

In a second terminal:

```bash
cd frontend
npm install
npm run dev
```

## Current Status

- Spring Boot and React templates are set up
- Entities: `User`, `Skill`, `Job`
- Relations: `user_skills` and `job_skills` (`requiredSkills`)
- `User.email` is unique

## Data Model

- `users` (`id`, `email`, `password_hash`)
- `skills` (`id`, `name`)
- `jobs` (`id`, `title`, `company`, `description`)
- `user_skills` links users to their skills (`user_id`, `skill_id`)
- `job_skills` links jobs to required skills (`job_id`, `skill_id`)

