INSERT INTO `t_sys_permission`(`permissionId`, `parentId`, `permissionName`, `description`, `state`, `type`, `createTime`, `updateTime`, `defaultPermission`, `version`, `url`, `perms`, `icon`, `seq`) VALUES (93, 3, 'APP用户管理', '', 1, 1, '2018-10-30 17:38:34', '2019-03-21 17:14:18', 0, 0, 'appUser', NULL, 'appUser', NULL);

ALTER TABLE `t_business_info`
ADD COLUMN `type` tinyint(2) DEFAULT NULL COMMENT '1:现场取号，2:预约取号' AFTER `final_state`;
ALTER TABLE `t_business_info`
ADD COLUMN `appointment_id` int(11) DEFAULT NULL COMMENT '预约表id' AFTER `type`;

ALTER TABLE `t_business_type`
ADD COLUMN `morning_limit_number` int(2) DEFAULT '5' COMMENT '上午限制数' AFTER `type`;
ALTER TABLE `t_business_type`
ADD COLUMN `afternoon_limit_number` int(11) DEFAULT '5' COMMENT '下午限制数' AFTER `morning_limit_number`;
-- 4-04更新
ALTER TABLE `t_appointment_materials`
ADD COLUMN `materialsId` int(11) NULL COMMENT 't_materials 主键' AFTER `fileId`;
-- 4-08更新
ALTER TABLE `t_appointment_info`
DROP COLUMN `number`;


-- ----------------------------
-- Table structure for t_app_token
-- ----------------------------
DROP TABLE IF EXISTS `t_app_token`;
CREATE TABLE `t_app_token` (
  `appUserId` bigint(20) NOT NULL COMMENT 'app用户id',
  `token` varchar(255) COLLATE utf8_bin NOT NULL,
  `expireTime` datetime NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_app_user
-- ----------------------------
DROP TABLE IF EXISTS `t_app_user`;
CREATE TABLE `t_app_user` (
  `appUserId` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'app用户id',
  `loginName` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'app用户登录名',
  `password` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'app用户密码',
  `realName` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证姓名',
  `idCardNo` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号码',
  `serviceKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '中心key',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `state` int(255) NOT NULL DEFAULT '1' COMMENT '状态 1 正常 2 删除',
  PRIMARY KEY (`appUserId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_appointment_info
-- ----------------------------
DROP TABLE IF EXISTS `t_appointment_info`;
CREATE TABLE `t_appointment_info` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `appUserId` bigint(11) NOT NULL COMMENT 'app用户id',
  `idCardNo` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `number` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '号码',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '结束时间',
  `state` int(2) NOT NULL DEFAULT '1' COMMENT '预约状态:1 预约成功 2 签到成功 3 取消 4 过号',
  `service_key` varchar(35) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心key',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `business_type` int(10) DEFAULT NULL COMMENT '业务类型',
  `business_type_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务类型名称',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `morning_number` int(11) DEFAULT NULL COMMENT '上午预约累积号',
  `afternoon_number` int(11) DEFAULT NULL COMMENT '下午预约累积号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Table structure for t_appointment_materials
-- ----------------------------
DROP TABLE IF EXISTS `t_appointment_materials`;
CREATE TABLE `t_appointment_materials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_id` int(11) DEFAULT NULL,
  `materials_name` varchar(60) COLLATE utf8_bin NOT NULL COMMENT '材料名称',
  `materials_type` int(10) NOT NULL COMMENT '1:身份证、2:户口本、',
  `file_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '上传材料路径',
  `state` int(10) NOT NULL DEFAULT '1' COMMENT '状态 1:有效 0:无效 -1:禁用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `fileId` int(11) DEFAULT NULL COMMENT 't_storage_res表主键id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
