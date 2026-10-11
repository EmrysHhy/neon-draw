# neon-draw

Neon Draw 是一个基于 Spring Boot 的活动抽奖管理系统。管理员可以维护活动、奖品、分组和参与人员，并在活动中执行抽奖、查看中奖记录以及发送中奖通知。

## 功能特性

- 管理员注册、密码登录和验证码登录
- 活动创建、状态管理和活动详情查询
- 奖品创建、图片上传、列表查询和删除
- 用户分组、成员维护和成员查询
- 抽奖执行、中奖记录展示及邮件通知
- JWT 登录鉴权、Redis 缓存、RabbitMQ 异步消息处理
- 内置管理端、活动端和抽奖页静态页面

## 技术栈

- Java 17
- Spring Boot 4.0.7、Spring MVC、Validation、Mail
- MyBatis Spring Boot 4.0.1
- MySQL、Redis、RabbitMQ
- Maven、Lombok、JWT、Hutool

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.9+
- MySQL 8.x
- Redis 6+
- RabbitMQ 3.x（启用 `neon-draw` virtual host）

### 初始化数据库

执行 [`src/main/resources/neon-draw.sql`](src/main/resources/neon-draw.sql)，创建 `neon_draw` 数据库及数据表。

注意：该脚本会删除并重建同名数据库，请仅在新环境执行，或先备份已有数据。

### 配置服务

项目默认使用 `dev` 配置。请在 `src/main/resources/application-dev.yaml` 或本地配置中填写数据库、Redis、RabbitMQ、邮件和图片存储参数。生产部署建议通过环境变量或外部配置注入凭据，并避免将真实密码提交到仓库。

主要配置项包括：

| 配置 | 用途 |
| --- | --- |
| `spring.datasource.*` | MySQL 连接 |
| `spring.data.redis.*` | Redis 连接 |
| `spring.rabbitmq.*` | RabbitMQ 连接 |
| `spring.mail.*` | 中奖邮件发送 |
| `pic.local-path` | 奖品图片保存目录 |
| `server.port` | HTTP 服务端口，开发配置默认为 `8081` |

### 启动项目

```bash
mvn spring-boot:run
```

或先打包再运行：

```bash
mvn clean package -DskipTests
java -jar target/neon-draw-0.0.1.jar
```

启动后访问 `http://localhost:8081/blogin.html` 登录管理端；抽奖页面为 `http://localhost:8081/draw.html`。静态页面位于 `src/main/resources/static/`。

## 主要接口

接口统一返回 `CommonResult`，需要登录的接口通过 `user_token` 请求头传递 JWT：

- `/admin`：管理员注册、登录和验证码
- `/activity`：活动创建、列表和详情
- `/prize`：奖品管理及图片上传
- `/group`：分组管理
- `/member`：参与人员管理
- `/draw-prize`：执行抽奖
- `/winning-records/show`：公开展示中奖记录

## 测试

运行测试：

```bash
mvn test -DskipTests=false
```

部分测试需要 MySQL、Redis、RabbitMQ 等外部服务，请先准备相应依赖或按需运行单元测试。

## 项目结构

```text
src/
├── main/java/com/emrys/neondraw/
│   ├── controller/   HTTP 接口及请求/响应对象
│   ├── service/      业务服务、状态机和消息处理
│   ├── dao/          MyBatis Mapper 与数据对象
│   └── common/       配置、鉴权、异常、工具和通用返回值
├── main/resources/
│   ├── mapper/       MyBatis XML
│   └── static/       管理端与活动端页面
└── test/java/        测试代码
```

## 贡献

欢迎提交 Issue 和 Pull Request。提交代码前请确保项目能够编译，并补充与改动相关的测试。

## 许可证

当前仓库未声明开源许可证。如需对外分发，请先补充合适的 LICENSE 文件。
