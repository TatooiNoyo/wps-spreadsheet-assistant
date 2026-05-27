# WPS电子表格助手

## v2 介绍

`wps-assistant` v2 是一个用于业务系统中导入、导出 WPS/Excel（xlsx）数据的分层组件库。

它聚焦于单模型场景下的完整流程：

- 下载模板
- 导入解析
- 数据校验
- WPS 图片公式（`DISPIMG`）解析与持久化
- 常规导出与大数据游标导出
- 模板必填标识与下拉选项

## v2 模块结构

```
wps-assistant/                         # 父 POM（无 src）
├── wps-spreadsheet-api/src/main/java  # L1 契约与领域模型
├── wps-spreadsheet-core/src/main/java # L2/L3 核心实现
├── wps-spreadsheet-servlet/src/main/java
├── wps-spreadsheet-spring/src/main/java
└── wps-spreadsheet-demo/src/main/java # L5 示例应用
```

- `wps-spreadsheet-api`：领域模型与端口接口（无 Servlet 依赖）
- `wps-spreadsheet-core`：应用编排 + EasyExcel/WPS 基础设施实现
- `wps-spreadsheet-servlet`：Servlet 输出适配
- `wps-spreadsheet-spring`：Spring Multipart 适配
- `wps-spreadsheet-demo`：可运行的 Spring Boot 示例

## 构建

```bash
./mvnw -DskipTests compile
```

## 发布

默认构建不启用 Central 发布插件。需要发布时启用 `release` profile：

```bash
./mvnw -Prelease deploy
```

发布到 Maven Central 的模块：`api`、`core`、`servlet`、`spring` 及父 POM。  
`wps-spreadsheet-demo` 为本地示例，已通过 `maven.deploy.skip=true` 与 `central-publishing-maven-plugin` 的 `skipPublishing=true` 排除，不会上传到 Central。

## 迁移指南

v1 -> v2 的改动和替代接口请查看：

- `MIGRATION_V2.md`