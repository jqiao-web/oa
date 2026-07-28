# OA 协同办公系统 — 需求文档与技术文档

---

# 第一部分：需求文档

---

## 一、产品概述

### 1.1 产品定位

面向中小企业的 SaaS 化协同办公平台，帮助企业实现**审批流程数字化、考勤管理智能化、项目协作高效化、文档管理集中化**，提升团队协作效率。

### 1.2 目标用户

| 用户类型 | 描述 |
|---------|------|
| 企业管理员 | 系统配置、组织架构管理、权限分配 |
| 部门主管 | 审批管理、团队考勤查看、项目分配 |
| 普通员工 | 提交审批、打卡考勤、处理任务、文档协作 |
| HR | 考勤规则配置、考勤统计报表导出 |
| 项目经理 | 项目创建、任务分配、进度跟踪 |

### 1.3 核心价值

- **审批流程**：告别纸质审批，支持自定义流程模板，审批全程可追踪
- **考勤管理**：GPS 定位打卡 + 排班管理 + 自动统计报表
- **项目管理**：看板视图 + 甘特图，任务分配到人，进度一目了然
- **文档协作**：集中存储 + 权限控制 + 在线预览

---

## 二、功能需求详情

### 2.1 用户权限服务（auth-service）

#### 2.1.1 员工账号管理

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 员工注册 | 管理员创建员工账号，填写姓名/工号/手机号/部门/职位 | P0 |
| 员工登录 | 账号密码 + 验证码登录，返回 JWT Token | P0 |
| 密码重置 | 管理员可重置员工密码 | P1 |
| 账号状态 | 启用/禁用/离职三种状态 | P0 |
| 个人资料 | 员工修改头像、手机号、个人简介 | P1 |

#### 2.1.2 RBAC 权限模型

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 角色管理 | 创建/编辑/删除角色（如：管理员、部门经理、普通员工） | P0 |
| 权限管理 | 菜单权限 + 按钮权限 + 接口权限 | P0 |
| 角色授权 | 为角色分配权限，支持批量操作 | P0 |
| 用户绑定角色 | 一个员工可绑定多个角色 | P0 |
| 菜单管理 | 树形菜单配置，支持图标/排序/显隐 | P1 |

#### 2.1.3 部门组织架构

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 部门树 | 支持无限级部门嵌套（总公司 → 分公司 → 部门 → 小组） | P0 |
| 部门增删改 | 创建/编辑/删除部门，删除前校验是否有子部门或员工 | P0 |
| 部门主管 | 每个部门可设置一名主管 | P1 |
| 组织架构图 | 前端可视化展示组织架构树 | P1 |

#### 2.1.4 数据权限控制

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 全部数据 | 可查看全公司数据（如：超级管理员） | P0 |
| 本部门数据 | 只能查看本部门数据 | P0 |
| 本部门及下级 | 可查看本部门及所有下级部门数据 | P1 |
| 仅本人数据 | 只能查看自己创建/负责的数据 | P0 |
| 自定义范围 | 可自定义数据权限范围（进阶） | P2 |

---

### 2.2 审批服务（approval-service）

#### 2.2.1 审批模板管理

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 模板列表 | 系统预置 + 自定义模板（请假、报销、出差、采购等） | P0 |
| 表单设计 | 每个模板绑定一组表单字段（文本/数字/日期/图片/明细表） | P0 |
| 流程设计 | 为模板配置审批流程节点（审批人/条件分支） | P0 |
| 模板启停 | 启用/停用模板 | P1 |

#### 2.2.2 审批流程引擎（核心）

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 提交审批 | 员工填写表单提交，生成审批单 | P0 |
| 审批流转 | 按照流程配置自动流转到下一个审批人 | P0 |
| 审批操作 | 同意 / 拒绝 / 退回 / 转交 / 加签 | P0 |
| 多级审批 | 支持 N 级串行审批 | P0 |
| 会签 | 多人同时审批，全部同意才通过 | P1 |
| 或签 | 多人同时审批，任一同意即通过 | P1 |
| 条件分支 | 根据表单金额/类型走不同审批流程 | P2 |
| 催办 | 申请人可催办当前审批人 | P1 |
| 撤回 | 申请人在下一级审批前可撤回 | P1 |

#### 2.2.3 审批记录与统计

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 我发起的 | 查看自己提交的审批单列表及状态 | P0 |
| 我审批的 | 查看待审批 + 已审批列表 | P0 |
| 审批详情 | 查看审批表单 + 审批流程时间线 | P0 |
| 审批统计 | 按类型/部门/时间维度统计审批数据 | P2 |

---

### 2.3 考勤服务（attendance-service）

#### 2.3.1 打卡管理

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 上班打卡 | 记录打卡时间 + GPS 定位 + WiFi 信息 | P0 |
| 下班打卡 | 记录打卡时间 + GPS 定位 + WiFi 信息 | P0 |
| 打卡范围 | 管理员设置打卡地点（经纬度 + 半径 500m） | P0 |
| WiFi 打卡 | 绑定公司 WiFi MAC 地址（可选） | P2 |
| 打卡记录 | 查看个人每日打卡记录 | P0 |
| 外勤打卡 | 不在打卡范围内可提交外勤打卡（需备注） | P1 |

#### 2.3.2 考勤规则配置

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 固定班制 | 设置固定上下班时间（如 9:00-18:00） | P0 |
| 弹性班制 | 设置弹性时间（如 8:30-9:30 上班均可） | P1 |
| 排班制 | 按周/月排班，支持不同班次轮换 | P1 |
| 休息日 | 设置周末/法定节假日/调休 | P0 |
| 迟到/早退 | 自动判定迟到、早退、缺卡 | P0 |

#### 2.3.3 请假与加班

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 请假申请 | 事假/病假/年假/调休/婚假/产假等 | P0 |
| 请假审批 | 对接审批服务，自动扣减假期余额 | P0 |
| 加班申请 | 工作日加班/周末加班/节假日加班 | P1 |
| 假期余额 | 年假/调休等余额管理 | P1 |

