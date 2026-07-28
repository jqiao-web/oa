-- =============================================
-- OA 协同办公系统 - approval 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_approval` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_approval`;

-- 审批模板表
CREATE TABLE IF NOT EXISTS `approval_template` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批模板表';

-- 审批单表
CREATE TABLE IF NOT EXISTS `approval_instance` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批单表';

-- 审批记录表（每个节点一条记录）
CREATE TABLE IF NOT EXISTS `approval_record` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批记录表';
