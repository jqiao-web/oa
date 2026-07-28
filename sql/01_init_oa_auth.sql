-- =============================================
-- OA 协同办公系统 - auth 服务数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS `oa_auth` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `oa_auth`;

-- 员工表
CREATE TABLE IF NOT EXISTS `sys_user` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='员工表';

-- 部门表
CREATE TABLE IF NOT EXISTS `sys_dept` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 菜单/权限表
CREATE TABLE IF NOT EXISTS `sys_menu` (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单权限表';

-- 角色-菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `user_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 初始数据
-- =============================================

-- 插入默认管理员（密码：admin123，BCrypt 加密）
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `phone`, `status`) VALUES
(1, 'admin', '$2a$10$VQEDLpC0TX3JiJFSyCXRNOSY6pe9LOBsVNtGGONGBDNMBZ.UUOXt.', '系统管理员', '13800000000', 1);

-- 插入默认部门
INSERT INTO `sys_dept` (`id`, `parent_id`, `name`, `sort`, `status`) VALUES
(1, 0, '总公司', 0, 1),
(2, 1, '技术部', 1, 1),
(3, 1, '产品部', 2, 1),
(4, 1, '人事部', 3, 1);

-- 插入默认角色
INSERT INTO `sys_role` (`id`, `name`, `code`, `data_scope`, `sort`, `status`) VALUES
(1, '超级管理员', 'ROLE_ADMIN', 1, 0, 1),
(2, '部门经理', 'ROLE_DEPT_MANAGER', 3, 1, 1),
(3, '普通员工', 'ROLE_USER', 4, 2, 1);

-- 管理员绑定超级管理员角色
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES (1, 1);

-- 插入默认菜单
INSERT INTO `sys_menu` (`id`, `parent_id`, `name`, `type`, `path`, `component`, `permission`, `icon`, `sort`, `visible`) VALUES
-- 一级目录
(1,  0, '系统管理', 1, '/system', NULL, NULL, 'Setting', 1, 1),
(2,  0, '审批管理', 1, '/approval', NULL, NULL, 'Stamp', 2, 1),
(3,  0, '考勤管理', 1, '/attendance', NULL, NULL, 'Clock', 3, 1),
(4,  0, '项目管理', 1, '/project', NULL, NULL, 'Folder', 4, 1),
(5,  0, '文档中心', 1, '/document', NULL, NULL, 'Document', 5, 1),
(6,  0, '消息中心', 1, '/notification', NULL, NULL, 'Bell', 6, 1),
-- 系统管理子菜单
(10, 1, '用户管理', 2, '/system/user', 'system/UserManage', 'system:user:list', 'User', 1, 1),
(11, 1, '角色管理', 2, '/system/role', 'system/RoleManage', 'system:role:list', 'UserFilled', 2, 1),
(12, 1, '部门管理', 2, '/system/dept', 'system/DeptManage', 'system:dept:list', 'OfficeBuilding', 3, 1),
(13, 1, '菜单管理', 2, '/system/menu', 'system/MenuManage', 'system:menu:list', 'Menu', 4, 1),
-- 审批管理子菜单
(20, 2, '提交审批', 2, '/approval/submit', 'approval/ApprovalSubmit', 'approval:instance:submit', 'EditPen', 1, 1),
(21, 2, '我发起的', 2, '/approval/mine', 'approval/ApprovalMine', 'approval:instance:mine', 'Sent', 2, 1),
(22, 2, '待我审批', 2, '/approval/todo', 'approval/ApprovalTodo', 'approval:instance:todo', 'List', 3, 1),
(23, 2, '我已审批', 2, '/approval/done', 'approval/ApprovalDone', 'approval:instance:done', 'Finished', 4, 1),
-- 考勤管理子菜单
(30, 3, '打卡', 2, '/attendance/clock', 'attendance/AttendanceClock', 'attendance:clock', 'Location', 1, 1),
(31, 3, '打卡记录', 2, '/attendance/record', 'attendance/AttendanceRecord', 'attendance:record:list', 'Calendar', 2, 1),
(32, 3, '请假管理', 2, '/attendance/leave', 'attendance/AttendanceLeave', 'attendance:leave:list', 'Tickets', 3, 1),
(33, 3, '考勤报表', 2, '/attendance/report', 'attendance/AttendanceReport', 'attendance:report', 'DataAnalysis', 4, 1),
-- 项目管理子菜单
(40, 4, '项目列表', 2, '/project/list', 'project/ProjectList', 'project:list', 'Grid', 1, 1),
-- 文档中心子菜单
(50, 5, '文件管理', 2, '/document/list', 'document/DocumentList', 'document:list', 'FolderOpened', 1, 1),
-- 消息中心子菜单
(60, 6, '我的消息', 2, '/notification/list', 'notification/NotificationList', 'notification:list', 'ChatDotRound', 1, 1);

-- 超级管理员拥有所有菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(1, 10), (1, 11), (1, 12), (1, 13),
(1, 20), (1, 21), (1, 22), (1, 23),
(1, 30), (1, 31), (1, 32), (1, 33),
(1, 40), (1, 50), (1, 60);