#### 2.3.4 考勤统计报表

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 个人月报 | 出勤天数、迟到次数、请假天数 | P0 |
| 部门月报 | 部门出勤汇总统计 | P1 |
| 异常记录 | 迟到/早退/缺卡/旷工汇总 | P0 |
| 报表导出 | 导出 Excel（异步生成 + 下载通知） | P1 |

---

### 2.4 项目服务（project-service）

#### 2.4.1 项目管理

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 创建项目 | 填写项目名称/描述/开始结束日期/封面 | P0 |
| 项目成员 | 邀请成员、设置角色（负责人/成员/观察者） | P0 |
| 项目列表 | 我参与的 + 我创建的 + 全部项目 | P0 |
| 项目归档 | 项目完成后归档 | P1 |

#### 2.4.2 任务管理

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 创建任务 | 标题/描述/负责人/优先级/截止日期/标签 | P0 |
| 任务看板 | 看板视图（待办 → 进行中 → 待验收 → 已完成） | P0 |
| 列表视图 | 列表形式展示任务，支持筛选排序 | P1 |
| 子任务 | 支持任务下创建子任务 | P1 |
| 任务评论 | 任务详情页可评论、@ 成员 | P1 |
| 附件上传 | 任务关联附件文件 | P2 |

#### 2.4.3 任务流转

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 拖拽移动 | 看板中拖拽任务卡片切换状态 | P0 |
| 状态变更通知 | 任务状态变化时 MQ 通知相关人员 | P1 |
| 任务日志 | 记录任务的所有操作日志 | P1 |

#### 2.4.4 工时统计

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 工时填报 | 员工按项目/任务填报每日工时 | P1 |
| 工时统计 | 按人员/项目维度统计工时 | P2 |

---

### 2.5 文档服务（document-service）

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 文件夹管理 | 树形目录结构，支持创建/重命名/删除 | P0 |
| 文件上传 | 支持多文件上传，限制单文件大小 50MB | P0 |
| 文件下载 | 权限校验后下载文件 | P0 |
| 在线预览 | 图片/PDF/Office 文档在线预览 | P1 |
| 文件权限 | 文件夹级别权限控制（可查看/可编辑/可管理） | P0 |
| 文件搜索 | 按文件名/类型/上传者搜索 | P1 |
| 回收站 | 删除文件进入回收站，30天自动清理 | P2 |

---

### 2.6 通知服务（notification-service）

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 站内信 | 系统通知/审批通知/任务通知（RabbitMQ 异步） | P0 |
| 消息列表 | 已读/未读分类，一键全部已读 | P0 |
| 未读角标 | 导航栏未读消息数量实时更新 | P0 |
| 邮件通知 | 重要事项邮件提醒（MQ 异步发送） | P1 |

---

### 2.7 网关服务（gateway-service）

| 功能 | 描述 | 优先级 |
|------|------|:------:|
| 路由转发 | 根据路径前缀转发到对应微服务 | P0 |
| JWT 鉴权 | 全局过滤器校验 Token 合法性 | P0 |
| 白名单 | 登录/注册/验证码等接口免鉴权 | P0 |
| 接口限流 | 令牌桶算法限制接口请求频率 | P1 |
| CORS 跨域 | 统一配置跨域资源共享 | P0 |
| 请求日志 | 记录请求路径/耗时/状态码 | P1 |

---

## 三、用户故事（核心场景）

### 3.1 审批场景

```
作为一名普通员工，
我想在系统中提交一个请假申请，
以便我的主管和 HR 可以审批并记录我的请假信息。

流程：
1. 员工选择「请假」审批模板
2. 填写请假类型、起止日期、请假事由
3. 提交后，审批单流转到直属主管
4. 主管审批同意 → 流转到 HR
5. HR 审批同意 → 审批完成 → 自动扣减年假余额
6. 每个环节通过 RabbitMQ 异步发送通知
```

### 3.2 考勤场景

```
作为一名普通员工，
我想在到达公司时通过手机打卡，
以便系统自动记录我的考勤。

流程：
1. 员工打开考勤页面，系统获取 GPS 定位
2. 判断是否在打卡范围内（GeoHash 比对）
3. 在范围内 → 打卡成功，记录时间和位置
4. 不在范围内 → 提示外勤打卡（需填写备注）
5. 每日打卡数据存入 Redis，定时同步到 MySQL
6. 月底自动生成考勤统计报表
```

### 3.3 项目管理场景

```
作为一名项目经理，
我想创建一个项目并分配任务给团队成员，
以便我可以通过看板跟踪项目进度。

流程：
1. 项目经理创建项目，邀请团队成员
2. 创建任务，指定负责人、优先级、截止日期
3. 任务默认进入「待办」列
4. 成员拖拽任务到「进行中」→ 开始工作
5. 完成后拖拽到「已完成」
6. 状态变更通过 RabbitMQ 发送通知给相关人员
```

---
---

# 第二部分：技术文档

---

## 四、系统架构设计

### 4.1 微服务拆分

```
oa-cloud-project/                    # 父工程（聚合 POM）
├── oa-gateway/                      # 网关服务 (端口: 8080)
├── oa-auth/                         # 用户权限服务 (端口: 8081)
├── oa-approval/                     # 审批服务 (端口: 8082)
├── oa-attendance/                   # 考勤服务 (端口: 8083)
├── oa-project/                      # 项目服务 (端口: 8084)
├── oa-document/                     # 文档服务 (端口: 8085)
├── oa-notification/                 # 通知服务 (端口: 8086)
├── oa-common/                       # 公共模块（工具类、通用实体、异常处理）
│   ├── oa-common-core/              # 核心工具、统一响应、异常处理
│   ├── oa-common-redis/             # Redis 配置 + 缓存/分布式锁工具类
│   ├── oa-common-security/          # JWT 鉴权 + 权限注解
│   ├── oa-common-rabbitmq/          # RabbitMQ 配置 + 消息定义
│   └── oa-common-mybatis/           # MyBatis-Plus 配置 + 分页 + 数据权限
└── oa-web/                          # 前端 Vue3 项目
```

