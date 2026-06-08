plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.petfood"
version = "0.0.1-SNAPSHOT"

java {
    // Spec requires Java 21. Gradle will locate an installed JDK 21 toolchain
    // (or download one if auto-provisioning is enabled).
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Web (REST controllers, JSON via Jackson)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Plain JDBC access — gives us JdbcClient. NO JPA / Hibernate / Spring Data JPA.
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    // Flyway schema migrations (plain .sql files). flyway-mysql is required for
    // MySQL support since Flyway 10.
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-mysql")

    // MySQL JDBC driver (runtime only).
    runtimeOnly("com.mysql:mysql-connector-j")

    // Tests. The integration test boots the context and hits a real endpoint
    // against the local MySQL `petfood_test` database (see README for the
    // Docker/Testcontainers tradeoff).
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
