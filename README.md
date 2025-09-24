# Job Portal Platform

A full-stack job marketplace featuring Spring Boot REST APIs with JWT security and an Angular 17 experience designed for job seekers, employers, and administrators.

## Features

### Authentication & Roles
- JWT-based authentication with role-aware authorization (Job Seeker, Employer, Admin).
- BCrypt password hashing and activation toggles for user management.

### Job Seeker Experience
- Profile management with resume upload (PDF/DOC/DOCX scanning & storage).
- Discover roles via keyword & status filtering, save favorites, apply with cover letters, and monitor application status.

### Employer Workspace
- Publish, edit, archive, and delete job postings.
- Review applicants per posting and update application statuses.

### Admin Console
- Moderate user accounts and job postings.
- Platform health dashboard with counts of users, jobs, and applications.

## Project Structure

```
backend/   Spring Boot REST API (Java 17, Spring Boot 3)
frontend/  Angular 17 SPA (SCSS styling)
```

## Running the Backend

```bash
cd backend
mvn spring-boot:run
```

The server starts on `http://localhost:8080` with REST endpoints mounted under `/api`.

> **Note:** Offline environments require pre-populated Maven repositories for dependencies.

Seed accounts (password `password` for all):
- Admin — `admin@jobportal.com`
- Employer — `employer@jobportal.com`
- Job Seeker — `seeker@jobportal.com`

## Running the Frontend

```bash
cd frontend/job-portal-frontend
npm install
npm start
```

The Angular dev server runs on `http://localhost:4200` and proxies API calls to the backend via environment configuration.

## Testing

- Backend: `mvn test`
- Frontend: `npm test`

## API Highlights

- `POST /api/auth/signup`, `POST /api/auth/login`
- `GET /api/jobs`, `POST /api/jobs/{id}/favorite`
- `POST /api/employer/jobs`, `PATCH /api/employer/jobs/{id}/status`
- `POST /api/applications`, `GET /api/applications/me`
- `GET /api/admin/users`, `GET /api/admin/reports`

## Security

- Spring Security with JWT bearer tokens.
- BCrypt password encoding, activation toggles, and security guards on all protected routes.
- Resume uploads validated for size & extension before storage.
