# Pet Food Explorer

A small web app for browsing and exploring pet food products. It's a monorepo with
two halves:

- **`backend/`** — a Spring Boot REST API (Java 21, Gradle) that talks to a local
  MySQL database using hand-written SQL via `JdbcClient` (no JPA/Hibernate).
- **`frontend/`** — a React single-page app (Vite + TypeScript + Bootstrap 5) that
  consumes the API.

The current scaffold is a working end-to-end slice: a product list and a product
detail page backed by `GET /api/products` and `GET /api/products/{id}`.

> **Naming note:** the public-facing "product" is a row in the `food` table. The
> API exposes it under `/api/products`.

---

## Project structure

```
pet_food_explorer/
├── README.md
├── .gitignore
├── backend/
│   ├── build.gradle.kts          # Gradle (Kotlin DSL), pinned versions
│   ├── settings.gradle.kts
│   └── src/
│       ├── main/
│       │   ├── java/com/petfood/explorer/
│       │   │   ├── PetFoodExplorerApplication.java
│       │   │   ├── config/WebCorsConfig.java
│       │   │   ├── web/                 # ApiError, GlobalExceptionHandler, NotFoundException
│       │   │   └── product/            # records + controller/service/repository
│       │   └── resources/
│       │       ├── application.yml
│       │       └── db/migration/        # Flyway: V1__schema.sql, V2__seed_data.sql
│       └── test/
│           ├── java/.../ProductApiIntegrationTest.java
│           └── resources/application-test.yml
└── frontend/
    ├── package.json                 # pinned versions, npm scripts
    ├── vite.config.ts
    ├── index.html
    └── src/
        ├── main.tsx                 # imports Bootstrap CSS globally
        ├── App.tsx                  # React Router routes
        ├── api/                     # types.ts + client.ts (fetch wrapper)
        └── pages/                   # ProductListPage, ProductDetailPage
```

---

## Prerequisites

- **Java 21** (JDK). Check with `java -version`.
- **Node.js LTS** (ships with npm). Check with `node -v`.
- **Gradle** is **not** required globally — use the Gradle wrapper (`./gradlew`,
  or `gradlew.bat` on Windows) that lives in `backend/`.
- **MySQL 8.x**, installed and running **locally** (no Docker). The `mysql` client
  is handy for the setup steps below.

---

## Database setup

The app connects to a database named **`petfood`**; the integration test uses a
separate **`petfood_test`** database so tests never touch your dev data.

Run the following once as a MySQL admin user (e.g. `root`). It creates both
databases and a dedicated low-privilege application user:

```sql
-- Databases
CREATE DATABASE IF NOT EXISTS petfood
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS petfood_test
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Application user (matches the defaults in application.yml)
CREATE USER IF NOT EXISTS 'petfood'@'localhost' IDENTIFIED BY 'petfood';
GRANT ALL PRIVILEGES ON petfood.*      TO 'petfood'@'localhost';
GRANT ALL PRIVILEGES ON petfood_test.* TO 'petfood'@'localhost';
FLUSH PRIVILEGES;
```

You do **not** need to create any tables by hand — **Flyway** creates the schema
(`V1__schema.sql`) and inserts seed data (`V2__seed_data.sql`) automatically when
the backend starts (and when the test runs).

### Connection configuration

The datasource is externalized via environment variables with local defaults
(see `backend/src/main/resources/application.yml`):

| Variable        | Default     |
| --------------- | ----------- |
| `DB_HOST`       | `localhost` |
| `DB_PORT`       | `3306`      |
| `DB_NAME`       | `petfood`   |
| `DB_USER`       | `petfood`   |
| `DB_PASSWORD`   | `petfood`   |
| `TEST_DB_NAME`  | `petfood_test` (test profile only) |

Override any of them in your shell if your local setup differs.

---

## Running the backend (port 8080)

From `backend/`:

```bash
# Windows
gradlew.bat bootRun

# macOS / Linux
./gradlew bootRun
```

On startup Flyway applies the migrations and the API is available at
`http://localhost:8080/api`.

Quick check:

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
```

### Running the backend tests

```bash
# Windows
gradlew.bat test

# macOS / Linux
./gradlew test
```

---

## Running the frontend (port 5173)

From `frontend/`:

```bash
npm install      # first time only
npm run dev      # starts Vite dev server on http://localhost:5173
```

The dev server expects the backend at `http://localhost:8080/api`. Change it via
`VITE_API_BASE_URL` in `frontend/.env` if needed.

Other scripts:

```bash
npm run build    # type-check + production build
npm run lint     # ESLint
npm run format   # Prettier
```

---

## Design choices & tradeoffs

- **No ORM, hand-written SQL.** All database access uses Spring's `JdbcClient`
  with explicit, named-parameter SQL and record row mappers. This is deliberate —
  the project doubles as a way to learn SQL.
- **Where the SQL lives.** Queries are kept as formatted Java text blocks inside
  the repository classes (e.g. `ProductRepository`), close to their mappers, with
  comments on anything non-obvious. (The alternative — `.sql` resource files — was
  not used, to keep each query next to the code that runs it.)
- **Migrations are plain `.sql`.** Flyway runs `V1__schema.sql` (schema, derived
  directly from the provided data model) and `V2__seed_data.sql` (12 sample foods
  plus brands, ingredients, pet types, and the junction rows). Edit the seed file
  to add your own data.
- **Frontend styling.** Bootstrap 5 is imported globally from the `bootstrap` npm
  package and used via plain CSS classes — **`react-bootstrap` is intentionally
  not used**, to keep dependencies minimal and the markup explicit.
- **No authentication.** All endpoints are public/anonymous; Spring Security is not
  included.
- **Test database = local MySQL, not Testcontainers.** The integration test runs
  against the real local MySQL (`petfood_test`) rather than a Testcontainers MySQL
  instance. **Tradeoff:** Testcontainers would make the test fully self-contained
  but requires Docker, which isn't installed here and which the project otherwise
  avoids. Running against local MySQL exercises the real MySQL dialect (good for a
  SQL-focused project) at the cost of requiring the `petfood_test` database to
  exist. If you install Docker later, switching to Testcontainers is straightforward.

---

## API reference

| Method | Path                 | Description                                   |
| ------ | -------------------- | --------------------------------------------- |
| GET    | `/api/products`      | List all products (food + brand + type names) |
| GET    | `/api/products/{id}` | One product with brand, type, pet types, and label ingredients |

Errors are returned as a consistent JSON shape:

```json
{
  "timestamp": "2026-06-07T12:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "No product found with id 999999",
  "path": "/api/products/999999"
}
```
