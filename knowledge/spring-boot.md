# Spring Boot

**What it is:** A popular Java framework that handles the plumbing of being a
web server (accepting HTTP requests, converting JSON, connecting to the
database) so application code can focus on logic. The whole backend is a
Spring Boot app.

**David's level: introduced**

## What David knows

*(from the 2026-06-11 session)*

- Spring Boot turns a Java program into a long-running web server; starting it
  (`gradlew.bat bootRun`) makes it listen on port 8080 until stopped.
- It doesn't open a window — it just runs and waits for requests.

## Next things to learn

- **Dependency injection** — the core Spring idea: you declare what a class
  needs in its constructor (`ProductService(ProductRepository repo)`) and
  Spring creates and wires the objects for you. Key to reading every class in
  `backend/`.
- **Annotations** that mark roles: `@RestController`, `@Service`,
  `@Repository`, `@SpringBootApplication` — what each one tells Spring to do.
- **Request mapping** — how `@GetMapping("/api/products/{id}")` routes a URL
  to a Java method, and `@PathVariable` / `@RequestParam` pull values out of
  the URL.
- **Layered architecture** — why the code is split into controller → service →
  repository (see `../architecture/backend.md`).
- **Configuration** — `application.yml`, profiles (the `test` profile), and
  environment variable placeholders like `${DB_HOST:localhost}`.
- Exception handling with `@RestControllerAdvice` (see `error/`).

## Notes

(none yet)
