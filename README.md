# Spring Boot Demo

> English documentation: [README.en.md](./README.en.md)

这是一个基于 Spring Boot 的示例集合。当前根 `pom.xml` 声明 **62 个顶层 Maven
模块**；其中 `demo-admin`、`demo-dubbo` 和 `demo-oauth` 是聚合模块，共包含 7
个子模块，因此仓库中共有 **66 个叶子模块**。模块成熟度不一，部分目录仅有启动
骨架；本目录说明来自当前 POM、源码和配置，不表示所有示例都已完成运行时验证。

## 技术基线

- Java 8（根 POM 的编译目标为 `1.8`）
- Spring Boot `2.1.0.RELEASE`
- Maven 3.5+（仓库未提供 Maven Wrapper）
- UTF-8

该技术栈较旧。优先使用 JDK 8 构建；较新的 JDK 或 Maven 可能与旧插件、旧依赖
不兼容。

## 快速开始

每个示例都是独立应用，通常只需构建和运行目标模块，不建议把整个仓库当成一个
无需外部服务的测试套件。

```bash
# 在仓库根目录构建并测试一个模块，同时构建它依赖的仓库内模块
mvn -pl demo-helloworld -am clean test

# 打包一个模块
mvn -pl demo-helloworld -am clean package

# 运行一个简单模块
cd demo-helloworld
mvn spring-boot:run

# 或运行已打包的可执行 JAR
java -jar target/demo-helloworld.jar
```

多模块示例需要分别启动其服务：

```bash
# Spring Boot Admin：先 server，后 client
cd demo-admin/admin-server && mvn spring-boot:run
cd demo-admin/admin-client && mvn spring-boot:run

# OAuth：先 authorization server，后 resource server
cd demo-oauth/oauth-authorization-server && mvn spring-boot:run
cd demo-oauth/oauth-resource-server && mvn spring-boot:run
```

运行前必须阅读目标模块的 `README.md` 和 `src/main/resources/application*`。
数据库示例通常还需要执行模块内的 `db/`、`sql/`、`init/` 或
`src/main/resources/db/` 脚本。

## 外部服务与配置

不同模块按需使用以下服务，并非运行任一示例都要安装全部服务：

- MySQL：ORM、多数据源、分库分表、Quartz、Flyway、RBAC、Activiti、UReport2
  等模块
- Redis：缓存、Session、分布式限流、RBAC Security、第三方登录
- Kafka、RabbitMQ、ZooKeeper、XXL-JOB 调度中心
- Elasticsearch（仓库同时包含旧版 Spring Data 示例和 7.x High Level Client
  示例）、MongoDB、Neo4j
- SMTP、LDAP、Graylog 及其依赖服务
- Dubbo 示例使用 ZooKeeper 注册中心
- 社交登录、七牛云上传和支付示例需要自行申请第三方凭据

仓库中的配置值仅是演示默认值，有些包含历史环境地址或占位信息。启动前请在本地
覆盖连接地址和凭据，不要把真实密钥提交到仓库。多数 Web 示例默认使用 `8080`，
同时启动多个模块时需要修改 `server.port`。特殊端口请以模块配置和 README 为准。

## 模块目录

目录以根 `pom.xml` 为准。链接优先指向模块目录，其中已有 README 的模块可继续查看
详细配置和示例代码。

### 基础、Web 与运行方式

| 模块 | 当前内容 |
| --- | --- |
| [demo-helloworld](./demo-helloworld) | 最小 Web/Hello World 示例 |
| [demo-properties](./demo-properties) | 类型安全配置、环境 profile 与自定义属性 |
| [demo-actuator](./demo-actuator) | Actuator 端点及端点安全配置 |
| [demo-admin](./demo-admin) | Spring Boot Admin 聚合模块：`admin-server`、`admin-client` |
| [demo-logback](./demo-logback) | Logback 配置与日志输出 |
| [demo-log-aop](./demo-log-aop) | 使用 AOP 记录 Web 请求日志 |
| [demo-exception-handler](./demo-exception-handler) | JSON 与页面形式的全局异常处理 |
| [demo-async](./demo-async) | `@Async` 异步任务及线程池配置 |
| [demo-upload](./demo-upload) | 本地文件上传与七牛云上传 |
| [demo-websocket](./demo-websocket) | WebSocket 推送服务器状态 |
| [demo-websocket-socketio](./demo-websocket-socketio) | 基于 netty-socketio 的聊天室 |
| [demo-https](./demo-https) | HTTPS/证书配置示例 |
| [demo-war](./demo-war) | 外部 Servlet 容器使用的 WAR 打包 |
| [demo-docker](./demo-docker) | 简单 Web 应用及 Dockerfile |

### 模板与 API 文档

| 模块 | 当前内容 |
| --- | --- |
| [demo-template-freemarker](./demo-template-freemarker) | Freemarker 页面渲染 |
| [demo-template-thymeleaf](./demo-template-thymeleaf) | Thymeleaf 页面渲染 |
| [demo-template-beetl](./demo-template-beetl) | Beetl 页面渲染 |
| [demo-template-enjoy](./demo-template-enjoy) | Enjoy 页面渲染 |
| [demo-swagger](./demo-swagger) | Springfox Swagger 2 与 Swagger UI |
| [demo-swagger-beauty](./demo-swagger-beauty) | swagger-bootstrap-ui 文档界面 |

### 数据访问、迁移与代码生成

