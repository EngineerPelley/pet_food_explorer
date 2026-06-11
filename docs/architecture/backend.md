# Backend architecture

Spring Boot 3.4, Java 21, built with Gradle (Kotlin DSL). Source root:
`backend/src/main/java/com/petfood/explorer/`.

## Layers

Every request flows through three layers, each with one job:

```
HTTP request
   │
   ▼
controller/   ProductController   — translates HTTP ↔ Java: reads path/query
   │                                parameters, returns records that Spring
   │                                serializes to JSON. No logic.
   ▼
service/      ProductService      — business logic. Decides things (e.g.
   │                                "no such id → throw NotFoundException").
   ▼
repository/   ProductRepository   — all SQL lives here. Talks to MySQL via
   │                                JdbcClient, maps rows to records.
   ▼
MySQL
```

Supporting packages:

- `view/` — top-level API response shapes (`ProductSummary` for the list,
  `ProductDetail` for one product). Java records.
- `dto/` — smaller pieces nested inside views (`BrandSummary`,
  `IngredientView`, `PetTypeRef`, `ProductTypeRef`). Java records.
- `error/` — `NotFoundException` (thrown by the service),
  `GlobalExceptionHandler` (catches exceptions app-wide and converts them to
  a consistent `ApiError` JSON body: timestamp/status/error/message/path).
- `config/` — `WebCorsConfig` (allows the frontend origin).

## Data-access conventions (load-bearing)

All in `ProductRepository`; follow them for any new query:

- SQL is written by hand in Java **text blocks**, kept next to the row mapper
  that consumes it. No ORM.
- Explicit column lists — never `SELECT *`.
- **Named parameters only** (`:id`); user input is never concatenated into
  SQL. Dynamic queries (the ingredient filter) append fixed SQL fragments
  with generated parameter *names*; the values still bind as parameters.
- Nullable columns are read with `rs.wasNull()` or
  `rs.getObject("col", Integer.class)`.
- The detail page loads related collections (pet types, ingredients) with one
  query each, not one big join.

## Endpoints

| Method | Path                 | Returns                                                                                                                                                       |
| ------ | -------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| GET    | `/api/products`      | `ProductSummary[]`; optional repeatable `wanted` / `unwanted` query params filter by ingredient (every wanted, no unwanted; case-insensitive substring match) |
| GET    | `/api/products/{id}` | `ProductDetail` (brand, type, pet types, ingredients) or 404                                                                                                  |

## Configuration

`src/main/resources/application.yml` — datasource from env vars with local
defaults (`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`), server
port 8080, Flyway enabled, CORS origin.

## Testing

`ProductApiIntegrationTest` boots the full application (`@SpringBootTest`,
random port, `test` profile) against the real local `petfood_test` database
and calls the endpoints over HTTP. The test profile lets Flyway **clean** the
schema first, so every run starts from exactly the migration + seed data.

## Related

- Architecture: [[overview]] · [[database]] · [[frontend]]
- Features served: [[product-list]] · [[product-detail]] · [[ingredient-filtering]]
- Knowledge: [[spring-boot|Spring Boot]] · [[java|Java]] · [[jdbc-and-jdbcclient|JDBC & JdbcClient]] · [[sql-and-relational-databases|SQL]] · [[flyway-migrations|Flyway]] · [[http-and-rest|HTTP & REST]] · [[gradle|Gradle]]