### 4.2 服务通信

| 通信方式 | 技术 | 场景 |
|---------|------|------|
| 同步调用 | OpenFeign | 服务间实时调用（如审批服务调用用户服务获取员工信息） |
| 异步消息 | RabbitMQ | 非实时场景（审批通知、任务状态变更、考勤报表导出） |

---

## 五、数据库设计

### 5.1 auth 服务数据库

```sql
-- 员工表
CREATE TABLE `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` VARCHAR(50) NOT NULL COMMENT '登录账号（工号）',
  `password` VARCHAR(200) NOT NULL COMMENT '密码（BCrypt 加密）',
  `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像 URL',
  `dept_id` BIGINT DEFAULT NULL COMMENT '部门 ID',
  `position` VARCHAR(50) DEFAULT NULL COMMENT '职位',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用 2-离职',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB COMMENT='员工表';

-- 部门表
CREATE TABLE `sys_dept` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父部门 ID（0=顶级）',
  `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
  `leader_id` BIGINT DEFAULT NULL COMMENT '部门主管 ID',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB COMMENT='部门表';

-- 角色表
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `data_scope` TINYINT NOT NULL DEFAULT 4 COMMENT '数据权限：1-全部 2-本部门及下级 3-本部门 4-仅本人',
  `sort` INT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB COMMENT='角色表';

-- 菜单/权限表
CREATE TABLE `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单 ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `type` TINYINT NOT NULL COMMENT '类型：1-目录 2-菜单 3-按钮',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '路由地址',
  `component` VARCHAR(200) DEFAULT NULL COMMENT '组件路径',
  `permission` VARCHAR(100) DEFAULT NULL COMMENT '权限标识（如：approval:audit:approve）',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `sort` INT NOT NULL DEFAULT 0,
  `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否可见',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='菜单权限表';

-- 角色-菜单关联表
CREATE TABLE `sys_role_menu` (
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB COMMENT='角色菜单关联表';

-- 用户-角色关联表
CREATE TABLE `sys_user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB COMMENT='用户角色关联表';
```

### 5.2 approval 服务数据库

```sql
-- 审批模板表
CREATE TABLE `approval_template` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '模板名称',
  `code` VARCHAR(50) NOT NULL COMMENT '模板编码（leave/reimburse/travel）',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
  `description` VARCHAR(500) DEFAULT NULL COMMENT '模板描述',
  `form_config` JSON NOT NULL COMMENT '表单字段配置（JSON 数组）',
  `flow_config` JSON NOT NULL COMMENT '审批流程配置（JSON 对象）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-停用 1-启用',
  `create_by` BIGINT NOT NULL COMMENT '创建人 ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB COMMENT='审批模板表';

-- 审批单表
CREATE TABLE `approval_instance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `template_id` BIGINT NOT NULL COMMENT '模板 ID',
  `template_code` VARCHAR(50) NOT NULL COMMENT '模板编码（冗余）',
  `title` VARCHAR(200) NOT NULL COMMENT '审批标题',
  `form_data` JSON NOT NULL COMMENT '表单数据（JSON 对象）',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审批 1-审批中 2-已通过 3-已拒绝 4-已撤回',
  `applicant_id` BIGINT NOT NULL COMMENT '申请人 ID',
  `current_node` INT NOT NULL DEFAULT 1 COMMENT '当前审批节点序号',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finish_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_applicant` (`applicant_id`),
  KEY `idx_template_status` (`template_code`, `status`)
) ENGINE=InnoDB COMMENT='审批单表';

-- 审批记录表（每个节点一条记录）
CREATE TABLE `approval_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `instance_id` BIGINT NOT NULL COMMENT '审批单 ID',
  `node_index` INT NOT NULL COMMENT '节点序号',
  `node_name` VARCHAR(50) NOT NULL COMMENT '节点名称',
  `node_type` TINYINT NOT NULL COMMENT '节点类型：1-审批 2-会签 3-或签',
  `approver_id` BIGINT NOT NULL COMMENT '审批人 ID',
  `action` TINYINT DEFAULT NULL COMMENT '操作：1-同意 2-拒绝 3-退回 4-转交',
  `comment` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
  `action_time` DATETIME DEFAULT NULL COMMENT '操作时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待处理 1-已处理',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_instance` (`instance_id`),
  KEY `idx_approver_status` (`approver_id`, `status`)
) ENGINE=InnoDB COMMENT='审批记录表';
```

### 5.3 attendance 服务数据库

```sql
-- 考勤规则表
CREATE TABLE `attendance_rule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '规则名称',
  `type` TINYINT NOT NULL COMMENT '类型：1-固定班制 2-弹性班制 3-排班制',
  `work_start` TIME DEFAULT NULL COMMENT '上班时间（固定班制）',
  `work_end` TIME DEFAULT NULL COMMENT '下班时间（固定班制）',
  `flex_start_begin` TIME DEFAULT NULL COMMENT '弹性上班开始时间',
  `flex_start_end` TIME DEFAULT NULL COMMENT '弹性上班结束时间',
  `late_threshold` INT NOT NULL DEFAULT 10 COMMENT '迟到阈值（分钟）',
  `early_threshold` INT NOT NULL DEFAULT 10 COMMENT '早退阈值（分钟）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='考勤规则表';

-- 打卡地点表
CREATE TABLE `attendance_location` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '地点名称（如：总部大楼）',
  `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
  `radius` INT NOT NULL DEFAULT 500 COMMENT '打卡半径（米）',
  `geohash` VARCHAR(20) NOT NULL COMMENT 'GeoHash 编码',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='打卡地点表';

