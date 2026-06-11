# Architecture overview

Pet Food Explorer is three programs working together. Each runs separately on
the local machine:

```
┌──────────────┐   HTTP + JSON    ┌──────────────┐    JDBC (SQL)    ┌─────────┐
│   Frontend   │ ───────────────► │   Backend    │ ───────────────► │  MySQL  │
│ React + Vite │ ◄─────────────── │ Spring Boot  │ ◄─────────────── │  8.x    │
│  port 5173   │                  │  port 8080   │                  │ port 3306│
└──────────────┘                  └──────────────┘                  └─────────┘
  what the user sees                the API: logic                    the data
  in the browser                    + data access
```

- **Frontend** (`frontend/`) — a React single-page app served by the Vite dev
  server. It contains no business logic and no database access; it only calls
  the backend's API and renders the results. Details: [[frontend]].
- **Backend** (`backend/`) — a Spring Boot REST API. It owns all logic and all
  database access, and is the only thing that talks to MySQL. Details:
  [[backend]].
- **Database** — a locally installed MySQL 8 instance (no Docker). Its schema
  is created and versioned by Flyway migration files that live in the backend.
  Details: [[database]].

## A request, end to end

What happens when the user opens the product list page:

1. The browser loads the React app from Vite (port 5173).
2. `ProductListPage` runs and calls `fetchProducts()` in
   `frontend/src/api/client.ts`, which does an HTTP
   `GET http://localhost:8080/api/products`.
3. Spring routes the request to `ProductController.list(...)`, which calls
   `ProductService`, which calls `ProductRepository`.
4. The repository runs hand-written SQL against MySQL via `JdbcClient` and
   maps the rows to `ProductSummary` records.
5. Spring converts the records to JSON and sends them back.
6. React stores the result in component state and renders the table.

## Cross-cutting decisions

- **Naming:** the public-facing "product" is a row in the `food` table; the
  API exposes it as `/api/products`.
- **No ORM** — all SQL is hand-written (learning goal). No JPA/Hibernate.
- **No authentication** — all endpoints are public; Spring Security is not
  included.
- **No Docker** — MySQL is installed directly on the machine; tests use a real
  local `petfood_test` database instead of Testcontainers.
- **CORS:** the browser would normally block a page from port 5173 calling an
  API on port 8080; `WebCorsConfig` explicitly allows the Vite origin
  (configurable via `CORS_ALLOWED_ORIGIN`).
- **Configuration via environment variables with local defaults** — the same
  build runs anywhere; see `application.yml` and `frontend/.env`.

## Related

- Parts: [[backend]] · [[frontend]] · [[database]]
- Features built on this: [[product-list]] · [[product-detail]] · [[ingredient-filtering]]
- Knowledge: [[http-and-rest|HTTP & REST APIs]]
