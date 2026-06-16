# Job Skill Matcher

full-stack web app that matches users with jobs based on overlapping skills.

## Tech Stack

- Backend: Spring Boot (Java 17), Spring Data JPA, Maven
- Frontend: React + TypeScript + Vite
- Database: PostgreSQL (Docker Compose)

## Project Structure

- `backend/demo`: Spring Boot API
- `frontend`: React app
- `docker-compose.yml`: PostgreSQL container setup

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

- Base Spring Boot project is set up
- `User` entity implemented with unique email constraint
- `Skill` entity implemented
- `User` <-> `Skill` relation implemented through `user_skills` join table
- Frontend Vite template is initialized