-- 打卡记录表
CREATE TABLE `attendance_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '员工 ID',
  `type` TINYINT NOT NULL COMMENT '类型：1-上班 2-下班',
  `clock_time` DATETIME NOT NULL COMMENT '打卡时间',
  `latitude` DECIMAL(10,7) DEFAULT NULL COMMENT '打卡纬度',
  `longitude` DECIMAL(10,7) DEFAULT NULL COMMENT '打卡经度',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '打卡地址',
  `is_normal` TINYINT NOT NULL DEFAULT 1 COMMENT '是否正常：0-外勤 1-正常',
  `remark` VARCHAR(200) DEFAULT NULL COMMENT '备注（外勤说明）',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_date` (`user_id`, `clock_time`)
) ENGINE=InnoDB COMMENT='打卡记录表';

-- 请假表
CREATE TABLE `attendance_leave` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `type` TINYINT NOT NULL COMMENT '类型：1-事假 2-病假 3-年假 4-调休 5-婚假 6-产假',
  `start_time` DATETIME NOT NULL COMMENT '开始时间',
  `end_time` DATETIME NOT NULL COMMENT '结束时间',
  `duration` DECIMAL(4,1) NOT NULL COMMENT '时长（天）',
  `reason` VARCHAR(500) NOT NULL COMMENT '请假事由',
  `approval_id` BIGINT DEFAULT NULL COMMENT '关联审批单 ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待审批 1-已通过 2-已拒绝',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB COMMENT='请假表';
```

### 5.4 project 服务数据库

```sql
-- 项目表
CREATE TABLE `project` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '项目名称',
  `description` TEXT DEFAULT NULL COMMENT '项目描述',
  `cover` VARCHAR(500) DEFAULT NULL COMMENT '封面图片',
  `owner_id` BIGINT NOT NULL COMMENT '项目负责人 ID',
  `start_date` DATE DEFAULT NULL COMMENT '开始日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束日期',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-归档 1-进行中',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='项目表';

-- 项目成员表
CREATE TABLE `project_member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `project_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `role` TINYINT NOT NULL DEFAULT 2 COMMENT '角色：1-负责人 2-成员 3-观察者',
  `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_user` (`project_id`, `user_id`)
) ENGINE=InnoDB COMMENT='项目成员表';

-- 任务表
CREATE TABLE `project_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `project_id` BIGINT NOT NULL,
  `parent_id` BIGINT DEFAULT NULL COMMENT '父任务 ID（子任务用）',
  `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
  `description` TEXT DEFAULT NULL COMMENT '任务描述',
  `assignee_id` BIGINT DEFAULT NULL COMMENT '负责人 ID',
  `creator_id` BIGINT NOT NULL COMMENT '创建人 ID',
  `priority` TINYINT NOT NULL DEFAULT 2 COMMENT '优先级：0-低 1-中 2-高 3-紧急',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0-待办 1-进行中 2-待验收 3-已完成',
  `tag` VARCHAR(50) DEFAULT NULL COMMENT '标签',
  `due_date` DATE DEFAULT NULL COMMENT '截止日期',
  `sort` INT NOT NULL DEFAULT 0 COMMENT '看板内排序',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_project_status` (`project_id`, `status`),
  KEY `idx_assignee` (`assignee_id`)
) ENGINE=InnoDB COMMENT='任务表';

-- 任务操作日志表
CREATE TABLE `project_task_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL COMMENT '操作人',
  `action` VARCHAR(50) NOT NULL COMMENT '操作类型（创建/状态变更/分配/评论）',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '操作内容',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB COMMENT='任务操作日志表';
```

### 5.5 document 服务数据库

```sql
-- 文件夹表
CREATE TABLE `doc_folder` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父文件夹 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '文件夹名称',
  `create_by` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB COMMENT='文件夹表';

-- 文件表
CREATE TABLE `doc_file` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `folder_id` BIGINT NOT NULL DEFAULT 0 COMMENT '所属文件夹 ID',
  `name` VARCHAR(200) NOT NULL COMMENT '文件名',
  `size` BIGINT NOT NULL COMMENT '文件大小（字节）',
  `type` VARCHAR(20) NOT NULL COMMENT '文件类型（pdf/docx/xlsx/jpg）',
  `url` VARCHAR(500) NOT NULL COMMENT '存储路径',
  `create_by` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  `is_recycled` TINYINT NOT NULL DEFAULT 0 COMMENT '是否在回收站',
  `recycle_time` DATETIME DEFAULT NULL COMMENT '回收时间',
  PRIMARY KEY (`id`),
  KEY `idx_folder` (`folder_id`)
) ENGINE=InnoDB COMMENT='文件表';
```

### 5.6 notification 服务数据库

```sql
-- 通知消息表
CREATE TABLE `notification` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT '接收人 ID',
  `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
  `content` VARCHAR(1000) NOT NULL COMMENT '消息内容',
  `type` TINYINT NOT NULL COMMENT '类型：1-系统通知 2-审批通知 3-任务通知 4-考勤通知',
  `biz_id` BIGINT DEFAULT NULL COMMENT '关联业务 ID（审批单/任务）',
  `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user_read` (`user_id`, `is_read`)
) ENGINE=InnoDB COMMENT='通知消息表';
```

---

## 六、核心 API 接口设计

### 6.1 用户权限服务 (auth-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/auth/login` | 员工登录，返回 JWT Token |
| POST | `/auth/logout` | 注销登录 |
| GET | `/auth/user/info` | 获取当前登录用户信息 + 权限列表 |
| GET | `/auth/user/list` | 员工列表（分页 + 搜索） |
| POST | `/auth/user` | 创建员工账号 |
| PUT | `/auth/user/{id}` | 编辑员工信息 |
| DELETE | `/auth/user/{id}` | 删除员工 |
| GET | `/auth/dept/tree` | 获取部门树 |
| POST | `/auth/dept` | 创建部门 |
| PUT | `/auth/dept/{id}` | 编辑部门 |
| DELETE | `/auth/dept/{id}` | 删除部门 |
| GET | `/auth/role/list` | 角色列表 |
| POST | `/auth/role` | 创建角色 |
| PUT | `/auth/role/{id}` | 编辑角色（含权限分配） |
| GET | `/auth/menu/tree` | 获取菜单权限树 |

