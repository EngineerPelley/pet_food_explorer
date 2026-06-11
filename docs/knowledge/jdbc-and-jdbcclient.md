# JDBC & JdbcClient

**What it is:** JDBC is Java's standard way to talk to a database. `JdbcClient`
is Spring's modern, fluent wrapper around it: you give it SQL text, bind named
parameters, and map each result row to a Java object. This project uses it for
**all** data access — there is no ORM (no JPA/Hibernate), on purpose, so the
SQL stays visible and learnable.

**David's level: new**

## What David knows

(nothing recorded yet)

## Next things to learn

- The basic flow: `jdbc.sql(...)` → `.param(...)` → `.query(rowMapper)` →
  `.list()` / `.optional()` — read `ProductRepository.java` top to bottom.
- **Named parameters** (`:id`) and why values are *never* concatenated into the
  SQL string (SQL injection — letting user input rewrite your query).
- **Row mappers** — the lambda `(rs, rowNum) -> new ProductSummary(...)` that
  turns one `ResultSet` row into one Java record.
- `ResultSet` null handling: `rs.wasNull()` and
  `rs.getObject("col", Integer.class)` for columns that can be NULL.
- Building dynamic SQL safely — the ingredient filter appends fixed SQL
  fragments with *generated parameter names*; values still go through params.
- (Context) What an **ORM** is and why this project deliberately doesn't use
  one.

## Related

- Knowledge: [[java|Java]] (lambdas, records) · [[sql-and-relational-databases|Relational databases & SQL]] (the language being run) · [[spring-boot|Spring Boot]] (provides the configured `JdbcClient`)
- Architecture: [[backend]] (the repository layer)

## Notes

(none yet)
