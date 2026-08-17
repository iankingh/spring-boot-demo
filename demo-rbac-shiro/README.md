# spring-boot-demo-rbac-shiro

此模組目前是 Java 8、Spring Boot 2.1.0、Shiro、MyBatis-Plus 與 MySQL 的 RBAC 資料層／設定骨架，不是完整的認證授權實作。

## 需求與限制

- 需要 MySQL；先匯入 `sql/shiro.sql`，再調整 `src/main/resources/application.yml` 的本機 datasource。
- 目前只有 `GET /demo/test` 基礎接口，尚未實作 Realm、Shiro filter chain、mapper、entity、service 或登入流程。
- p6spy 與 MyBatis-Plus 的 SQL 分析／效能設定偏向開發環境，不應未經檢查直接用於正式環境。
- Shiro、MyBatis-Plus 與 p6spy 皆為此 Java 8 demo 保留的舊版基線；升級前需另行評估相容性。

從 repository 根目錄執行單模組驗證：

```bash
mvn -B -pl demo-rbac-shiro -am test
```

context test 會連線 MySQL；沒有外部資料庫時不會通過。