### 6.2 审批服务 (approval-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/approval/template/list` | 审批模板列表 |
| GET | `/approval/template/{id}` | 模板详情（含表单配置 + 流程配置） |
| POST | `/approval/template` | 创建审批模板 |
| PUT | `/approval/template/{id}` | 编辑审批模板 |
| POST | `/approval/instance` | 提交审批单 |
| GET | `/approval/instance/mine` | 我发起的审批列表 |
| GET | `/approval/instance/todo` | 待我审批的列表 |
| GET | `/approval/instance/done` | 我已审批的列表 |
| GET | `/approval/instance/{id}` | 审批单详情（含流程时间线） |
| POST | `/approval/instance/{id}/approve` | 同意审批 |
| POST | `/approval/instance/{id}/reject` | 拒绝审批 |
| POST | `/approval/instance/{id}/withdraw` | 撤回审批 |
| POST | `/approval/instance/{id}/urge` | 催办 |

### 6.3 考勤服务 (attendance-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/attendance/clock` | 打卡（上传 GPS 定位） |
| GET | `/attendance/record/today` | 今日打卡记录 |
| GET | `/attendance/record/list` | 打卡记录列表（按日期范围） |
| GET | `/attendance/rule` | 获取考勤规则 |
| POST | `/attendance/rule` | 设置考勤规则 |
| GET | `/attendance/location/list` | 打卡地点列表 |
| POST | `/attendance/location` | 设置打卡地点 |
| POST | `/attendance/leave` | 提交请假申请 |
| GET | `/attendance/leave/list` | 请假记录列表 |
| GET | `/attendance/report/personal` | 个人月度考勤报表 |
| GET | `/attendance/report/dept` | 部门月度考勤报表 |
| POST | `/attendance/report/export` | 异步导出考勤报表 |

### 6.4 项目服务 (project-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/project/list` | 项目列表 |
| POST | `/project` | 创建项目 |
| GET | `/project/{id}` | 项目详情 |
| PUT | `/project/{id}` | 编辑项目 |
| POST | `/project/{id}/member` | 邀请成员 |
| DELETE | `/project/{id}/member/{userId}` | 移除成员 |
| GET | `/project/{id}/task/board` | 任务看板数据（按状态分组） |
| POST | `/project/{id}/task` | 创建任务 |
| PUT | `/project/task/{id}` | 编辑任务 |
| PUT | `/project/task/{id}/status` | 更新任务状态（拖拽） |
| PUT | `/project/task/{id}/sort` | 看板内拖拽排序 |
| POST | `/project/task/{id}/comment` | 任务评论 |
| GET | `/project/task/{id}/logs` | 任务操作日志 |

### 6.5 文档服务 (document-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/doc/folder/tree` | 文件夹树 |
| POST | `/doc/folder` | 创建文件夹 |
| GET | `/doc/file/list` | 文件列表（按文件夹） |
| POST | `/doc/file/upload` | 上传文件 |
| GET | `/doc/file/{id}/download` | 下载文件 |
| GET | `/doc/file/{id}/preview` | 在线预览 |
| DELETE | `/doc/file/{id}` | 删除文件（进回收站） |
| GET | `/doc/recycle/list` | 回收站列表 |
| POST | `/doc/recycle/{id}/restore` | 恢复文件 |
| GET | `/doc/search` | 搜索文件 |

### 6.6 通知服务 (notification-service)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/notification/list` | 消息列表（分页） |
| GET | `/notification/unread-count` | 未读消息数量 |
| PUT | `/notification/{id}/read` | 标记已读 |
| PUT | `/notification/read-all` | 全部标记已读 |

---

## 七、Redis 缓存设计

### 7.1 缓存 Key 规范

| 模块 | Key 格式 | 缓存内容 | TTL |
|------|----------|----------|-----|
| 用户权限 | `auth:user:{userId}` | 用户信息 + 角色 + 权限 | 30 分钟 + 随机偏移 |
| 部门树 | `auth:dept:tree` | 组织架构树 | 1 小时 + 随机偏移 |
| 菜单权限 | `auth:menu:tree` | 菜单权限树 | 1 小时 + 随机偏移 |
| 审批模板 | `approval:template:{id}` | 模板详情（表单 + 流程配置） | 2 小时 + 随机偏移 |
| 未读消息数 | `notification:unread:{userId}` | 未读消息计数 | 10 分钟 |
| 考勤规则 | `attendance:rule` | 当前考勤规则 | 1 小时 |
| 打卡地点 | `attendance:location:list` | 打卡地点列表 | 2 小时 |
| 项目成员 | `project:{id}:members` | 项目成员列表 | 30 分钟 |

### 7.2 缓存防穿透/击穿/雪崩策略

| 问题 | 解决方案 | 应用场景 |
|------|---------|----------|
| 缓存穿透 | 缓存空值（2 分钟）+ 布隆过滤器 | 用户信息查询、审批模板查询 |
| 缓存击穿 | Redisson 互斥锁 + Double Check | 热点审批模板、部门树 |
| 缓存雪崩 | TTL 随机偏移 + 多级缓存（Caffeine + Redis） | 所有缓存数据 |

### 7.3 缓存一致性策略

| 场景 | 策略 |
|------|------|
| 用户信息修改 | 先更新数据库，再删除缓存（Cache Aside） |
| 部门树变更 | 先更新数据库，再删除缓存 + 发布 MQ 通知其他服务清除本地缓存 |
| 审批模板修改 | 先更新数据库，再删除缓存 |
| 未读消息数 | Redis 直接更新，定时同步到 MySQL |

---

## 八、分布式锁应用场景

| 场景 | 锁 Key | 说明 |
|------|--------|------|
| 审批操作 | `lock:approval:{instanceId}` | 防止同一审批单并发操作导致流程错乱 |
| 打卡操作 | `lock:clock:{userId}` | 防止同一员工并发重复打卡 |
| 任务状态变更 | `lock:task:status:{taskId}` | 防止拖拽时并发更新导致状态异常 |
| 任务拖拽排序 | `lock:task:sort:{projectId}` | 防止多人同时拖拽排序冲突 |
| 报表导出 | `lock:report:export:{userId}` | 防止同一用户重复触发导出 |

