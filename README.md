# Interactive Story Generator
An AI-powered platform for creating branching narrative stories with multiple storylines and characters.

## Tech Stack
- Frontend: React + Vite, DaisyUI, Bootstrap Icons
- Backend: Spring Boot
- Database: Supabase

## Quick Setup

### Prerequisites
- Node.js & npm
- Java 17+
- Supabase account

### Installation

```bash
# Frontend
cd frontend-vite
npm install
npm install yarn
yarn add bootstrap-icons
npm run dev

# Backend
cd backend
gradle build
gradle bootRun
```

### Environment variables
In `backend/src/resources/applications.properties`

```
# # PostgreSQL Configuration
spring.application.name=narrative

# Supabase database
spring.datasource.url=jdbc:postgresql://aws-0-ap-southeast-2.pooler.supabase.com:5432/postgres?user=postgres.rxqektixpglkxiantouz&password=Narrative123jjjja
spring.datasource.username=postgres.rxqektixpglkxiantouz
spring.datasource.password=Narrative123jjjja

spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Supabase auth

supabase.url=https://rxqektixpglkxiantouz.supabase.co
supabase.anon.key=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJ4cWVrdGl4cGdsa3hpYW50b3V6Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3Mjk5MTUyNzEsImV4cCI6MjA0NTQ5MTI3MX0.p3X1W2WD6izvJ_GUyc9_tAKD2Iz59hsQnaORBUu_i2o

API_KEY=AIzaSyCHNf0YPDwmWjlH5ZIIlSssHt4R8jExH1M
```