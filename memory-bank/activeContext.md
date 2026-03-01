# Active Context

## Current Focus
**Phase:** Initialization & Architecture Design.
**Goal:** Establish the Memory Bank and System Foundation for the AI-driven Recruitment Platform.

## Recent Changes
-   Completed backend configuration for Interview & Calendar Management including Biweekly events.
-   Cleaned up frontend `trip-mego` boilerplates and converted it to `avtech-recruitment-web`.
-   Transitioned to **Parallel Feature-Slice Development** model per user instructions.

## Roadmap & Timeline
We follow a 3-Phase Strategy to ensure ROI and Scalability.

### Phase 1: MVP - Core Recruitment (Weeks 1-8)
*   **Goal:** Launch basic Job Board & ATS for first 10 tenants.
*   **Sprint 1-2:** Scaffolding, Auth Service, Tenant Management.
*   **Sprint 3-4:** Job Posting, Candidate Application Flow.
*   **Sprint 5-6:** Basic Interview Scheduling, Notifications.
*   **Sprint 7-8:** Mobile App (Candidate View), QA, Soft Launch.

### Phase 2: Growth - AI & Social (Weeks 9-16)
*   **Goal:** Engage users and automate workflows.
*   **Sprint 9-10:** Social Feed, Company Pages.
*   **Sprint 11-12:** AI Resume Parsing, Candidate Scoring Algorithm.
*   **Sprint 13-14:** Subscription Billing Integration (Stripe/PayPal).
*   **Sprint 15-16:** Analytics Dashboard, Mobile App (HR View).

### Phase 3: Scale - Ecosystem (Weeks 17-24)
*   **Goal:** High availability and advanced features.
*   **Sprint 17-18:** Advanced Search (Elasticsearch), Recommendation Engine.
*   **Sprint 19-20:** Chatbot Assistant, Video Interview Integration.
*   **Sprint 21-22:** Performance Optimization (Redis, CDN), Security Audit.
*   **Sprint 23-24:** Multi-region deployment prep, Enterprise features (SSO).

## Risk Matrix
| Risk | Probability | Impact | Mitigation |
| :--- | :--- | :--- | :--- |
| **Multi-tenancy Data Leak** | Low | Critical | Strict Hibernate Filters + Row Level Security (RLS) testing. |
| **AI Cost Overrun** | Medium | High | Token usage limits per tenant, caching AI responses. |
| **Mobile Sync Conflicts** | High | Medium | robust "Last-Write-Wins" strategy, clear UI for conflict resolution. |
| **Email Deliverability** | Medium | High | Use dedicated IP/Service (SendGrid/AWS SES) with proper DKIM/SPF. |

## Next Steps
1.  **Architecture Verification:** [Done]
    -   Backend connected to local MySQL/Redis via Docker.
    -   `TenantContext` and Liquibase set up.
2.  **Implementation Focus: Parallel Feature-Slices**
    -   **Slice 1: Auth UI & API:** Build Login Endpoint `/api/auth/login`, issue JWT -> Consume JWT via Axios intercepted Frontend UI.
    -   **Slice 2: Job Board:** Finish UI to retrieve the `/api/jobs` data for HR dashboard.
