# spring-boot-demo-activiti

此模组是 Java 8、Spring Boot 2.1.0 與 Activiti 7.1.0.M2 的流程引擎示例骨架。它會載入 `src/main/resources/processes/team01.bpmn`，並以 Spring Security 的記憶體內 demo 使用者橋接 Activiti 身分。

## 需求與限制

- 需要可連線的 MySQL；請先建立本機資料庫並調整 `src/main/resources/application.yml`。Activiti 會自動建立所需資料表。
- 設定中的資料庫憑證和程式中的 demo 使用者只供本機測試，禁止沿用到正式環境。
- 既有測試會啟動 Spring context、登入 demo 使用者並查詢流程定義，因此沒有 MySQL 時不會通過。
- Activiti 依賴是 milestone 舊版；此模組不是 production-ready 的流程平台。

從 repository 根目錄執行單模組驗證：

```bash
mvn -B -pl demo-activiti -am test
```

