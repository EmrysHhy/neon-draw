# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

```bash
# Build (skip tests)
mvn clean package -DskipTests

# Run (dev profile, active by default)
mvn spring-boot:run

# Run tests (Mockito dynamic agent is pre-configured in pom.xml)
mvn test

# Run a single test class
mvn test -Dtest=UserServiceImplTest

# Run a single test method
mvn test -Dtest=UserServiceImplTest#testMethodName
```

Requires: Java 17, MySQL on 127.0.0.1:3306/lottery_system, Redis on localhost:8888.

## Architecture

Spring Boot 3.5 application using a layered architecture under `com.emrys.lotterysystem`:

```
controller/       — REST endpoints, param validation, calls ServiceConverter → service
service/          — Business interfaces + impl/, uses ServiceConverter + DAO layer
dao/              — MyBatis mappers (interfaces) + dataobject (DO classes)
common/           — Cross-cutting: config, error codes, exceptions, filters, interceptors, utils
```

**Data flow:** Controller → ControllerConverter → Service → ServiceConverter → Mapper (MyBatis XML)

Both `controller/convert/ControllerConverter` and `service/convert/ServiceConverter` are used for DTO/DO conversion at each layer boundary.

## Error Handling

Two exception hierarchies, caught by `GlobalExceptionHandler`:
- **`ServiceException`** — business logic errors (code 100-999 range), returns `CommonResult.error(code, message)`
- **`ControllerException`** — controller-level errors, same pattern

Error codes are defined as `ErrorCode` constants in `ServiceErrorCodeConstants` / `ControllerErrorCodeConstants` / `GlobalErrorCodeConstants`.

All API endpoints return `CommonResult<T>` (code + message + data).

## Key Patterns

- **MyBatis XML mappers** in `src/main/resources/mapper/*.xml`, mapper interfaces in `dao/mapper/`. Underscore-to-camelCase auto-mapping is enabled.
- **Profiles:** `application.yaml` (shared) → `application-dev.yaml` (local MySQL/Redis, file upload path, SQL logging). Active profile: `dev`.
- **Redis** used for activity detail caching (`activity_detail_{id}` keys, 3-day TTL).
- **Security:** AES for field encryption (custom `EncryptTypeHandle`), JWT for auth tokens, MD5 for passwords. Login interceptor registered via `LoginInterceptorConfig`.
- **Lombok** everywhere — `@Data`, `@Slf4j`, `@Builder`. Don't manually write getters/setters/loggers.
- **Hutool** (`cn.hutool:hutool-all`) available as a general utility library.

## Testing

Tests use JUnit 5 + Mockito (with `mockito-inline` via `-XX:+EnableDynamicAgentLoading`). `@SpringBootTest` for integration tests that need the full context.

## Conventions

- Chinese comments throughout the codebase; follow this style.
- DO classes suffixed with `DO` (e.g., `UserDO`, `ActivityDo`).
- Controller params suffixed with `Param`, results suffixed with `Result`.
- Service DTOs suffixed with `DTO` (e.g., `ActivityDetailDTO`).
- Static frontend pages (HTML + jQuery + AJAX) in `src/main/resources/static/`.
