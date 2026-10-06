# spring-boot-demo-cache

> 此 demo 演示了 Spring Boot Cache 注解与 Caffeine 本地缓存的集成。

Spring Boot 4 不再管理 Ehcache 2，因此本示例改用 Caffeine。`@Cacheable`、
`@CachePut` 和 `@CacheEvict` 示例仍使用 Spring Cache 抽象；Caffeine 提供进程
内缓存。要配置缓存过期策略，可在 `application.yml` 中使用：

```yaml
spring:
  cache:
    caffeine:
      spec: maximumSize=20000,expireAfterAccess=120s
```
