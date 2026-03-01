# Product Context

## Problem Statement
Traditional recruitment platforms are fragmented, static, and inefficient.
-   **Recruiters** drown in irrelevant CVs and lack tools to manage the end-to-end hiring process effectively.
-   **Candidates** face "black hole" applications with no feedback and a disconnect from company culture.
-   **Enterprises** struggle with rigid, expensive software that lacks modern social engagement features.
-   **Startups** need affordable, scalable tools that grow with them.

## Solution Strategy
We are building a **Talent Ecosystem** that unifies:
1.  **Social Network:** To drive engagement and passive candidate discovery.
2.  **AI-Powered ATS:** To automate screening, ranking, and scheduling.
3.  **SaaS Platform:** To provide customizable, isolated environments for companies of all sizes.

## User Roles & Workflows

### 1. ROLE_ADMIN (Global Admin)
*   **Responsibility:** Platform oversight, tenant management, revenue monitoring.
*   **Key Capabilities:**
    *   CRUD Users, Assign Roles.
    *   Manage Tenants & Subscription Plans.
    *   Override tenant isolation for support/audit.
    *   View System-wide Analytics & Financials.

### 2. ROLE_HR (Tenant-Level Admin)
*   **Responsibility:** Managing the hiring pipeline for a specific company (Tenant).
*   **Key Capabilities:**
    *   Create/Edit/Close Jobs.
    *   Screen & Rank Candidates (via AI).
    *   Schedule Interviews & Manage Status.
    *   View Job Analytics & ROI.

### 3. ROLE_EMPLOYER (Hiring Manager)
*   **Responsibility:** Decision making within the hiring team.
*   **Key Capabilities:**
    *   Request Job Openings.
    *   Review Shortlisted Candidates.
    *   Approve Offers.
    *   Post Company Updates (Social).
    *   Boost Jobs (if permitted).

### 4. ROLE_CANDIDATE
*   **Responsibility:** Job seeking and professional networking.
*   **Key Capabilities:**
    *   Profile Management & CV Upload.
    *   Apply to Jobs & Track Status.
    *   Interact with AI Career Coach.
    *   Social Networking (Post, Like, Comment).
    *   Manage Interview Schedule.

## Monetization Model

### Subscription Tiers (SaaS)
| Plan | Target | Features |
| :--- | :--- | :--- |
| **FREE** | Startups | 1 Active Job, Basic Search, Manual Screening. |
| **BASIC** | SME | 5 Active Jobs, 500 AI Credits, Standard Analytics. |
| **PRO** | Growth | Unlimited Jobs, 5000 AI Credits, Advanced Analytics, API Access. |
| **ENTERPRISE** | Corporate | Custom contracts, Dedicated Support, White-labeling, SSO. |

### Usage-Based & Boosts
-   **Job Boosts:** Prioritized listing visibility based on bid/tier.
-   **CV Unlock:** Pay-per-view for high-value passive candidates.
-   **AI Credits:** Additional consumption-based pricing for heavy AI usage.

## Domain Model (Bounded Contexts)
-   **Identity Context:** Auth, Users, Roles, Security.
-   **Tenant Context:** Companies, Settings, Subscriptions.
-   **Recruitment Context:** Jobs, Applications, Interviews.
-   **Social Context:** Feeds, Posts, Connections.
-   **Billing Context:** Invoices, Payments, Credits.
-   **AI Context:** Scoring, Chat, Recommendations.
