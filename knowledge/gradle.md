# Gradle

**What it is:** The build tool for the backend — it compiles the Java code,
downloads the libraries the project depends on, runs tests, and starts the app.
It plays a similar role to `javac` plus dependency management plus task
running.

**David's level: introduced**

## What David knows

*(from the 2026-06-11 session)*

- **`gradlew.bat`** (the "Gradle wrapper") is a small script shipped with the
  project so nobody needs Gradle installed — it downloads the right version
  automatically. Run it from the `backend/` folder.
- **`gradlew.bat bootRun`** compiles the app and starts the Spring Boot
  server on port 8080.

## Next things to learn

- **`gradlew.bat test`** — compiles and runs the test suite.
- Reading `build.gradle.kts` — where dependencies are declared
  (`implementation(...)`, `testImplementation(...)`) and what each one in this
  project is for.
- What "dependencies" are at all: libraries downloaded from a central
  repository (Maven Central) instead of being checked into the project.
- The difference between `implementation`, `runtimeOnly`, and
  `testImplementation`.
- Java **toolchains** — how the build pins Java 21 regardless of what's on
  the machine.

## Notes

(none yet)
