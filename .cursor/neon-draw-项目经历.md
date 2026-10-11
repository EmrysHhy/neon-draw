# 项目经历（Project）— Neon Draw 企业抽奖平台

> 可直接复制到简历「项目经历」模块；时间、角色请按实际情况微调。

---

## 版本一（推荐，简洁专业）

**Neon Draw 企业抽奖平台**  
**2025.03 - 2026.07**　|　**独立开发**　|　**后端开发**

**项目描述：**  
面向企业年会的在线抽奖管理系统，支持管理员创建抽奖活动、维护奖品与参与人员、现场开奖及中奖公示；提供静态 Web 管理端与开奖页面，满足企业活动全流程使用。

**技术栈：**  
Java 17、Spring Boot 4、MyBatis、MySQL、Redis、RabbitMQ、JWT、Spring Mail、Logback、Maven

**主要职责：**

1. **系统架构与模块设计**：采用 Controller → Service → DAO 分层架构，划分管理员认证、奖品/人员/分组管理、活动管理、抽奖执行等模块，设计 9 张核心业务表，实现多管理员数据隔离。
2. **抽奖核心流程**：实现活动创建、奖品绑定、人员导入、按奖项开奖等完整业务链路；通过 Redis 分布式锁防止同一奖项重复抽奖，保障并发场景下数据一致性。
3. **异步解耦与消息队列**：将抽奖落库、状态流转、缓存刷新等耗时操作接入 RabbitMQ 异步处理，配置死信队列兜底异常消息；开奖完成后通过线程池异步发送中奖通知邮件。
4. **状态机设计**：基于责任链模式实现活动/奖品/人员状态管理器，在单事务内完成「奖品已抽 → 人员中奖 → 活动完成」的状态流转，保证业务状态一致。
5. **缓存与性能优化**：使用 Redis 缓存活动详情、分组信息、中奖记录等热点数据，采用 Cache-Aside 策略并在开奖后主动刷新，降低数据库读压力。
6. **安全与鉴权**：基于 JWT + 拦截器实现管理员接口鉴权；手机号 AES 加密存储（MyBatis TypeHandler）；邮箱验证码登录，验证码写入 Redis 并设置 TTL。
7. **部署与运维**：配置 dev/prod 多环境；prod 环境使用 Logback 按级别拆分日志文件并滚动归档；编写 systemd 服务实现 jar 持久化运行与异常自动重启。

---

## 版本二（偏 STAR，突出难点）

**Neon Draw 企业抽奖平台**  
**2025.03 - 2026.07**　|　**独立开发**

**项目背景：**  
企业年会/团建活动需要一套可配置奖品、参与人员，并支持现场开奖与结果公示的抽奖系统，要求流程清晰、并发安全、可稳定部署。

**技术栈：**  
Spring Boot 4 / Java 17 / MyBatis / MySQL / Redis / RabbitMQ / JWT / Hutool / Spring Mail

**核心工作：**

- 设计并实现「活动 - 奖品 - 人员 - 中奖记录」完整领域模型，支持特/一/二/三等奖配置及每组奖品数量控制。
- **难点 1 — 并发抽奖**：接口层 Redis SETNX 加锁 + MQ 异步消费，避免重复提交与同步阻塞；消费端事务内完成校验、写库、状态更新。
- **难点 2 — 状态一致性**：封装 ActivityStatusManager，按序执行奖品状态、人员状态、活动状态三类 Operator，全部在同一事务中提交或回滚。
- **难点 3 — 缓存一致性**：开奖后同步刷新活动详情、中奖记录等多级 Redis 缓存，刷新失败时记录日志并保证主流程数据已落库。
- 实现奖品图片上传与等比例压缩、统一异常处理（CommonResult + GlobalExceptionHandler）、多环境配置与生产日志落盘。
- 项目部署至 Linux 云服务器，prod 端口 8081，配合 systemd/nohup 后台持久运行。

---

## 版本三（极简，一行式 bullet，适合版面紧张）

**Neon Draw 企业抽奖平台 | 独立开发 | 2025.03-2026.07**

Spring Boot 4 + MyBatis + Redis + RabbitMQ 构建的企业抽奖系统，覆盖活动/奖品/人员管理与现场开奖。

- 抽奖接口 Redis 分布式锁 + RabbitMQ 异步落库，死信队列处理异常消息
- 责任链状态机管理活动/奖品/人员状态，事务保证开奖数据一致
- JWT 鉴权 + 手机号 AES 加密 + 邮箱验证码登录
- Redis 缓存热点数据，开奖后 Cache-Aside 主动刷新
- 多环境部署，Logback 分级滚动日志，systemd 持久化运行

---

## 英文版（可选）

**Neon Draw — Enterprise Lottery Platform**  
*Independent Developer | Mar 2025 – Jul 2026*

Built a full-stack enterprise lottery system for company events using **Spring Boot 4, MyBatis, MySQL, Redis, and RabbitMQ**. Implemented admin auth (JWT), activity/prize/participant management, live draw execution, and public winner display.

- Decoupled draw processing with **RabbitMQ** and **Redis distributed locks** for concurrency safety
- Designed a **chain-of-responsibility status manager** for transactional state transitions
- Applied **Redis cache-aside** for hot data; async email notifications via thread pool
- Deployed on Linux with **multi-profile config**, Logback file logging, and **systemd** service
