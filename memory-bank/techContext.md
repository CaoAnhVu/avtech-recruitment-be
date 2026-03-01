# Technical Context

## Tech Stack

### Backend (The Core)
-   **Language:** Java 17+ (LTS).
-   **Framework:** Spring Boot 3.2+.
-   **Security:** Spring Security 6 (Oauth2 Resource Server / JWT).
-   **Persistence:** Spring Data JPA (Hibernate), Liquibase (Migrations).
-   **Messaging:** Spring Kafka.
-   **Utilities:** MapStruct (DTO mapping), Lombok (Boilerplate), Jackson (JSON).
-   **API Doc:** OpenAPI 3 / Swagger UI.

### Frontend (Web Dashboard)
-   **Framework:** React 18 (Concurrent Mode).
-   **Language:** TypeScript 5+.
-   **Build Tool:** Vite 5 (Super fast HMR).
-   **UI Library:** Ant Design 5 (Enterprise components).
-   **Styling:** Tailwind CSS v4 (Utility-first).
-   **State Management:**
    -   **Server State:** TanStack Query v5 (Caching, invalidation).
    -   **Client State:** Zustand (Lightweight global store).
    -   **Forms:** React Hook Form + Zod (Validation).

### Mobile App (Candidate & HR)
-   **Framework:** React Native 0.73+.
-   **Language:** TypeScript.
-   **Navigation:** React Navigation 6 (Native stack).
-   **Offline:** SQLite / WatermelonDB (for robust offline data) or AsyncStorage (simple key-value).
-   **Sync:** TanStack Query (React Query) for offline-first data fetching strategies.

### Infrastructure & DevOps
-   **Containerization:** Docker, Docker Compose (Local dev).
-   **Orchestration:** Kubernetes (K8s) ready (Manifests to be created).
-   **Reverse Proxy:** Nginx (SSL termination, Load Balancing).
-   **CI/CD:** GitHub Actions / GitLab CI (Build -> Test -> Docker Push -> Deploy).
-   **Monitoring:** Prometheus + Grafana (Metrics), ELK Stack (Logs).

## Development Environment
-   **IDE:** IntelliJ IDEA (Backend), VS Code (Frontend/Mobile).
-   **Java SDK:** Amazon Corretto 17.
-   **Node:** v20 (LTS).
-   **Databases:**
    -   MySQL 8.0 (Primary).
    -   Redis 7 (Cache/queue).

## Deployment Strategy
We support a **multi-stage deployment pipeline**:
1.  **Local:** Docker Compose spinning up all services.
2.  **Dev/Staging:** Single VPS or small K8s cluster.
3.  **Production:** Auto-scaling group behind Load Balancer, Managed RDS, Managed Redis.

## Important Constraints
-   All APIs must output JSON.
-   Datetimes must be ISO-8601 UTC.
-   Money must be handled as `BigDecimal` (Java) / String (JSON) to avoid precision loss.
-   Primary Keys should be UUIDs or TSIDs (Time-Sorted Unique Identifiers) to avoid enumeration attacks and fragmentation.
