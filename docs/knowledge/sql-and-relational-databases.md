# Relational databases & SQL

**What it is:** A relational database stores data in tables with typed columns,
linked to each other by keys. SQL is the language for defining those tables and
querying them. This project uses **MySQL 8** and — deliberately — hand-written
SQL everywhere, because learning SQL is one of the project's goals.

**David's level: new**

## What David knows

(nothing recorded yet)

## Next things to learn

Roughly in dependency order:

- **Tables, rows, columns, data types** — see `V1__schema.sql` for the real
  thing (`food`, `brand`, `ingredient`...).
- **Primary keys** and **AUTO_INCREMENT** — how each row gets its identity.
- **Foreign keys** — how `food.brand_id` points at a `brand` row, and what
  `ON DELETE RESTRICT` / `CASCADE` / `SET NULL` mean.
- **SELECT basics** — choosing columns, `WHERE`, `ORDER BY`.
- **JOINs** — combining tables; the difference between `JOIN` (must match) and
  `LEFT JOIN` (keep the row even when there's no match) — both appear in
  `ProductRepository`.
- **Many-to-many relationships and junction tables** — `food_ingredient` and
  `food_pet_type`: why you need a third table to say "this food contains these
  ingredients".
- **NULL** — what "no value" means in SQL and how it differs from Java's null
  in practice (e.g. `LEFT JOIN` producing NULL columns).
- **Subqueries with EXISTS / NOT EXISTS** — used by the ingredient filter
  feature ("food must contain every wanted ingredient and none of the
  unwanted ones").
- **LIKE and case-insensitive matching.**
- Indexes — why `KEY idx_food_brand` exists.

## Notes

(none yet)
