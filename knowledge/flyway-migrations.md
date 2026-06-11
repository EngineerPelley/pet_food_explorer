# Flyway migrations

**What it is:** Flyway is a tool that builds and evolves the database schema
from numbered SQL files ("migrations"). When the backend starts, Flyway checks
which migrations the database has already run and applies any new ones, in
order. The schema is therefore version-controlled in git like code.

**David's level: new**

## What David knows

(nothing recorded yet)

## Next things to learn

- The files: `backend/src/main/resources/db/migration/V1__schema.sql` (creates
  all tables) and `V2__seed_data.sql` (inserts sample data). Naming pattern:
  `V<number>__<description>.sql`.
- The golden rule: **never edit a migration that has already run** — add a new
  `V3__...sql` instead. (Flyway records a checksum of each applied file and
  fails if it changed.)
- How Flyway knows what it has run: the `flyway_schema_history` table it
  creates in the database.
- Why the **test** profile allows `clean` (wipe everything and re-run all
  migrations) so each test run starts from identical, known data.

## Notes

(none yet)
