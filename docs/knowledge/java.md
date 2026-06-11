# Java

**What it is:** The programming language the backend is written in.

**David's level: solid**

David knows Java as a programming language from college coursework. Treat core
language features as known and build new explanations on top of them.

## What David knows

- Core Java: classes, interfaces, methods, generics, collections, exceptions.

## Next things to learn

Modern Java features this project uses that may be newer than coursework:

- **Records** (`public record ProductSummary(...)`) — compact immutable data
  classes; the project uses them for every API response shape.
- **Text blocks** (`"""..."""`) — multi-line strings; the project keeps its SQL
  in these.
- **`Optional<T>`** — a container that either holds a value or is empty, used
  instead of returning `null` (see `ProductRepository.findById`).
- **Lambdas as row mappers** — `(rs, rowNum) -> new ProductSummary(...)`.

## Related

- Knowledge: [[spring-boot|Spring Boot]] (the framework on top) · [[jdbc-and-jdbcclient|JDBC & JdbcClient]] (Java ↔ database) · [[typescript|TypeScript]] (the frontend's typed language — compare and contrast)
- Architecture: [[backend]]

## Notes

(none yet)
