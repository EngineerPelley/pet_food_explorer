# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Working with the user

- The project owner (David) is a junior in college learning how to build a website. He knows Java as a programming language but is **not** familiar with Spring, React, or the surrounding web ecosystem (build tools, npm, REST conventions, databases-in-practice). Do not assume he knows what technical terms mean — define jargon in plain language the first time it comes up, and build explanations on his Java knowledge. The project itself doubles as a learning exercise (e.g. hand-written SQL instead of an ORM is deliberate).  His teacher, Scott is a senior architect and you can ask David to talk with Scott to help him understand specific concept if David does not undrstand after you try to explain to him.
- **Keep everything inside this project folder.** Do not write memory files, settings, or any other data outside `pet_food_explorer/` (e.g. nothing under `~/.claude`), because this project is sometimes used on other people's machines and must not touch their setup. If something is worth remembering across sessions, record it in this file instead.
- **Follow the process in `process.md`.** All work goes documents-first: feature description (`features/`) → architecture (`architecture/`) → knowledge-gap analysis (`knowledge/`) → code → knowledge capture. `knowledge/` models what David currently knows, one file per subject — read the relevant topic files before explaining things (don't re-teach what's marked solid; do teach what's marked new), and update them after each feature with what David learned.

## Overview

Monorepo with two independent halves (no shared build):

- **`backend/`** — Spring Boot 3.4 REST API (Java 21, Gradle Kotlin DSL) on port 8080, backed by a **local MySQL 8.x** instance (no Docker anywhere in this project).
- **`frontend/`** — React 18 SPA (Vite + TypeScript + Bootstrap 5) on port 5173, consuming the API at `http://localhost:8080/api` (configurable via `VITE_API_BASE_URL` in `frontend/.env`).

Naming note: the public-facing "product" is a row in the `food` table; the API exposes it as `/api/products`.

## Commands

Backend (run from `backend/`; use `gradlew.bat` on Windows, `./gradlew` elsewhere):

```
gradlew.bat bootRun        # start API on :8080 (Flyway migrates on startup)
gradlew.bat test           # run all tests
gradlew.bat test --tests "com.petfood.explorer.controller.ProductApiIntegrationTest"   # one test class
```

Frontend (run from `frontend/`):

```
npm run dev        # Vite dev server on :5173
npm run build      # tsc -b + vite build (this is the type-check)
npm run lint       # ESLint
npm run format     # Prettier (writes)
```

## Database

- Requires local MySQL with databases `petfood` (dev) and `petfood_test` (tests) and user `petfood`/`petfood` — bootstrap SQL is in the README. Connection settings are env vars with local defaults (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `TEST_DB_NAME`).
- **Flyway owns the schema.** Migrations are plain `.sql` files in `backend/src/main/resources/db/migration/` (`V1__schema.sql` schema, `V2__seed_data.sql` seed: 12 foods). Never alter applied migrations; add a new `V<n>__*.sql`.
- The test profile (`application-test.yml`) enables Flyway clean, so each test run wipes and re-migrates `petfood_test` — tests can assume exactly the seed data (e.g. assertions count 12 foods; updating `V2__seed_data.sql` may break test assertions).
- The integration test (`ProductApiIntegrationTest`) boots the full context against real local MySQL — **Testcontainers is deliberately not used** (no Docker on this machine). Tests fail without a running local MySQL.

## Backend architecture

Layered, one class per layer for the product slice: `ProductController` → `ProductService` → `ProductRepository`, with `view/` records (`ProductSummary`, `ProductDetail`) as API response shapes and `dto/` records (`BrandSummary`, `IngredientView`, `PetTypeRef`, `ProductTypeRef`) as nested/lookup pieces.

**No ORM — this is deliberate and load-bearing.** The project doubles as a SQL learning exercise. All data access goes through Spring's `JdbcClient` with hand-written SQL kept as Java text blocks inside the repository class, next to its record row mappers. Conventions enforced in `ProductRepository`:

- Explicit column lists, never `SELECT *`.
- Named parameters only (`:name`), never string concatenation of values. Dynamic SQL (e.g. the ingredient filter in `search()`) appends fixed SQL fragments with generated param *names*; values always go through params.
- Comment anything non-obvious in the SQL.
- Nullable FK columns are handled via `rs.wasNull()` / `rs.getObject(..., Integer.class)` (see `product_type` handling in `findById`).

The ingredient filter pattern: each `wanted` term becomes a correlated `EXISTS` subquery (food must match all), each `unwanted` term a `NOT EXISTS` (food matching any is excluded); terms match case-insensitively via `LIKE %term%`.

Errors: throw `NotFoundException` from the service; `GlobalExceptionHandler` maps exceptions to the consistent `ApiError` JSON shape (timestamp/status/error/message/path).

CORS for the Vite dev server is configured in `WebCorsConfig`, origin externalized as `app.cors.allowed-origin`.

## Frontend architecture

- `src/api/types.ts` mirrors the backend `view`/`dto` records; `src/api/client.ts` is a thin typed `fetch` wrapper. Repeated query params for the ingredient filter (`?wanted=a&wanted=b&unwanted=c`).
- Routing via `react-router-dom` in `App.tsx`; pages in `src/pages/`.
- **`react-bootstrap` is intentionally not used** — Bootstrap 5 CSS is imported globally in `main.tsx` and applied via plain class names. Keep dependencies minimal; versions in `package.json` are pinned exactly.
