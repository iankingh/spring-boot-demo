# Spring Boot Demo

> Chinese documentation: [README.md](./README.md)

This repository is a collection of Spring Boot examples. The root `pom.xml`
currently declares **62 top-level Maven modules**. Three of them
(`demo-admin`, `demo-dubbo`, and `demo-oauth`) are aggregators containing seven
children, for **66 leaf modules** in total. Module maturity varies and several
directories are startup skeletons only. This catalog reflects the current
POMs, source, and configuration; it does not imply that every demo has been
runtime-tested.

## Current baseline

- Java 17 (the root POM compilation target is `17`)
- Spring Boot `4.1.1`
- Maven 3.6.3+ (no Maven Wrapper is included)
- UTF-8

The historical baseline was Java 8 / Spring Boot `2.1.0.RELEASE`; the existing
migration is no longer Java 8 compatible. The full-reactor CI job uses Java 17;
the smoke matrix uses Java 17 and 25. Some modules retain
legacy dependencies and configuration; the three smoke modules do not prove that
the full reactor or external-service modules have completed migration.

## Quick start

Each example is an independent application. Build and run the module you need
instead of treating the whole repository as a service-free test suite.

```bash
# From the repository root: build and test one module and its reactor dependencies
mvn -pl demo-helloworld -am clean test

# Package one module
mvn -pl demo-helloworld -am clean package

# Run a simple module
cd demo-helloworld
mvn spring-boot:run

# Or run its packaged executable JAR
java -jar target/demo-helloworld.jar
```

Multi-service examples must be started separately:

```bash
# Spring Boot Admin: server first, then client
cd demo-admin/admin-server && mvn spring-boot:run
cd demo-admin/admin-client && mvn spring-boot:run

# OAuth: authorization server first, then resource server
cd demo-oauth/oauth-authorization-server && mvn spring-boot:run
cd demo-oauth/oauth-resource-server && mvn spring-boot:run
```

Before starting a module, read its `README.md` and
`src/main/resources/application*`. Database demos commonly require scripts
under `db/`, `sql/`, `init/`, or `src/main/resources/db/`.

## External services and configuration

Services are required per module, not globally:

- MySQL for the ORM, multi-datasource, sharding, Quartz, Flyway, RBAC,
  Activiti, and UReport2 examples
- Redis for caching, Session, distributed rate limiting, RBAC Security, and
  social login
- Kafka, RabbitMQ, ZooKeeper, and an XXL-JOB admin service
- Elasticsearch (both a legacy Spring Data example and a 7.x High Level
  Client example), MongoDB, and Neo4j
- SMTP, LDAP, Graylog and its supporting services
- ZooKeeper as the registry for the Dubbo example
- User-provided third-party credentials for social login, Qiniu upload, and
  payment examples

Configuration values are demonstration defaults; some are historical hosts or
placeholders. Override endpoints and credentials locally, and never commit real
secrets. Most web modules default to port `8080`, so change `server.port` when
running several at once. See each module's configuration and README for special
ports.

## Module catalog

The catalog follows the root `pom.xml`. Module links lead to detailed READMEs
where one exists.

### Fundamentals, web, and packaging

| Module | Current contents |
| --- | --- |
| [demo-helloworld](./demo-helloworld) | Minimal Hello World web application |
| [demo-properties](./demo-properties) | Typed configuration, profiles, and custom properties |
| [demo-actuator](./demo-actuator) | Actuator endpoints and endpoint security |
| [demo-admin](./demo-admin) | Spring Boot Admin aggregator: `admin-server` and `admin-client` |
| [demo-logback](./demo-logback) | Logback configuration and logging |
| [demo-log-aop](./demo-log-aop) | AOP-based web request logging |
| [demo-exception-handler](./demo-exception-handler) | Global JSON and page exception handling |
| [demo-async](./demo-async) | `@Async` tasks and executor configuration |
| [demo-upload](./demo-upload) | Local and Qiniu file uploads |
| [demo-websocket](./demo-websocket) | Server-status push over WebSocket |
| [demo-websocket-socketio](./demo-websocket-socketio) | netty-socketio chat room |
| [demo-https](./demo-https) | HTTPS and certificate configuration |
| [demo-war](./demo-war) | WAR packaging for an external servlet container |
| [demo-docker](./demo-docker) | Simple web application and Dockerfile |

