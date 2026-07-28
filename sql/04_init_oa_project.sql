-- =============================================
-- OA 协同办公系统 - project 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_project` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_project`;

-- 项目表
CREATE TABLE IF NOT EXISTS `project` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目表';

-- 项目成员表
CREATE TABLE IF NOT EXISTS `project_member` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `project_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `role` TINYINT NOT NULL DEFAULT 2 COMMENT '角色：1-负责人 2-成员 3-观察者',
  `join_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_user` (`project_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='项目成员表';

-- 任务表
CREATE TABLE IF NOT EXISTS `project_task` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务表';

-- 任务操作日志表
CREATE TABLE IF NOT EXISTS `project_task_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL COMMENT '操作人',
  `action` VARCHAR(50) NOT NULL COMMENT '操作类型（创建/状态变更/分配/评论）',
  `content` VARCHAR(500) DEFAULT NULL COMMENT '操作内容',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务操作日志表';
