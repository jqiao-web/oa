-- =============================================
-- OA 协同办公系统 - document 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_document` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_document`;

-- 文件夹表
CREATE TABLE IF NOT EXISTS `doc_folder` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父文件夹 ID',
  `name` VARCHAR(100) NOT NULL COMMENT '文件夹名称',
  `create_by` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `deleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件夹表';

-- 文件表
CREATE TABLE IF NOT EXISTS `doc_file` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';
