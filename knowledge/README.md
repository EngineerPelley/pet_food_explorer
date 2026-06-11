# Knowledge

One file per subject relevant to this project. Together these files model what
David currently knows — they are read before teaching (to pitch explanations
right) and updated after each feature (see `../process.md`, step 5).

Levels used in each file:
- **solid** — David knows this; don't re-explain it.
- **introduced** — David has seen it and has working notes, but it's not yet second nature.
- **new** — not yet covered.

## Topics

| Topic                                                  | Level      | Why it matters here                                    |
| ------------------------------------------------------ | ---------- | ------------------------------------------------------ |
| [Java](java.md)                                        | solid      | The backend language                                   |
| [HTTP & REST APIs](http-and-rest.md)                   | introduced | How frontend and backend talk                          |
| [Spring Boot](spring-boot.md)                          | introduced | The backend framework                                  |
| [Relational databases & SQL](sql-and-relational-databases.md) | new | Where the data lives; this project hand-writes its SQL |
| [JDBC & JdbcClient](jdbc-and-jdbcclient.md)            | new        | How Java code runs SQL                                 |
| [Flyway migrations](flyway-migrations.md)              | new        | How the database schema is created and versioned       |
| [Gradle](gradle.md)                                    | introduced | Builds and runs the backend                            |
| [Git](git.md)                                          | introduced | Version control for the whole project                  |
| [React](react.md)                                      | new        | The frontend UI library                                |
| [TypeScript](typescript.md)                            | new        | The frontend language (typed JavaScript)               |
| [Node, npm & Vite](node-npm-vite.md)                   | new        | Frontend tooling: dependencies and the dev server      |
| [HTML, CSS & Bootstrap](html-css-bootstrap.md)         | new        | What the browser actually renders                      |