| 模块 | 当前内容 |
| --- | --- |
| [demo-orm-jdbctemplate](./demo-orm-jdbctemplate) | JdbcTemplate 与通用 DAO |
| [demo-orm-jpa](./demo-orm-jpa) | Spring Data JPA |
| [demo-orm-mybatis](./demo-orm-mybatis) | 原生 MyBatis starter |
| [demo-orm-mybatis-mapper-page](./demo-orm-mybatis-mapper-page) | 通用 Mapper 与 PageHelper |
| [demo-orm-mybatis-plus](./demo-orm-mybatis-plus) | MyBatis-Plus、BaseMapper/Service 与 ActiveRecord |
| [demo-orm-beetlsql](./demo-orm-beetlsql) | BeetlSQL 数据访问 |
| [demo-multi-datasource-jpa](./demo-multi-datasource-jpa) | JPA 双数据源 |
| [demo-multi-datasource-mybatis](./demo-multi-datasource-mybatis) | dynamic-datasource + MyBatis-Plus |
| [demo-dynamic-datasource](./demo-dynamic-datasource) | 运行时添加和切换数据源 |
| [demo-sharding-jdbc](./demo-sharding-jdbc) | Sharding-JDBC 分库分表与 MyBatis-Plus |
| [demo-flyway](./demo-flyway) | Flyway 数据库迁移脚本 |
| [demo-codegen](./demo-codegen) | MySQL 元数据与 Velocity 代码生成 |

### 缓存、安全、身份与限流

| 模块 | 当前内容 |
| --- | --- |
| [demo-cache-redis](./demo-cache-redis) | Redis 操作与 Spring Cache |
| [demo-cache-ehcache](./demo-cache-ehcache) | Ehcache 本地缓存 |
| [demo-rbac-security](./demo-rbac-security) | Spring Security、JWT、JPA、Redis 的 RBAC 示例 |
| [demo-rbac-shiro](./demo-rbac-shiro) | Shiro/MyBatis-Plus RBAC 骨架；当前仅有基础接口和配置 |
| [demo-session](./demo-session) | Spring Session + Redis 共享会话 |
| [demo-oauth](./demo-oauth) | OAuth2 聚合模块：授权服务器与资源服务器 |
| [demo-social](./demo-social) | JustAuth 第三方登录与 Redis 状态存储 |
| [demo-ldap](./demo-ldap) | Spring Data LDAP CRUD 与登录示例 |
| [demo-ratelimit-guava](./demo-ratelimit-guava) | AOP + Guava RateLimiter 单机限流 |
| [demo-ratelimit-redis](./demo-ratelimit-redis) | AOP + Redis + Lua 分布式限流 |

### 消息、RPC 与分布式协调

| 模块 | 当前内容 |
| --- | --- |
| [demo-mq-rabbitmq](./demo-mq-rabbitmq) | Direct、Fanout、Topic 与延迟队列 |
| [demo-mq-kafka](./demo-mq-kafka) | Kafka 消息发送和监听 |
| [demo-mq-rocketmq](./demo-mq-rocketmq) | 仅有 Spring Boot 启动骨架，尚无 RocketMQ 集成代码 |
| [demo-zookeeper](./demo-zookeeper) | Curator + AOP 分布式锁 |
| [demo-dubbo](./demo-dubbo) | `dubbo-common`、`dubbo-provider`、`dubbo-consumer` |

### 任务与运维

| 模块 | 当前内容 |
| --- | --- |
| [demo-task](./demo-task) | Spring 定时任务 |
| [demo-task-quartz](./demo-task-quartz) | Quartz 任务管理、持久化与页面 |
| [demo-task-xxl-job](./demo-task-xxl-job) | XXL-JOB 执行器和调度管理调用 |
| [demo-email](./demo-email) | 文本、HTML、模板、附件和内嵌资源邮件 |
| [demo-graylog](./demo-graylog) | Logback GELF 输出到 Graylog |

### 搜索与专用数据库

| 模块 | 当前内容 |
| --- | --- |
| [demo-elasticsearch](./demo-elasticsearch) | Spring Data Elasticsearch 的索引、查询与聚合 |
| [demo-elasticsearch-rest-high-level-client](./demo-elasticsearch-rest-high-level-client) | Elasticsearch 7.x REST High Level Client |
| [demo-mongodb](./demo-mongodb) | Spring Data MongoDB CRUD |
| [demo-neo4j](./demo-neo4j) | Neo4j 校园人物关系图 |

### 工作流、报表、网络与支付

| 模块 | 当前内容 |
| --- | --- |
| [demo-ureport2](./demo-ureport2) | UReport2 报表设计及数据库存储 |
| [demo-activiti](./demo-activiti) | Activiti 7 启动、安全和 MySQL 配置骨架 |
| [demo-uflo](./demo-uflo) | 仅有 Spring Boot 启动骨架，尚无 UFLO 集成代码 |
| [demo-urule](./demo-urule) | 仅有 Spring Boot 启动骨架，尚无 URule 集成代码 |
| [demo-tio](./demo-tio) | 仅有 Spring Boot 启动骨架，尚无 t-io 集成代码 |
| [demo-pay](./demo-pay) | 已声明 IJPay、支付宝 SDK 与 ZXing，但当前只有应用启动骨架 |

## 测试与已知限制

- 测试命令应针对单个模块执行；不少 `@SpringBootTest` 会读取真实
  `application.yml`，可能要求数据库或中间件已经启动。
- 根聚合构建会包含骨架模块和所有外部服务模块，不是无依赖的快速验证命令。
- 老版本依赖可能需要访问仓库中配置的 Maven 镜像或上游历史制品。
- 本目录只核对了仓库结构、POM、配置、测试和模块文档；未声明 66 个叶子模块均已
  完成端到端运行验证。

## 其他文档

- 各模块目录中的 `README.md`
- 许可证见 [LICENSE](./LICENSE)