```java
// 审批操作加锁示例
@Service
public class ApprovalService {

    @Autowired
    private DistributedLock distributedLock;

    public void approve(Long instanceId, Long approverId, Integer action, String comment) {
        String lockKey = "lock:approval:" + instanceId;
        distributedLock.executeWithLock(lockKey, () -> {
            // 1. 查询审批单状态
            // 2. 校验当前审批人是否匹配
            // 3. 更新审批记录
            // 4. 流转到下一节点或完成
            // 5. 发送 MQ 通知
            return null;
        });
    }
}

// 打卡操作加锁示例
@Service
public class AttendanceService {

    @Autowired
    private DistributedLock distributedLock;

    public void clock(Long userId, BigDecimal lat, BigDecimal lng, Integer type) {
        String lockKey = "lock:clock:" + userId;
        distributedLock.executeWithLock(lockKey, () -> {
            // 1. 检查今日是否已打同类型卡
            // 2. 校验打卡地点（GeoHash）
            // 3. 记录打卡信息到 Redis
            // 4. 异步同步到 MySQL
            return null;
        });
    }
}
```

---

## 九、RabbitMQ 消息队列设计

### 9.1 队列与交换机规划

```
【审批通知】
交换机: approval.notification.exchange (Topic)
路由键: approval.notify.{action}  (approve / reject / submit / urge)
队列: approval.notification.queue → 监听后写入通知服务

【任务通知】
交换机: task.notification.exchange (Topic)
路由键: task.notify.{action}  (status_change / assign / comment)
队列: task.notification.queue → 监听后写入通知服务

【考勤报表导出】
交换机: attendance.export.exchange (Direct)
路由键: attendance.export
队列: attendance.export.queue → 异步生成 Excel + 上传 OSS + 通知下载

【缓存同步】
交换机: cache.sync.exchange (Fanout)
队列: 各服务各自的队列 → 清除本地 Caffeine 缓存
```

### 9.2 RabbitMQ 配置代码

```java
@Configuration
public class OaRabbitMQConfig {

    // ==================== 审批通知 ====================
    @Bean
    public TopicExchange approvalNotificationExchange() {
        return new TopicExchange("approval.notification.exchange");
    }

    @Bean
    public Queue approvalNotificationQueue() {
        return QueueBuilder.durable("approval.notification.queue").build();
    }

    @Bean
    public Binding approvalNotificationBinding() {
        return BindingBuilder.bind(approvalNotificationQueue())
                .to(approvalNotificationExchange())
                .with("approval.notify.*");
    }

    // ==================== 任务通知 ====================
    @Bean
    public TopicExchange taskNotificationExchange() {
        return new TopicExchange("task.notification.exchange");
    }

    @Bean
    public Queue taskNotificationQueue() {
        return QueueBuilder.durable("task.notification.queue").build();
    }

    @Bean
    public Binding taskNotificationBinding() {
        return BindingBuilder.bind(taskNotificationQueue())
                .to(taskNotificationExchange())
                .with("task.notify.*");
    }

    // ==================== 考勤报表导出（异步） ====================
    @Bean
    public DirectExchange attendanceExportExchange() {
        return new DirectExchange("attendance.export.exchange");
    }

    @Bean
    public Queue attendanceExportQueue() {
        return QueueBuilder.durable("attendance.export.queue").build();
    }

    @Bean
    public Binding attendanceExportBinding() {
        return BindingBuilder.bind(attendanceExportQueue())
                .to(attendanceExportExchange())
                .with("attendance.export");
    }

    // ==================== 缓存同步（Fanout 广播） ====================
    @Bean
    public FanoutExchange cacheSyncExchange() {
        return new FanoutExchange("cache.sync.exchange");
    }
}
```

### 9.3 消息生产与消费示例

```java
// 审批服务 — 生产者
@Service
public class ApprovalMessageProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendApprovalNotification(String action, Long instanceId,
                                          Long targetUserId, String title) {
        ApprovalNotifyMessage msg = new ApprovalNotifyMessage();
        msg.setAction(action);
        msg.setInstanceId(instanceId);
        msg.setTargetUserId(targetUserId);
        msg.setTitle(title);
        rabbitTemplate.convertAndSend(
            "approval.notification.exchange",
            "approval.notify." + action,
            msg
        );
    }
}

// 通知服务 — 消费者
@Component
@Slf4j
public class ApprovalNotificationConsumer {
    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "approval.notification.queue")
    public void handleApprovalNotification(ApprovalNotifyMessage msg,
                                            Message message, Channel channel) {
        try {
            // 根据 action 生成不同通知内容
            String content = switch (msg.getAction()) {
                case "submit" -> "您有一条新的审批待处理：" + msg.getTitle();
                case "approve" -> "您的审批已通过：" + msg.getTitle();
                case "reject" -> "您的审批已被拒绝：" + msg.getTitle();
                case "urge" -> "有人催办了审批：" + msg.getTitle();
                default -> "审批通知：" + msg.getTitle();
            };
            notificationService.createNotification(
                msg.getTargetUserId(), msg.getTitle(), content, 2, msg.getInstanceId()
            );
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("处理审批通知失败", e);
        }
    }
}

// 考勤服务 — 异步报表导出生产者
@Service
public class AttendanceExportProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendExportRequest(Long userId, String month, String deptId) {
        ExportRequest request = new ExportRequest(userId, month, deptId);
        rabbitTemplate.convertAndSend(
            "attendance.export.exchange", "attendance.export", request
        );
        // 记录导出任务状态到 Redis
        redisTemplate.opsForValue().set(
            "export:status:" + userId, "processing", 10, TimeUnit.MINUTES
        );
    }
}

// 考勤服务 — 异步报表导出消费者
@Component
@Slf4j
public class AttendanceExportConsumer {
    @Autowired
    private AttendanceReportService reportService;

    @RabbitListener(queues = "attendance.export.queue")
    public void handleExport(ExportRequest request, Message message, Channel channel) {
        try {
            // 1. 生成 Excel 文件
            // 2. 上传到 OSS
            // 3. 更新 Redis 导出状态为完成 + 下载链接
            // 4. 发送通知消息告诉用户下载
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("导出考勤报表失败", e);
            // 更新状态为失败
        }
    }
}
```

