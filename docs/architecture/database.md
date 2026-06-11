# Database architecture

MySQL 8.x installed locally (no Docker). Two databases: **`petfood`** for
development, **`petfood_test`** for the integration tests (so tests never touch
dev data). The schema is owned by **Flyway** migrations in
`backend/src/main/resources/db/migration/` — never created by hand.

- `V1__schema.sql` — all tables.
- `V2__seed_data.sql` — sample data: 12 foods plus brands, ingredients, pet
  types, and junction rows. (Integration tests assert against this exact data.)
- Rule: applied migrations are never edited; changes go in a new `V<n>__*.sql`.

## Schema

```
brand ──< food >── product_type            (>── = foreign key, ──< = one-to-many)
            │
            ├──< food_ingredient >── ingredient ──< sub_ingredient
            │       (junction: label_position, percentage)
            │
            └──< food_pet_type >── pet_type
                    (junction)
```

| Table             | Role                                                        |
| ----------------- | ----------------------------------------------------------- |
| `food`            | The central table — one row per product the app shows.      |
| `brand`           | Who makes it. `food.brand_id` is **required** (NOT NULL, ON DELETE RESTRICT: can't delete a brand that has foods). |
| `product_type`    | dry / wet / raw / freeze-dried / treat. **Optional** on food (nullable FK, ON DELETE SET NULL) — hence the LEFT JOINs in queries. |
| `pet_type`        | dog / cat. Many-to-many with food via `food_pet_type`.      |
| `ingredient`      | Master list of ingredients, with a `source` (animal / plant / synthetic). |
| `food_ingredient` | Junction table for the many-to-many food↔ingredient link. Carries extra facts about the *pair*: `label_position` (order by weight on the label) and `percentage`. |
| `sub_ingredient`  | Components of an ingredient (e.g. what "chicken meal" contains). Not yet exposed through the API. |

Conventions: InnoDB, utf8mb4, unsigned auto-increment integer primary keys,
natural-key UNIQUE constraints on lookup names, composite primary keys on the
junction tables, explicit indexes on FK columns.

## Connection

Configured in `application.yml` via env vars with local defaults: user
`petfood` / password `petfood`, database `petfood` (tests:
`application-test.yml`, database `petfood_test`). One-time setup SQL for
databases and the user is in the project README.

## Related

- Architecture: [[overview]] · [[backend]] (the only thing that talks to this database)
- Knowledge: [[sql-and-relational-databases|Relational databases & SQL]] · [[flyway-migrations|Flyway migrations]]
