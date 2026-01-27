📘 ShiftPlanner – README
🧭 Projektübersicht

ShiftPlanner ist eine Java-basierte Backend-Anwendung zur Verwaltung von Mitarbeiterschichten, Verfügbarkeiten und wöchentlichen Anforderungen.
Das Projekt wurde als Lern- und Praxisprojekt mit Spring Boot, JPA/Hibernate und PostgreSQL umgesetzt und legt besonderen Fokus auf saubere Architektur,
Datenkonsistenz und Erweiterbarkeit.

 Hauptfunktionen

Verwaltung von Mitarbeitern

Pflege von Verfügbarkeiten (Wochentag, Schicht, verfügbar/nicht verfügbar)

Definition von wöchentlichen Personalanforderungen

Persistente Speicherung über PostgreSQL

Datenbankmigrationen mit Flyway

Validierung der Datenbankstruktur über Hibernate

 Architektur & Technologien
Technologie-Stack

Java 21

Spring Boot 4

Spring Data JPA / Hibernate

PostgreSQL

Flyway (Datenbankmigration)

Maven

Docker & Docker Compose

Architektur

Das Projekt folgt einer klassischen Schichtenarchitektur:

Controller-Schicht
REST-Schnittstellen und Request-Handling

Service-Schicht
Geschäftslogik und Validierung

Repository-Schicht
Datenzugriff mit Spring Data JPA

Datenbank
PostgreSQL mit versionierten Migrationen

🗄️ Datenbank & Migrationen

Die Datenbank wird vollständig über Flyway verwaltet.

Migrationsstruktur
src/main/resources/db/migration
├── V1_init.sql
├── V2_seed_dev_data.sql
├── V3_seed_availability.sql
└── V4_seed_weekly_requirement.sql


V1: Initiales Schema

V2–V4: Seed-Daten für Entwicklung und Tests

⚠️ Hinweis:
Migrationsdateien müssen strikt dem Flyway-Namensschema folgen (Vx__beschreibung.sql).

⚙️ Konfiguration
Lokale Entwicklung (application.properties)
spring.application.name=shiftplanner-api

spring.datasource.url=jdbc:postgresql://localhost:5433/shiftplanner
spring.datasource.username=shiftplanner
spring.datasource.password=shiftplanner

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.locations=classpath:db/migration

server.port=8585

Docker-Profil (application-docker.properties)
spring.datasource.url=jdbc:postgresql://db:5432/shiftplanner
spring.datasource.username=shiftplanner
spring.datasource.password=shiftplanner

spring.flyway.enabled=false
spring.jpa.hibernate.ddl-auto=validate

🐳 Docker & Docker Compose
docker-compose.yml
version: "3.8"

services:
  db:
    image: postgres:15
    container_name: shiftplanner-db
    environment:
      POSTGRES_DB: shiftplanner
      POSTGRES_USER: shiftplanner
      POSTGRES_PASSWORD: shiftplanner
    ports:
      - "5433:5432"

  app:
    build: .
    container_name: shiftplanner-app
    depends_on:
      - db
    environment:
      SPRING_PROFILES_ACTIVE: docker
    ports:
      - "8585:8585"

▶️ Start & Ausführung
Lokale Ausführung (ohne Docker)

PostgreSQL starten

Datenbank anlegen:

CREATE DATABASE shiftplanner;


Projekt starten:

./mvnw spring-boot:run -Dspring-boot.run.profiles=local

Docker-basiert
docker-compose up --build


Die Anwendung ist anschließend erreichbar unter:

👉 http://localhost:8585

 Tests

Integrationstests mit Spring Boot Test

Verwendung einer realen PostgreSQL-Instanz

Flyway-Migrationen werden vor Tests validiert

 Projektstatus

 Datenbank & Migrationen stabil

 JPA-Repositories funktionsfähig

 Grundlegende Geschäftslogik umgesetzt

 REST-API wird sukzessive erweitert

 Security-Konfiguration aktuell nur für Entwicklung geeignet

 Ausblick / Erweiterungen

REST-API-Dokumentation (OpenAPI / Swagger)

Rollen- und Rechtesystem

Erweiterte Schichtplanung (Konflikterkennung)

Frontend (z. B. Vaadin oder React)

CI-Pipeline (GitHub Actions)

 Hinweise

spring.jpa.open-in-view ist aktuell aktiv (Default) – für Produktion deaktivieren

Das automatisch generierte Security-Passwort ist nur für Entwicklung

Wenn du willst, kann ich als Nächstes:

eine englische README daraus machen

eine Kurz-README für Bewerbungen schreiben

oder die Docker-/Flyway-Sektion noch kompakter formulieren
