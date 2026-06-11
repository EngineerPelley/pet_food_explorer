# HTTP & REST APIs

**What it is:** HTTP is the protocol browsers and servers use to talk; a REST
API is a server that exposes data over HTTP at predictable URLs, usually as
JSON. This is how the frontend gets its data from the backend.

**David's level: introduced**

## What David knows

*(from the 2026-06-11 session)*

- The backend is a program that runs continuously, **listening for requests**,
  and answers with data rather than web pages.
- **JSON** is the format the answers come in — structured text representing
  objects and lists.
- **localhost** means "this computer", and a **port** (like 8080) is the
  numbered door a server listens on — so `http://localhost:8080/api/products`
  means "the thing listening on door 8080 of my own machine, path
  `/api/products`".

## Next things to learn

- HTTP **methods** (GET, POST, PUT, DELETE) and what each is for. This project
  currently only uses GET (reading data).
- **Status codes** — 200 OK, 404 Not Found, 500 Server Error — and how the
  backend chooses which to send (see `error/GlobalExceptionHandler.java`).
- **Query parameters** — the `?wanted=chicken&unwanted=beef` part of a URL and
  how repeated parameters become a list on the server.
- **CORS** — why a browser blocks the frontend (port 5173) from calling the
  backend (port 8080) by default, and how `WebCorsConfig` allows it.
- How JSON maps to/from Java records automatically (Jackson) and to TypeScript
  types by hand.

## Related

- Knowledge: [[spring-boot|Spring Boot]] (serves HTTP on the backend) · [[react|React]] (consumes it on the frontend)
- Architecture: [[overview]] (the frontend↔backend arrow is HTTP) · [[backend]] · [[frontend]]
- Features: [[ingredient-filtering]] (query parameters in action)

## Notes

(none yet)