### Templates and API documentation

| Module | Current contents |
| --- | --- |
| [demo-template-freemarker](./demo-template-freemarker) | Freemarker views |
| [demo-template-thymeleaf](./demo-template-thymeleaf) | Thymeleaf views |
| [demo-template-beetl](./demo-template-beetl) | Beetl views |
| [demo-template-enjoy](./demo-template-enjoy) | Enjoy views |
| [demo-swagger](./demo-swagger) | Springfox Swagger 2 and Swagger UI |
| [demo-swagger-beauty](./demo-swagger-beauty) | swagger-bootstrap-ui documentation |

### Data access, migrations, and generation

| Module | Current contents |
| --- | --- |
| [demo-orm-jdbctemplate](./demo-orm-jdbctemplate) | JdbcTemplate and a generic DAO |
| [demo-orm-jpa](./demo-orm-jpa) | Spring Data JPA |
| [demo-orm-mybatis](./demo-orm-mybatis) | Native MyBatis starter |
| [demo-orm-mybatis-mapper-page](./demo-orm-mybatis-mapper-page) | Generic Mapper and PageHelper |
| [demo-orm-mybatis-plus](./demo-orm-mybatis-plus) | MyBatis-Plus, BaseMapper/Service, and ActiveRecord |
| [demo-orm-beetlsql](./demo-orm-beetlsql) | BeetlSQL data access |
| [demo-multi-datasource-jpa](./demo-multi-datasource-jpa) | Two JPA data sources |
| [demo-multi-datasource-mybatis](./demo-multi-datasource-mybatis) | dynamic-datasource with MyBatis-Plus |
| [demo-dynamic-datasource](./demo-dynamic-datasource) | Runtime data-source creation and switching |
| [demo-sharding-jdbc](./demo-sharding-jdbc) | Sharding-JDBC with MyBatis-Plus |
| [demo-flyway](./demo-flyway) | Flyway database migrations |
| [demo-codegen](./demo-codegen) | MySQL metadata and Velocity code generation |

### Caching, security, identity, and rate limiting

| Module | Current contents |
| --- | --- |
| [demo-cache-redis](./demo-cache-redis) | Redis operations and Spring Cache |
| [demo-cache-ehcache](./demo-cache-ehcache) | Local Ehcache caching |
| [demo-rbac-security](./demo-rbac-security) | Spring Security, JWT, JPA, and Redis RBAC |
| [demo-rbac-shiro](./demo-rbac-shiro) | Shiro/MyBatis-Plus RBAC skeleton with basic API/configuration only |
| [demo-session](./demo-session) | Shared sessions with Spring Session and Redis |
| [demo-oauth](./demo-oauth) | OAuth2 authorization-server and resource-server aggregator |
| [demo-social](./demo-social) | JustAuth social login with Redis state |
| [demo-ldap](./demo-ldap) | Spring Data LDAP CRUD and login |
| [demo-ratelimit-guava](./demo-ratelimit-guava) | Local AOP + Guava RateLimiter |
| [demo-ratelimit-redis](./demo-ratelimit-redis) | Distributed AOP + Redis + Lua rate limiting |

### Messaging, RPC, and coordination