---

## 十、数据权限实现方案

### 10.1 基于注解 + AOP 的数据权限

```java
// 数据权限注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {
    /** 部门表别名 */
    String deptAlias() default "d";
    /** 用户表别名 */
    String userAlias() default "u";
}

// 数据权限 AOP 切面
@Aspect
@Component
public class DataScopeAspect {

    @Before("@annotation(dataScope)")
    public void doBefore(JoinPoint point, DataScope dataScope) {
        // 1. 获取当前登录用户的数据权限范围
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Integer dataScopeType = loginUser.getDataScope();

        StringBuilder sqlString = new StringBuilder();
        if (dataScopeType == 1) {
            // 全部数据权限，不拼接条件
            return;
        } else if (dataScopeType == 2) {
            // 本部门及下级部门
            sqlString.append(String.format(
                "%s.dept_id IN (SELECT id FROM sys_dept WHERE id = %d OR FIND_IN_SET(%d, ancestors))",
                dataScope.deptAlias(), loginUser.getDeptId(), loginUser.getDeptId()
            ));
        } else if (dataScopeType == 3) {
            // 仅本部门
            sqlString.append(String.format(
                "%s.dept_id = %d", dataScope.deptAlias(), loginUser.getDeptId()
            ));
        } else {
            // 仅本人
            sqlString.append(String.format(
                "%s.create_by = %d", dataScope.userAlias(), loginUser.getUserId()
            ));
        }

        // 3. 将 SQL 条件存入 ThreadLocal，在 MyBatis 拦截器中拼接
        DataScopeContextHolder.set(sqlString.toString());
    }
}

// 使用示例
@DataScope(deptAlias = "d", userAlias = "d")
public List<Dept> selectDeptList() {
    // MyBatis 拦截器自动拼接数据权限条件
    return deptMapper.selectList(null);
}
```

---

## 十一、Docker 部署方案

### 11.1 docker-compose.yml

```yaml
version: '3.8'

services:
  # ==================== 中间件 ====================
  mysql:
    image: mysql:8.0
    container_name: oa-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root123456
      TZ: Asia/Shanghai
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d  # 初始化 SQL
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

  redis:
    image: redis:7-alpine
    container_name: oa-redis
    ports:
      - "6379:6379"
    command: redis-server --requirepass redis123
    volumes:
      - redis-data:/data

  rabbitmq:
    image: rabbitmq:3-management-alpine
    container_name: oa-rabbitmq
    environment:
      RABBITMQ_DEFAULT_USER: admin
      RABBITMQ_DEFAULT_PASS: admin123
    ports:
      - "5672:5672"    # AMQP
      - "15672:15672"  # 管理面板
    volumes:
      - rabbitmq-data:/var/lib/rabbitmq

  nacos:
    image: nacos/nacos-server:v2.3.0
    container_name: oa-nacos
    environment:
      MODE: standalone
      SPRING_DATASOURCE_PLATFORM: mysql
      MYSQL_SERVICE_HOST: mysql
      MYSQL_SERVICE_DB_NAME: nacos_config
      MYSQL_SERVICE_USER: root
      MYSQL_SERVICE_PASSWORD: root123456
    ports:
      - "8848:8848"
      - "9848:9848"
    depends_on:
      - mysql

  # ==================== 微服务 ====================
  oa-gateway:
    build: ./oa-gateway
    container_name: oa-gateway
    ports:
      - "8080:8080"
    depends_on:
      - nacos
      - redis
    environment:
      SPRING_PROFILES_ACTIVE: prod

  oa-auth:
    build: ./oa-auth
    container_name: oa-auth
    depends_on:
      - nacos
      - mysql
      - redis

  oa-approval:
    build: ./oa-approval
    container_name: oa-approval
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq

  oa-attendance:
    build: ./oa-attendance
    container_name: oa-attendance
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq

  oa-project:
    build: ./oa-project
    container_name: oa-project
    depends_on:
      - nacos
      - mysql
      - redis
      - rabbitmq

  oa-document:
    build: ./oa-document
    container_name: oa-document
    depends_on:
      - nacos
      - mysql

  oa-notification:
    build: ./oa-notification
    container_name: oa-notification
    depends_on:
      - nacos
      - mysql
      - rabbitmq

  # ==================== 前端 + 反向代理 ====================
  nginx:
    image: nginx:alpine
    container_name: oa-nginx
    ports:
      - "80:80"
    volumes:
      - ./oa-web/dist:/usr/share/nginx/html   # Vue 构建产物
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
    depends_on:
      - oa-gateway

volumes:
  mysql-data:
  redis-data:
  rabbitmq-data:
```

### 11.2 Nginx 配置

```nginx
server {
    listen 80;
    server_name oa.example.com;

    # 前端静态资源
    location / {
        root /usr/share/nginx/html;
        index index.html;
        try_files $uri $uri/ /index.html;  # Vue Router history 模式
    }

    # API 转发到网关
    location /api/ {
        proxy_pass http://oa-gateway:8080/;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }

    # WebSocket
    location /ws/ {
        proxy_pass http://oa-notification:8086/ws/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host $host;
    }
}
```

### 11.3 微服务 Dockerfile 示例

```dockerfile
# 多阶段构建
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
```

---

## 十二、前端架构设计

### 12.1 项目结构

