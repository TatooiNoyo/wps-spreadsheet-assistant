# wps-assistant v2 迁移指南

## 总览

v2 将项目从单模块重构为多模块，并将核心层从 Servlet 解耦。

## 模块变更

- 新增 `wps-spreadsheet-api`：声明核心输入输出与端口接口
- 新增 `wps-spreadsheet-core`：保留主要导入导出能力（源码在 `wps-spreadsheet-core/src/main/java`）
- 新增 `wps-spreadsheet-servlet`：`HttpServletResponse` 适配
- 新增 `wps-spreadsheet-spring`：`MultipartFile` 适配
- 新增 `wps-spreadsheet-demo`：示例应用（源码在 `wps-spreadsheet-demo/src/main/java`）
- 根目录不再有 `src/`；父工程仅负责聚合与依赖管理

## 接口迁移

### 输出接口：从 Servlet 响应对象迁移到 `ExcelOutput`

v1:

```java
void downloadTemplate(HttpServletResponse response);
void exportExcel(HttpServletResponse response, List<String> ids);
```

v2:

```java
void downloadTemplate(ExcelOutput output);
void exportExcel(ExcelOutput output, List<String> ids);
```

如需在 Web 层继续使用 `HttpServletResponse`，请改用：

- `io.github.tatooinoyo.wpsassistant.spreadsheet.adapter.servlet.ServletSpreadsheetSupport`
- `io.github.tatooinoyo.wpsassistant.spreadsheet.adapter.servlet.HttpServletExcelOutput`

### 导入接口：统一为 `ExcelInput`

v2 中 `IMultipartFile` 继承 `ExcelInput`，原有实现可继续使用。  
Spring 场景请使用：

- `io.github.tatooinoyo.wpsassistant.spreadsheet.spring.MultipartFileAdaptor`

### 大数据导出端口

`IService4Excel` 的默认实现不再返回空值，改为抛出 `UnsupportedOperationException`。  
如果你调用了 `exportLargeData`，必须实现：

- `listByIdCursor(Serializable lastId, int batchSize)`
- `getEntityId(T entity)`

## 兼容建议

- 新代码直接依赖 `SpreadsheetService`
- 旧 Controller 可通过 `LegacyServletExcelService` 或 `ServletSpreadsheetSupport` 平滑过渡

## 发布配置

v2 将发布插件移动到了 `release` profile 中。  
本地开发推荐默认 profile；发布时使用：

```bash
./mvnw -Prelease deploy
```

**不会发布** `wps-spreadsheet-demo`（该模块设置了 `maven.deploy.skip=true`，并在 `central-publishing-maven-plugin` 中配置 `skipPublishing=true`）。  
GitHub Actions 发布 workflow 使用 `-Prelease` profile，与父 POM 一致。