| Module | Current contents |
| --- | --- |
| [demo-mq-rabbitmq](./demo-mq-rabbitmq) | Direct, Fanout, Topic, and delayed queues |
| [demo-mq-kafka](./demo-mq-kafka) | Kafka producers and listeners |
| [demo-mq-rocketmq](./demo-mq-rocketmq) | Spring Boot startup skeleton; no RocketMQ integration code yet |
| [demo-zookeeper](./demo-zookeeper) | Curator and AOP distributed lock |
| [demo-dubbo](./demo-dubbo) | `dubbo-common`, `dubbo-provider`, and `dubbo-consumer` |

### Scheduling and operations

| Module | Current contents |
| --- | --- |
| [demo-task](./demo-task) | Spring scheduled tasks |
| [demo-task-quartz](./demo-task-quartz) | Quartz management, persistence, and UI |
| [demo-task-xxl-job](./demo-task-xxl-job) | XXL-JOB executor and admin calls |
| [demo-email](./demo-email) | Text, HTML, template, attachment, and inline-resource mail |
| [demo-graylog](./demo-graylog) | Logback GELF output to Graylog |

### Search and specialized databases

| Module | Current contents |
| --- | --- |
| [demo-elasticsearch](./demo-elasticsearch) | Spring Data Elasticsearch indexes, queries, and aggregations |
| [demo-elasticsearch-rest-high-level-client](./demo-elasticsearch-rest-high-level-client) | Elasticsearch 7.x REST High Level Client |
| [demo-mongodb](./demo-mongodb) | Spring Data MongoDB CRUD |
| [demo-neo4j](./demo-neo4j) | Neo4j campus relationship graph |

### Workflow, reports, networking, and payments

| Module | Current contents |
| --- | --- |
| [demo-ureport2](./demo-ureport2) | UReport2 designer and database-backed report storage |
| [demo-activiti](./demo-activiti) | Activiti 7 startup, security, and MySQL configuration skeleton |
| [demo-uflo](./demo-uflo) | Spring Boot startup skeleton; no UFLO integration code yet |
| [demo-urule](./demo-urule) | Spring Boot startup skeleton; no URule integration code yet |
| [demo-tio](./demo-tio) | Spring Boot startup skeleton; no t-io integration code yet |
| [demo-pay](./demo-pay) | IJPay, Alipay SDK, and ZXing dependencies, but only an application startup skeleton |

## Tests and limitations

### Java 17 / 25 smoke validation (2026-10-05)

The existing Java 17 / Boot 4 migration is preserved, with the CI JDK and smoke
command aligned to the current POM. Fresh tests and packages were executed using
Temurin `17.0.9`, Homebrew OpenJDK `25.0.4.1`, and Maven `3.8.4`. All three
modules below passed on both JDKs:

```bash
# JAVA_HOME points to Java 17 or 25; target remains Java 17, and mvn must be on PATH
mvn -B -V -pl demo-helloworld,demo-properties,demo-exception-handler -am clean package
```

| Module | Fresh `clean package` | Tests actually executed |
| --- | --- | --- |
| `demo-helloworld` | Passed | 1, passed |
| `demo-properties` | Passed | 1, passed |
| `demo-exception-handler` | Passed | 1, passed |

Java 8 was also tested: all three modules failed before tests because the
compiler target and Boot 4 class files require Java 17. The passing results
above do not imply Java 8 compatibility. The full-reactor CI package job still
skips tests; neither that full build nor remote GitHub Actions was run locally.
JDK 23+ no longer discovers classpath processors by default;
`demo-exception-handler` explicitly configures the existing BOM-managed Lombok
annotation processor instead of relying on implicit processing.

- Run tests per module. Several `@SpringBootTest` classes load the real
  `application.yml` and therefore require a database or middleware service.
- A root reactor build includes all external-service and skeleton modules; it
  is not a dependency-free smoke test.
- Legacy dependencies may require the configured Maven mirror or historical
  upstream artifacts.
- This documentation audit covered repository structure, POMs, configuration,
  tests, and module docs; it does not claim end-to-end validation of all 66
  leaf modules.

## More documentation

- `README.md` files inside module directories
- [License](./LICENSE)
