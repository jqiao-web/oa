-- =============================================
-- OA 协同办公系统 - notification 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_notification` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_notification`;

-- 通知消息表
CREATE TABLE IF NOT EXISTS `notification` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知消息表';
