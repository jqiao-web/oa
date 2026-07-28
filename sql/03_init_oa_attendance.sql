-- =============================================
-- OA 协同办公系统 - attendance 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_attendance` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_attendance`;

-- 考勤规则表
CREATE TABLE IF NOT EXISTS `attendance_rule` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤规则表';

-- 打卡地点表
CREATE TABLE IF NOT EXISTS `attendance_location` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '地点名称（如：总部大楼）',
  `latitude` DECIMAL(10,7) NOT NULL COMMENT '纬度',
  `longitude` DECIMAL(10,7) NOT NULL COMMENT '经度',
  `radius` INT NOT NULL DEFAULT 500 COMMENT '打卡半径（米）',
  `geohash` VARCHAR(20) NOT NULL COMMENT 'GeoHash 编码',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打卡地点表';

-- 打卡记录表
CREATE TABLE IF NOT EXISTS `attendance_record` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打卡记录表';

-- 请假表
CREATE TABLE IF NOT EXISTS `attendance_leave` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='请假表';

-- =============================================
-- 初始数据
-- =============================================

-- 插入默认考勤规则（固定班制 9:00-18:00）
INSERT INTO `attendance_rule` (`id`, `name`, `type`, `work_start`, `work_end`, `late_threshold`, `early_threshold`) VALUES
(1, '默认固定班制', 1, '09:00:00', '18:00:00', 10, 10);

-- 插入默认打卡地点（北京天安门广场示例坐标）
INSERT INTO `attendance_location` (`id`, `name`, `latitude`, `longitude`, `radius`, `geohash`) VALUES
(1, '总部大楼', 39.9088230, 116.3974700, 500, 'wx4g0ec1');