```
oa-ui/
├── public/
├── src/
│   ├── api/                 # 接口请求模块
│   │   ├── auth.ts
│   │   ├── approval.ts
│   │   ├── attendance.ts
│   │   ├── project.ts
│   │   ├── document.ts
│   │   └── notification.ts
│   ├── assets/              # 静态资源
│   ├── components/          # 公共组件
│   │   ├── Breadcrumb.vue
│   │   ├── DeptTree.vue
│   │   ├── UserSelect.vue
│   │   └── Pagination.vue
│   ├── layouts/             # 布局组件
│   │   ├── DefaultLayout.vue   # 左侧菜单 + 顶部导航
│   │   └── BlankLayout.vue     # 登录页布局
│   ├── router/              # 路由配置
│   │   ├── index.ts
│   │   └── guards.ts           # 路由守卫（权限拦截）
│   ├── stores/              # Pinia 状态管理
│   │   ├── user.ts              # 用户信息 + Token
│   │   ├── permission.ts        # 动态菜单 + 权限
│   │   └── notification.ts      # 未读消息数
│   ├── utils/               # 工具函数
│   │   ├── request.ts           # Axios 封装
│   │   ├── auth.ts              # Token 管理
│   │   └── geo.ts               # GPS 定位工具
│   ├── views/               # 页面组件
│   │   ├── login/               # 登录页
│   │   ├── dashboard/           # 工作台首页
│   │   ├── approval/            # 审批模块
│   │   ├── attendance/          # 考勤模块
│   │   ├── project/             # 项目模块
│   │   ├── document/            # 文档模块
│   │   ├── notification/        # 通知中心
│   │   └── system/              # 系统管理（用户/角色/部门/菜单）
│   ├── App.vue
│   └── main.ts
├── .env.development         # 开发环境变量
├── .env.production          # 生产环境变量
├── vite.config.ts
└── package.json
```

### 12.2 核心页面列表

| 模块 | 页面 | 说明 |
|------|------|------|
| 登录 | 登录页 | 账号密码 + 验证码 |
| 工作台 | 首页仪表盘 | 待办审批、今日考勤、我的任务、最新通知 |
| 审批 | 提交审批页 | 选择模板 → 动态表单填写 |
| 审批 | 我发起的 | 审批单列表 + 状态筛选 |
| 审批 | 待我审批 | 待办列表 + 审批操作弹窗 |
| 审批 | 审批详情 | 表单内容 + 审批流程时间线 |
| 考勤 | 打卡页 | GPS 定位 + 地图显示 + 打卡按钮 |
| 考勤 | 打卡记录 | 日历视图 + 列表视图 |
| 考勤 | 请假申请 | 请假表单 |
| 考勤 | 考勤报表 | 图表统计 + 导出按钮 |
| 项目 | 项目列表 | 卡片式项目展示 |
| 项目 | 任务看板 | 拖拽看板（待办/进行中/待验收/已完成） |
| 项目 | 任务详情 | 任务信息 + 评论 + 操作日志 |
| 文档 | 文档中心 | 左侧文件夹树 + 右侧文件列表 |
| 通知 | 消息中心 | 通知列表 + 已读/未读筛选 |
| 系统管理 | 用户管理 | 员工列表 + 角色分配 |
| 系统管理 | 角色管理 | 角色列表 + 权限树勾选 |
| 系统管理 | 部门管理 | 部门树 + 增删改 |
| 系统管理 | 菜单管理 | 菜单权限树配置 |

### 12.3 Pinia 状态管理示例

```typescript
// stores/user.ts
import { defineStore } from 'pinia'
import { login, getUserInfo, logout } from '@/api/auth'
import { setToken, getToken, removeToken } from '@/utils/auth'

interface UserState {
  token: string
  userId: number
  username: string
  realName: string
  avatar: string
  permissions: string[]
  roles: string[]
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken() || '',
    userId: 0,
    username: '',
    realName: '',
    avatar: '',
    permissions: [],
    roles: [],
  }),

  actions: {
    // 登录
    async loginAction(username: string, password: string) {
      const res = await login({ username, password })
      this.token = res.data.token
      setToken(res.data.token)
    },

    // 获取用户信息
    async getUserInfoAction() {
      const res = await getUserInfo()
      this.userId = res.data.userId
      this.username = res.data.username
      this.realName = res.data.realName
      this.avatar = res.data.avatar
      this.permissions = res.data.permissions
      this.roles = res.data.roles
    },

    // 注销
    async logoutAction() {
      await logout()
      this.token = ''
      this.permissions = []
      removeToken()
    },
  },
})
```

### 12.4 Axios 封装示例

```typescript
// utils/request.ts
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { getToken } from '@/utils/auth'
import router from '@/router'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL, // /api
  timeout: 15000,
})

// 请求拦截器 — 自动携带 Token
service.interceptors.request.use(
  (config) => {
    const token = getToken()
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截器 — 统一错误处理
service.interceptors.response.use(
  (response) => {
    const { code, message, data } = response.data
    if (code === 200) {
      return response.data
    }
    // 401: Token 过期
    if (code === 401) {
      const userStore = useUserStore()
      userStore.logoutAction()
      router.push('/login')
      ElMessage.error('登录已过期，请重新登录')
      return Promise.reject(new Error(message))
    }
    ElMessage.error(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default service
```

---

## 十三、开发计划建议

| 阶段 | 时间 | 内容 |
|------|------|------|
| 第 1 周 | 基础搭建 | 父工程 + 公共模块 + 网关 + Nacos 注册 |
| 第 2 周 | 权限服务 | 用户/角色/部门/菜单 + JWT + RBAC + 数据权限 |
| 第 3 周 | 审批服务 | 审批模板 + 流程引擎 + 审批操作 + MQ 通知 |
| 第 4 周 | 考勤服务 | 打卡 + GPS 定位 + 考勤规则 + 请假 |
| 第 5 周 | 项目 + 文档 | 项目管理 + 任务看板 + 文档服务 |
| 第 6 周 | 前端开发 | Vue3 前端所有页面 + 联调测试 |
| 第 7 周 | 部署上线 | Docker 编排 + Nginx + 部署 + 完善文档 |