/*
Navicat MySQL Data Transfer

Source Server         : 172.20.103.138
Source Server Version : 50625
Source Host           : 172.20.103.138:3306
Source Database       : rs_back_1129

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2018-12-12 18:32:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` bigint(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `c_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_certificate_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_business_certificate_rel`;
CREATE TABLE `t_business_certificate_rel` (
  `businessKey` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '业务唯一key，生成号码时候对应生成的key',
  `certificateId` int(11) NOT NULL COMMENT '证件类型id',
  PRIMARY KEY (`businessKey`,`certificateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_certificate_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_client_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_business_client_rel`;
CREATE TABLE `t_business_client_rel` (
  `business_id` int(11) NOT NULL COMMENT '业务唯一key，生成号码时候对应生成的key',
  `client_id` int(11) NOT NULL COMMENT '证件号码，客户表主键',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `education_id` int(11) DEFAULT NULL COMMENT '教育id 文化程度',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '教育id 文化程度',
  PRIMARY KEY (`business_id`,`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_client_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_details_classification
-- ----------------------------
DROP TABLE IF EXISTS `t_business_details_classification`;
CREATE TABLE `t_business_details_classification` (
  `number` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '详情分类编码（详情类型应遵循业务类型表）',
  `typeDetail` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '详细类型描述',
  `businessType` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '业务类型（参照业务类型表）',
  `describe` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '类型描述（参照业务类型表）',
  `type` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '大的类型  1000：公安 2000：社保',
  `status` int(2) DEFAULT '0' COMMENT '状态 0正常，-1为弃用',
  PRIMARY KEY (`number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_details_classification
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_info
-- ----------------------------
DROP TABLE IF EXISTS `t_business_info`;
CREATE TABLE `t_business_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(11) DEFAULT NULL COMMENT '号码',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `start_time` timestamp NULL DEFAULT NULL COMMENT '业务开始时间',
  `end_time` timestamp NULL DEFAULT NULL COMMENT '业务结束时间',
  `statu` tinyint(2) DEFAULT '0' COMMENT '业务状态:0 未处理 1 处理中 2 已处理 3  过号',
  `service_key` varchar(35) DEFAULT '' COMMENT '服务中心key',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `business_type` int(11) DEFAULT NULL COMMENT '业务类型',
  `business_type_name` varchar(255) DEFAULT '' COMMENT '业务类型名称',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(255) DEFAULT '' COMMENT '操作人姓名',
  `remarks` varchar(1000) DEFAULT '' COMMENT '描述',
  `version` int(11) DEFAULT '0' COMMENT '版本',
  `terminal_number` int(11) DEFAULT NULL COMMENT '终端号码',
  `final_statu` tinyint(2) DEFAULT '0' COMMENT '结束时的状态  0：未结束 1：已经处理  2：未处理',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_business_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_log
-- ----------------------------
DROP TABLE IF EXISTS `t_business_log`;
CREATE TABLE `t_business_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `business_id` int(255) DEFAULT NULL COMMENT '业务表id',
  `log_type` int(11) DEFAULT NULL COMMENT ' 业务日志类型 ,参照业务日志类型表',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '业务日志创建时间 ',
  `remark` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '业务日志描述',
  `photo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=378 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_log
-- ----------------------------
INSERT INTO `t_business_log` VALUES ('360', '124', '1', '2018-11-21 18:12:22', '业务处理', null);
INSERT INTO `t_business_log` VALUES ('361', '125', '1', '2018-11-21 19:38:37', '业务处理', null);
INSERT INTO `t_business_log` VALUES ('362', '125', '2', '2018-11-21 19:43:25', '识别身份证', '280');
INSERT INTO `t_business_log` VALUES ('363', '125', '2', '2018-11-21 19:43:39', '识别身份证', '281');
INSERT INTO `t_business_log` VALUES ('364', '125', '2', '2018-11-21 19:45:28', '识别身份证', '282');
INSERT INTO `t_business_log` VALUES ('365', '1', '1', '2018-11-29 10:26:17', '业务处理', null);
INSERT INTO `t_business_log` VALUES ('366', '1', '2', '2018-11-29 10:26:37', '识别身份证', '1');
INSERT INTO `t_business_log` VALUES ('367', '1', '3', '2018-11-29 10:27:09', '截图', '2');
INSERT INTO `t_business_log` VALUES ('368', '1', '3', '2018-11-29 10:27:09', '截图', '3');
INSERT INTO `t_business_log` VALUES ('369', '1', '3', '2018-11-29 10:27:11', '截图', '4');
INSERT INTO `t_business_log` VALUES ('370', '1', '3', '2018-11-29 10:27:14', '截图', '5');
INSERT INTO `t_business_log` VALUES ('371', '1', '5', '2018-11-29 10:35:20', '业务已处理', null);
INSERT INTO `t_business_log` VALUES ('372', '2', '1', '2018-11-29 10:35:20', '业务处理', null);
INSERT INTO `t_business_log` VALUES ('373', '2', '2', '2018-11-29 10:37:34', '识别身份证', '6');
INSERT INTO `t_business_log` VALUES ('374', '2', '5', '2018-11-29 10:37:50', '业务已处理', null);
INSERT INTO `t_business_log` VALUES ('375', '3', '1', '2018-11-29 10:37:50', '业务处理', null);
INSERT INTO `t_business_log` VALUES ('376', '3', '2', '2018-11-29 10:38:39', '识别身份证', '7');
INSERT INTO `t_business_log` VALUES ('377', '3', '2', '2018-11-29 10:44:15', '识别身份证', '8');

-- ----------------------------
-- Table structure for t_business_log_type
-- ----------------------------
DROP TABLE IF EXISTS `t_business_log_type`;
CREATE TABLE `t_business_log_type` (
  `businessLogType` int(11) NOT NULL AUTO_INCREMENT COMMENT ' 业务日志类型',
  `businessLogTitle` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务日志标题',
  PRIMARY KEY (`businessLogType`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_log_type
-- ----------------------------
INSERT INTO `t_business_log_type` VALUES ('1', '业务处理');
INSERT INTO `t_business_log_type` VALUES ('2', '识别身份证');
INSERT INTO `t_business_log_type` VALUES ('3', '截图');
INSERT INTO `t_business_log_type` VALUES ('4', '删除截图');
INSERT INTO `t_business_log_type` VALUES ('5', '业务已处理');
INSERT INTO `t_business_log_type` VALUES ('6', '获取签名照片');
INSERT INTO `t_business_log_type` VALUES ('7', '删除签名照片');
INSERT INTO `t_business_log_type` VALUES ('8', '扫描指纹照');
INSERT INTO `t_business_log_type` VALUES ('9', '删除指纹照');
INSERT INTO `t_business_log_type` VALUES ('10', '采集证件照');
INSERT INTO `t_business_log_type` VALUES ('11', '删除证件照');

-- ----------------------------
-- Table structure for t_business_type
-- ----------------------------
DROP TABLE IF EXISTS `t_business_type`;
CREATE TABLE `t_business_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键类型',
  `parent_id` int(11) NOT NULL DEFAULT '0' COMMENT '父类型',
  `describes` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '类型描述',
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `service_key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心key',
  `grade` int(2) NOT NULL DEFAULT '1' COMMENT '业务等级',
  `is_leaf` int(2) NOT NULL DEFAULT '0' COMMENT '是否是叶子节点  0:否 1:是',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态 -2：删除 -1禁用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_form` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要申请单 0：不需要 1：需要',
  `append_form` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要附加申请单 0：不需要 1：需要',
  `is_materials` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要申请材料 0：不需要 1：需要',
  `materials_print` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要本地打印申请材料 0：不需要 1：需要',
  `e_signature` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要电子签名 0：不需要 1：需要',
  `remote_print` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要远程打印 0：不需要 1：需要',
  `police_sign` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要公安电子签名加盖打印 0：不需要 1：需要',
  `police_notarize` int(2) NOT NULL DEFAULT '0' COMMENT '是否需要公安信息确认单 0：不需要 1：需要',
  `whole_time` int(2) NOT NULL DEFAULT '0' COMMENT '是否全时段  0：是  1：不是',
  `business_month` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务受理月份',
  `business_day` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务受理日',
  `business_flow` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务流程',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '业务类型： 0 ：普通   1：默认 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_type_certificate_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_business_type_certificate_rel`;
CREATE TABLE `t_business_type_certificate_rel` (
  `businessType` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '业务类型 (对应业务类型表)',
  `certificateId` int(11) NOT NULL COMMENT '证件类型id(对应证件类型表)',
  PRIMARY KEY (`businessType`,`certificateId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_type_certificate_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_type_forms
-- ----------------------------
DROP TABLE IF EXISTS `t_business_type_forms`;
CREATE TABLE `t_business_type_forms` (
  `businessType` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '业务类型 (对应业务类型表 t_business_type)',
  `formsId` int(11) NOT NULL COMMENT '表单模板主键（对应表单模板表t_ forms）',
  PRIMARY KEY (`businessType`,`formsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_type_forms
-- ----------------------------

-- ----------------------------
-- Table structure for t_business_unattended_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_business_unattended_rel`;
CREATE TABLE `t_business_unattended_rel` (
  `business_id` int(11) NOT NULL COMMENT 'ҵ��key',
  `cause_id` int(11) NOT NULL COMMENT 'ԭ��id',
  PRIMARY KEY (`business_id`,`cause_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_business_unattended_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_certificate_type
-- ----------------------------
DROP TABLE IF EXISTS `t_certificate_type`;
CREATE TABLE `t_certificate_type` (
  `certificateId` int(11) NOT NULL AUTO_INCREMENT COMMENT '证件类型id',
  `certificateTitle` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '证件名称',
  PRIMARY KEY (`certificateId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_certificate_type
-- ----------------------------
INSERT INTO `t_certificate_type` VALUES ('1', '居民身份证');
INSERT INTO `t_certificate_type` VALUES ('2', '居民户口簿');
INSERT INTO `t_certificate_type` VALUES ('3', '结婚证');
INSERT INTO `t_certificate_type` VALUES ('4', '离婚协议书');
INSERT INTO `t_certificate_type` VALUES ('5', '离婚证');
INSERT INTO `t_certificate_type` VALUES ('6', '离婚判决书');
INSERT INTO `t_certificate_type` VALUES ('7', '毕业证书');
INSERT INTO `t_certificate_type` VALUES ('8', '在职单位证明');
INSERT INTO `t_certificate_type` VALUES ('9', '劳动合同');
INSERT INTO `t_certificate_type` VALUES ('10', '住所证明');
INSERT INTO `t_certificate_type` VALUES ('11', '就业证明');
INSERT INTO `t_certificate_type` VALUES ('12', '就读证明');
INSERT INTO `t_certificate_type` VALUES ('13', '不动产权证书');
INSERT INTO `t_certificate_type` VALUES ('14', '房屋所有权证');
INSERT INTO `t_certificate_type` VALUES ('15', '房屋使用权证明');
INSERT INTO `t_certificate_type` VALUES ('16', '房屋租赁合同（协议）');
INSERT INTO `t_certificate_type` VALUES ('17', '委托书');
INSERT INTO `t_certificate_type` VALUES ('18', '亲属关系证明');
INSERT INTO `t_certificate_type` VALUES ('19', ' 出生医学证明');
INSERT INTO `t_certificate_type` VALUES ('20', '银行卡');
INSERT INTO `t_certificate_type` VALUES ('21', '申请表');
INSERT INTO `t_certificate_type` VALUES ('22', '暂住证');
INSERT INTO `t_certificate_type` VALUES ('23', '市民卡');
INSERT INTO `t_certificate_type` VALUES ('24', '关系证明');

-- ----------------------------
-- Table structure for t_client_info
-- ----------------------------
DROP TABLE IF EXISTS `t_client_info`;
CREATE TABLE `t_client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '申请人姓名',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别 1:男 2:女 9：其他',
  `birth_date` date DEFAULT NULL COMMENT '出生日期',
  `address` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '家庭地址',
  `id_type` int(2) DEFAULT '1' COMMENT '证件类型 1.身份证 2.户口本 3.护照 ',
  `idcard` varchar(36) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '证件号码 主键',
  `issuing_organ` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '发证机关',
  `expiration_date` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '有效期限:依次是起始日期的年，月，日，终止日期的年，月，日',
  `new_addr` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '最新家庭住址',
  `nation_name` varchar(10) COLLATE utf8_bin DEFAULT NULL COMMENT '民族名称',
  `nation` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '民族 参照名族表',
  `birthplace` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '出生地',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '联系方式',
  `extend` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '扩展',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `education_id` int(11) DEFAULT NULL COMMENT '教育id 文化程度',
  `source` int(2) DEFAULT '1' COMMENT '来源  1：本地人口  2：外来人口',
  `icon_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '证件头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_idcard` (`idcard`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_client_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_common_config
-- ----------------------------
DROP TABLE IF EXISTS `t_common_config`;
CREATE TABLE `t_common_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `commonName` varchar(500) DEFAULT '' COMMENT '通用名称',
  `commonValue` varchar(500) DEFAULT '' COMMENT '通用值',
  `state` int(2) DEFAULT '0' COMMENT '状态，默认0,',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifyTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `describes` varchar(1024) DEFAULT '' COMMENT '设置描述 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_common_config
-- ----------------------------
INSERT INTO `t_common_config` VALUES ('1', 'meeting_uuid', '4106ffa0a7d111e6802eb82a72db6d4d', '0', '0', '2018-03-24 13:10:59', '2018-11-12 15:58:53', '会管登陆后返回的用户uuid');
INSERT INTO `t_common_config` VALUES ('2', 'meeting_token', '3b066af7f37e11e8b0d6ac1f6b190e53', '0', '0', '2018-03-24 13:12:10', '2018-11-29 10:26:23', '会管登陆后返回的token');
INSERT INTO `t_common_config` VALUES ('3', 'meeting_url', 'http://1.102.143.130:8080', '0', '16', '2018-03-24 13:25:59', '2018-11-27 10:10:09', '会管服务器地址');
INSERT INTO `t_common_config` VALUES ('4', 'meeting_login_name', 'admin', '0', '16', '2018-03-24 13:27:12', '2018-11-27 10:10:10', '会管登陆用户');
INSERT INTO `t_common_config` VALUES ('5', 'meeting_login_password', 'e10adc3949ba59abbe56e057f20f883e', '0', '16', '2018-03-24 13:27:32', '2018-11-27 10:10:10', '会管登陆密码');
INSERT INTO `t_common_config` VALUES ('6', 'cms_url', 'http://172.20.102.75:8080', '0', '16', '2018-03-24 14:47:53', '2018-11-27 10:10:10', '存储网关地址');
INSERT INTO `t_common_config` VALUES ('7', 'version', '1.2.3', '0', '0', '2018-03-28 20:50:18', '2018-09-27 16:03:47', '版本号');
INSERT INTO `t_common_config` VALUES ('8', 'meeting_groupId', '6d95d0f6e1a811e88aceac1f6b190e53', '0', '0', '2018-05-17 14:03:56', '2018-11-12 18:36:53', '会管登陆后返回的组织id');
INSERT INTO `t_common_config` VALUES ('9', 'max_tasks', '4', '0', '16', '2018-06-07 14:10:07', '2018-11-27 10:10:10', '最大任务数');
INSERT INTO `t_common_config` VALUES ('10', 'mainTitle', '浙江省电子政务视联网远程申办服务平台', '0', '16', '2018-08-03 13:30:06', '2018-11-27 10:10:10', '一级标题');
INSERT INTO `t_common_config` VALUES ('11', 'publicSubhead', '', '0', '16', '2018-08-03 13:30:06', '2018-11-29 14:53:35', '二级公安标题');
INSERT INTO `t_common_config` VALUES ('12', 'socialSubhead', '社保业务', '0', '16', '2018-08-03 13:30:06', '2018-11-27 10:10:10', '二级社保标题');
INSERT INTO `t_common_config` VALUES ('19', 'cms_image_uri', '', '0', '16', '2018-07-31 15:28:01', '2018-11-27 10:10:10', '存储网关图片地址uri');
INSERT INTO `t_common_config` VALUES ('20', 'printer_type', '1', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '常用打印机');
INSERT INTO `t_common_config` VALUES ('21', 'printer_type', '2', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '申请单打印机');
INSERT INTO `t_common_config` VALUES ('22', 'photo_config', '1', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '签名位置');
INSERT INTO `t_common_config` VALUES ('23', 'photo_config', '2', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '签章位置');
INSERT INTO `t_common_config` VALUES ('24', '20012', '12-01/12-20', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '禁止相关业务申办');
INSERT INTO `t_common_config` VALUES ('25', '20013', '12-01/12-20', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '禁止相关业务申办');
INSERT INTO `t_common_config` VALUES ('26', '20014', '12-01/12-20', '0', '0', '2018-08-06 18:39:55', '2018-08-06 18:39:55', '禁止相关业务申办');
INSERT INTO `t_common_config` VALUES ('27', 'sms_message', '【浙江省远程申办服务平台】尊敬的xxx，您当前有业务需要办理，请尽快处理！', '0', '0', '2018-11-07 11:39:18', '2018-11-07 14:30:43', '短信提醒业务办理内容');
INSERT INTO `t_common_config` VALUES ('28', 'sms_url', 'http://172.20.103.253:8080', '0', '0', '2018-11-07 14:29:49', '2018-11-07 14:31:39', '短信提醒业务办理');

-- ----------------------------
-- Table structure for t_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_key` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '审批中心key',
  `dept_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门名称',
  `dept_title` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '部门标题',
  `dept_contacts` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门联系人',
  `dept_contacts_phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门联系人电话',
  `dept_status` int(2) NOT NULL DEFAULT '0' COMMENT '部门状态  -2 删除  -1：禁用  0：启用',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `number_prefix` char(4) COLLATE utf8_bin DEFAULT NULL COMMENT '叫号前缀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_dept
-- ----------------------------

-- ----------------------------
-- Table structure for t_device_info
-- ----------------------------
DROP TABLE IF EXISTS `t_device_info`;
CREATE TABLE `t_device_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `service_key` varchar(64) DEFAULT NULL COMMENT '所属服务中心',
  `private_name` varchar(128) DEFAULT NULL COMMENT '设备私有名称',
  `device_name` varchar(128) DEFAULT NULL COMMENT '设备物理名称',
  `type` varchar(10) DEFAULT NULL COMMENT '类型  1:pad 2:一体机 3:打印机',
  `status` int(2) DEFAULT '0' COMMENT '状态  0：可用 -1:删除  2：不可用',
  `ip` varchar(20) DEFAULT NULL COMMENT 'ip',
  `port` int(5) DEFAULT NULL COMMENT '端口',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `password` varchar(64) DEFAULT '0' COMMENT '叫号机密码',
  `device_code` varchar(128) DEFAULT NULL COMMENT '叫号机设备唯一标识',
  `device_ip` varchar(255) DEFAULT NULL COMMENT '叫号机所在ip',
  `printer_type` varchar(255) DEFAULT NULL COMMENT '打印机类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `deviceId` (`id`) USING BTREE,
  KEY `serviceKey` (`service_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_device_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_education_type
-- ----------------------------
DROP TABLE IF EXISTS `t_education_type`;
CREATE TABLE `t_education_type` (
  `educationId` int(11) NOT NULL AUTO_INCREMENT COMMENT '学历id',
  `educationHierarchy` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '教育层级',
  PRIMARY KEY (`educationId`,`educationHierarchy`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_education_type
-- ----------------------------
INSERT INTO `t_education_type` VALUES ('1', '小学');
INSERT INTO `t_education_type` VALUES ('2', '初中');
INSERT INTO `t_education_type` VALUES ('3', '高中');
INSERT INTO `t_education_type` VALUES ('4', '专科');
INSERT INTO `t_education_type` VALUES ('5', '本科');
INSERT INTO `t_education_type` VALUES ('6', '硕士');
INSERT INTO `t_education_type` VALUES ('7', '博士');

-- ----------------------------
-- Table structure for t_forms
-- ----------------------------
DROP TABLE IF EXISTS `t_forms`;
CREATE TABLE `t_forms` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '名称',
  `formAddress` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '表单地址',
  `state` int(11) DEFAULT '0' COMMENT '状态 0：正常  -1：删除',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modifyTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_forms
-- ----------------------------
INSERT INTO `t_forms` VALUES ('1', '个人停缴申请书', '2个人停缴申请书.doc', '0', '0', '2018-07-27 10:45:54', '2018-07-30 10:00:13');
INSERT INTO `t_forms` VALUES ('2', '变更表（个人参保信息）', '3变更表（个人参保信息）.docx', '0', '0', '2018-07-30 10:00:28', '2018-07-30 10:00:28');
INSERT INTO `t_forms` VALUES ('3', '委托书', '20委托书.doc', '0', '0', '2018-07-30 10:00:40', '2018-07-30 12:15:44');
INSERT INTO `t_forms` VALUES ('4', '退休后换工资卡', '20退休后换工资卡.doc', '0', '0', '2018-07-30 10:00:50', '2018-07-30 10:00:50');
INSERT INTO `t_forms` VALUES ('5', '人力社保部门无差别受理手册', '人力社保部门无差别受理手册.doc', '0', '0', '2018-07-30 10:00:59', '2018-07-30 10:00:59');
INSERT INTO `t_forms` VALUES ('6', '桐乡市职工基本医疗保险异地安置人员就医申请表', '桐乡市职工基本医疗保险异地安置人员就医申请表.docx', '0', '0', '2018-07-30 11:10:21', '2018-07-30 11:10:26');

-- ----------------------------
-- Table structure for t_materials
-- ----------------------------
DROP TABLE IF EXISTS `t_materials`;
CREATE TABLE `t_materials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `materials_name` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '材料名称',
  `service_key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心key',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `materials_type` int(2) DEFAULT NULL COMMENT '1:申请单 2：附加申请材料 3：申请材料 4：业务确认书',
  `is_upload` int(2) DEFAULT NULL COMMENT '是否上传 0：不上传 1：上传',
  `file_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件路径',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `materials_version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `materials_status` int(2) NOT NULL DEFAULT '0' COMMENT '状态 -1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_materials
-- ----------------------------

-- ----------------------------
-- Table structure for t_meeting_operation
-- ----------------------------
DROP TABLE IF EXISTS `t_meeting_operation`;
CREATE TABLE `t_meeting_operation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) DEFAULT NULL COMMENT '业务唯一id',
  `schedule_id` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '会议id',
  `user_id` int(11) DEFAULT NULL COMMENT '操作人id 现更改为窗口id 字段名保持原来不变',
  `terminal_list` varchar(1000) COLLATE utf8_bin DEFAULT NULL COMMENT '会议终端列表',
  `ip` varchar(20) COLLATE utf8_bin DEFAULT '' COMMENT '主席ip',
  `status` int(2) DEFAULT '0' COMMENT '状态,0:会议中,1:已停会',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_share` int(2) DEFAULT '0' COMMENT '是否分享  isShare  0 ：未开启  1：已开启',
  `dyn_device_number` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '动态入会得终端号码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_meeting_operation
-- ----------------------------

-- ----------------------------
-- Table structure for t_nation
-- ----------------------------
DROP TABLE IF EXISTS `t_nation`;
CREATE TABLE `t_nation` (
  `nationNumber` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '民族编号',
  `nationName` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '民族名称',
  PRIMARY KEY (`nationNumber`,`nationName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_nation
-- ----------------------------
INSERT INTO `t_nation` VALUES ('01', '汉');
INSERT INTO `t_nation` VALUES ('02', '蒙古');
INSERT INTO `t_nation` VALUES ('03', '回');
INSERT INTO `t_nation` VALUES ('04', '藏');
INSERT INTO `t_nation` VALUES ('05', '维吾尔');
INSERT INTO `t_nation` VALUES ('06', '苗');
INSERT INTO `t_nation` VALUES ('07', '彝');
INSERT INTO `t_nation` VALUES ('08', '壮');
INSERT INTO `t_nation` VALUES ('09', '布依');
INSERT INTO `t_nation` VALUES ('10', '朝鲜');
INSERT INTO `t_nation` VALUES ('11', '满');
INSERT INTO `t_nation` VALUES ('12', '侗');
INSERT INTO `t_nation` VALUES ('13', '瑶');
INSERT INTO `t_nation` VALUES ('14', '白');
INSERT INTO `t_nation` VALUES ('15', '土家');
INSERT INTO `t_nation` VALUES ('16', '哈尼');
INSERT INTO `t_nation` VALUES ('17', '哈萨克');
INSERT INTO `t_nation` VALUES ('18', '傣');
INSERT INTO `t_nation` VALUES ('19', '黎');
INSERT INTO `t_nation` VALUES ('20', '傈僳');
INSERT INTO `t_nation` VALUES ('21', '佤');
INSERT INTO `t_nation` VALUES ('22', '畲');
INSERT INTO `t_nation` VALUES ('23', '高山');
INSERT INTO `t_nation` VALUES ('24', '拉祜');
INSERT INTO `t_nation` VALUES ('25', '水');
INSERT INTO `t_nation` VALUES ('26', '东乡');
INSERT INTO `t_nation` VALUES ('27', '纳西');
INSERT INTO `t_nation` VALUES ('28', '景颇');
INSERT INTO `t_nation` VALUES ('29', '柯尔克孜');
INSERT INTO `t_nation` VALUES ('30', '土');
INSERT INTO `t_nation` VALUES ('31', '达斡尔');
INSERT INTO `t_nation` VALUES ('32', '仫佬');
INSERT INTO `t_nation` VALUES ('33', '羌');
INSERT INTO `t_nation` VALUES ('34', '布朗');
INSERT INTO `t_nation` VALUES ('35', '撒拉');
INSERT INTO `t_nation` VALUES ('36', '毛南');
INSERT INTO `t_nation` VALUES ('37', '仡佬');
INSERT INTO `t_nation` VALUES ('38', '锡伯');
INSERT INTO `t_nation` VALUES ('39', '阿昌');
INSERT INTO `t_nation` VALUES ('40', '普米');
INSERT INTO `t_nation` VALUES ('41', '塔吉克');
INSERT INTO `t_nation` VALUES ('42', '怒');
INSERT INTO `t_nation` VALUES ('43', '乌孜别克');
INSERT INTO `t_nation` VALUES ('44', '俄罗斯');
INSERT INTO `t_nation` VALUES ('45', '鄂温克');
INSERT INTO `t_nation` VALUES ('46', '德昂');
INSERT INTO `t_nation` VALUES ('47', '保安');
INSERT INTO `t_nation` VALUES ('48', '裕固');
INSERT INTO `t_nation` VALUES ('49', '京');
INSERT INTO `t_nation` VALUES ('50', '塔塔尔');
INSERT INTO `t_nation` VALUES ('51', '独龙');
INSERT INTO `t_nation` VALUES ('52', '鄂伦春');
INSERT INTO `t_nation` VALUES ('53', '赫哲');
INSERT INTO `t_nation` VALUES ('54', '门巴');
INSERT INTO `t_nation` VALUES ('55', '珞巴');
INSERT INTO `t_nation` VALUES ('56', '基诺');

-- ----------------------------
-- Table structure for t_newborn_base_info
-- ----------------------------
DROP TABLE IF EXISTS `t_newborn_base_info`;
CREATE TABLE `t_newborn_base_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务唯一key，生成号码时候对应生成的key',
  `pre_address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '迁入前地址',
  `pre_police_station` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '迁入前派出所',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '迁入地址',
  `police_station` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '迁入派出所',
  `excuse` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '申请事由',
  `comment` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '被投靠户户主意见',
  `address_information` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '现住址信息',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='新生儿基本信息表';

-- ----------------------------
-- Records of t_newborn_base_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_newborn_info
-- ----------------------------
DROP TABLE IF EXISTS `t_newborn_info`;
CREATE TABLE `t_newborn_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `base_id` int(11) DEFAULT NULL COMMENT '主表关联id',
  `birth_date` int(11) DEFAULT NULL COMMENT '出生日期',
  `relation` varchar(40) COLLATE utf8_bin DEFAULT NULL COMMENT '申请人与变动人关系',
  `phone` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '联系方式',
  `full_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '新生儿姓名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='新生儿信息表';

-- ----------------------------
-- Records of t_newborn_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_number_iteration
-- ----------------------------
DROP TABLE IF EXISTS `t_number_iteration`;
CREATE TABLE `t_number_iteration` (
  `service_key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '审批中心唯一key',
  `dept_id` int(11) NOT NULL COMMENT '部门id',
  `number` int(11) NOT NULL DEFAULT '0' COMMENT '号码每天从0开始',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_number_iteration
-- ----------------------------

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `permissionId` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `permissionName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '权限名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '权限功能详细描述',
  `status` int(2) DEFAULT '1' COMMENT '权限状态，1有效，0无效',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `defaultPermission` int(2) DEFAULT '0' COMMENT '是否为默认权限，0否，1是',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `url` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '权限url地址，可以为空',
  PRIMARY KEY (`permissionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_permission
-- ----------------------------

-- ----------------------------
-- Table structure for t_photo
-- ----------------------------
DROP TABLE IF EXISTS `t_photo`;
CREATE TABLE `t_photo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片主键',
  `picture_type` int(11) DEFAULT NULL COMMENT '图片类型  图片类型id 参照t_picture_type表',
  `materials_id` int(11) DEFAULT NULL COMMENT '证件类型id 。参照t_certificate_type表',
  `guid` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '会议id',
  `business_id` int(11) NOT NULL COMMENT '业务唯一key',
  `v2v_id` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '终端号码',
  `picture_id` int(11) NOT NULL COMMENT '图片的id',
  `frame_path` varchar(1500) COLLATE utf8_bin DEFAULT NULL COMMENT '大图的地址',
  `icon_path` varchar(1500) COLLATE utf8_bin DEFAULT NULL COMMENT '缩略图地址',
  `statu` int(2) NOT NULL DEFAULT '0' COMMENT '状态 -1删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'timestamp',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `heigth` int(10) DEFAULT NULL COMMENT '图片高度',
  `width` int(10) DEFAULT NULL COMMENT '图片宽',
  `format` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '图片格式',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_photo
-- ----------------------------

-- ----------------------------
-- Table structure for t_photo_config
-- ----------------------------
DROP TABLE IF EXISTS `t_photo_config`;
CREATE TABLE `t_photo_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_type` int(10) DEFAULT NULL COMMENT '业务类型',
  `type` tinyint(1) DEFAULT NULL COMMENT '类型 参照t_common_config   photo_config value',
  `x` int(11) DEFAULT NULL COMMENT 'x坐标',
  `y` int(11) DEFAULT NULL COMMENT 'y坐标',
  `width` int(11) DEFAULT NULL COMMENT '宽度',
  `height` int(11) DEFAULT NULL COMMENT '高度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='图片签名和签章配置';

-- ----------------------------
-- Records of t_photo_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_picture_type
-- ----------------------------
DROP TABLE IF EXISTS `t_picture_type`;
CREATE TABLE `t_picture_type` (
  `typeId` int(11) NOT NULL AUTO_INCREMENT COMMENT '图片类型id',
  `typeName` varchar(32) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '图片类型名称',
  PRIMARY KEY (`typeId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_picture_type
-- ----------------------------
INSERT INTO `t_picture_type` VALUES ('1', '截图');
INSERT INTO `t_picture_type` VALUES ('2', '身份证头像');
INSERT INTO `t_picture_type` VALUES ('3', '签名照');
INSERT INTO `t_picture_type` VALUES ('4', '指纹找');
INSERT INTO `t_picture_type` VALUES ('5', '证件照');

-- ----------------------------
-- Table structure for t_project_change
-- ----------------------------
DROP TABLE IF EXISTS `t_project_change`;
CREATE TABLE `t_project_change` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `businessKey` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '业务key',
  `applicantName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '申请人姓名',
  `IDNumber` varchar(36) COLLATE utf8_bin DEFAULT NULL COMMENT '证件号码',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '地址',
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '联系方式',
  `cause` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '变更原因',
  `projectName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '项目名称',
  `beforeChangeNumber` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '变更前业务详情号码（参照业务详情分类表t_business_details_classification  ）',
  `afterChangeNumber` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '变更后业务详情号码（参照业务详情分类表t_business_details_classification  ）',
  `userName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '用户真实姓名（受理人）',
  `changeTime` datetime DEFAULT NULL COMMENT '变更时间',
  `affiliation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '归属',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_project_change
-- ----------------------------

-- ----------------------------
-- Table structure for t_regionb
-- ----------------------------
DROP TABLE IF EXISTS `t_regionb`;
CREATE TABLE `t_regionb` (
  `id` varchar(12) NOT NULL,
  `pid` varchar(12) NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL,
  `gradeid` int(11) NOT NULL DEFAULT '0',
  `isleaf` char(1) NOT NULL DEFAULT '0',
  `updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_regionb
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '角色名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '角色描述',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(2) DEFAULT '1' COMMENT '角色状态，1有效，0无效',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_role_permission_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission_rel`;
CREATE TABLE `t_role_permission_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `roleId` int(11) DEFAULT NULL COMMENT '角色id',
  `permissionId` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_role_permission_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_service_center
-- ----------------------------
DROP TABLE IF EXISTS `t_service_center`;
CREATE TABLE `t_service_center` (
  `serviceId` int(11) NOT NULL AUTO_INCREMENT COMMENT '服务中心主键',
  `serviceKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心唯一key',
  `parentKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '父级服务中心唯一key',
  `serviceName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中名字',
  `gradeId` int(2) DEFAULT NULL COMMENT '服务中心级别 ，0：全国 1：省级 2：市级 3：县级(区) 4：乡镇 5:村',
  `regionKey` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '行政区域最后一级',
  `regionId` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '行政区域id',
  `regionAll` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '全部行政区域',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `status` int(2) DEFAULT '0' COMMENT '状态 0：可用 -1:删除  2：不可用',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `serviceTitle` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '服务中心标题',
  `type` bigint(255) DEFAULT NULL COMMENT '类型 1：统筹中心 2：审核中心 3：服务中心',
  PRIMARY KEY (`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_service_center
-- ----------------------------

-- ----------------------------
-- Table structure for t_signedphotoes
-- ----------------------------
DROP TABLE IF EXISTS `t_signedphotoes`;
CREATE TABLE `t_signedphotoes` (
  `id` int(10) NOT NULL COMMENT '主键',
  `guid` varchar(32) DEFAULT '' COMMENT '业务ID',
  `businessKey` varchar(255) DEFAULT '' COMMENT '业务唯一key',
  `v2vid` varchar(32) DEFAULT '' COMMENT '终端号码',
  `photopath` varchar(500) DEFAULT '' COMMENT '图片地址',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '图片生成时间',
  `width` int(10) DEFAULT '0' COMMENT '图片宽',
  `heigth` int(10) DEFAULT '0' COMMENT '图片高度',
  `format` varchar(32) DEFAULT '' COMMENT '图片格式',
  `version` int(10) DEFAULT '0' COMMENT '版本号',
  `status` int(2) DEFAULT '0' COMMENT '状态 0： 正常 -1：删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_signedphotoes
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dept`;
CREATE TABLE `t_sys_dept` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_key` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '审批中心key',
  `dept_name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门名称',
  `dept_title` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '部门标题',
  `dept_contacts` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门联系人',
  `dept_contacts_phone` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '部门联系人电话',
  `dept_status` int(2) NOT NULL DEFAULT '0' COMMENT '部门状态  -2 删除  -1：禁用  0：启用',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `number_prefix` char(4) COLLATE utf8_bin DEFAULT NULL COMMENT '叫号前缀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_dept
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_dept_service_center_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_dept_service_center_rel`;
CREATE TABLE `t_sys_dept_service_center_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_id` int(11) NOT NULL COMMENT '部门id主键',
  `service_center_id` int(11) NOT NULL COMMENT '服务中心id主键',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_dept_service_center_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_permission`;
CREATE TABLE `t_sys_permission` (
  `permissionId` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `parentId` bigint(20) DEFAULT NULL COMMENT '父权限ID，顶级权限为0',
  `permissionName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '权限名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '权限功能详细描述',
  `status` int(2) DEFAULT '1' COMMENT '权限状态，1有效，0无效',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `defaultPermission` int(2) DEFAULT '0' COMMENT '是否为默认权限，0否，1是',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `url` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '权限url地址，可以为空',
  `perms` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `icon` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '菜单栏icon',
  PRIMARY KEY (`permissionId`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=93 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of t_sys_permission
-- ----------------------------
INSERT INTO `t_sys_permission` VALUES ('1', '0', '首页', '', '1', '1', '2018-10-30 17:34:59', '2018-11-08 21:18:49', '1', '0', 'echart', null, null);
INSERT INTO `t_sys_permission` VALUES ('2', '0', '业务统计', '', '1', '1', '2018-10-30 17:35:23', '2018-11-08 21:18:52', '1', '0', 'businessStatistice', null, null);
INSERT INTO `t_sys_permission` VALUES ('3', '0', '系统设置', '', '1', '0', '2018-10-30 17:35:36', '2018-10-30 17:35:36', '0', '0', '', null, null);
INSERT INTO `t_sys_permission` VALUES ('4', '3', '系统日志', '', '1', '1', '2018-10-30 17:37:31', '2018-12-05 16:28:06', '0', '0', 'systemLog', 'log:query', 'sys_log');
INSERT INTO `t_sys_permission` VALUES ('5', '3', '权限管理', '', '1', '0', '2018-10-30 17:37:48', '2018-11-30 10:01:50', '0', '0', '', null, 'user_mng');
INSERT INTO `t_sys_permission` VALUES ('6', '3', '部门管理', '', '1', '1', '2018-10-30 17:37:57', '2018-12-05 16:28:29', '0', '0', 'management', 'dept:query', 'dept_mng');
INSERT INTO `t_sys_permission` VALUES ('7', '3', '资源管理', '', '1', '0', '2018-10-30 17:38:06', '2018-11-26 16:10:04', '0', '0', '', null, 'resource_mng');
INSERT INTO `t_sys_permission` VALUES ('8', '3', '业务管理', '', '1', '0', '2018-10-30 17:38:16', '2018-11-26 16:10:10', '0', '0', '', null, 'business_mng');
INSERT INTO `t_sys_permission` VALUES ('9', '3', '系统设置', '', '1', '1', '2018-10-30 17:38:34', '2018-11-26 16:10:12', '0', '0', 'setting', null, 'sys_config');
INSERT INTO `t_sys_permission` VALUES ('11', '4', '导出', '', '1', '2', '2018-10-30 17:39:44', '2018-10-30 18:44:49', '0', '0', '', 'log:export', null);
INSERT INTO `t_sys_permission` VALUES ('12', '5', '用户管理', '', '1', '1', '2018-10-30 17:43:24', '2018-12-05 16:14:49', '0', '0', 'userManagement', 'user:query', null);
INSERT INTO `t_sys_permission` VALUES ('13', '5', '角色管理', '', '1', '1', '2018-10-30 17:43:36', '2018-12-05 16:31:56', '0', '0', 'authorityManagement', 'role:query', null);
INSERT INTO `t_sys_permission` VALUES ('15', '6', '新建', '', '1', '2', '2018-10-30 17:47:24', '2018-10-30 18:44:49', '0', '0', '', 'dept:add', null);
INSERT INTO `t_sys_permission` VALUES ('17', '6', '禁用/启用', '', '1', '2', '2018-10-30 18:01:55', '2018-10-30 18:42:56', '0', '0', '', 'dept:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('18', '6', '编辑', '', '1', '2', '2018-10-30 18:02:27', '2018-10-30 18:43:07', '0', '0', '', 'dept:edit', null);
INSERT INTO `t_sys_permission` VALUES ('19', '6', '删除', '', '1', '2', '2018-10-30 18:02:49', '2018-10-30 18:43:16', '0', '0', '', 'dept:delete', null);
INSERT INTO `t_sys_permission` VALUES ('20', '6', '详情', '', '1', '2', '2018-10-30 18:03:07', '2018-10-30 18:43:28', '0', '0', '', 'dept:info', null);
INSERT INTO `t_sys_permission` VALUES ('22', '12', '新建', '', '1', '2', '2018-10-30 18:04:45', '2018-10-30 18:43:49', '0', '0', '', 'user:add', null);
INSERT INTO `t_sys_permission` VALUES ('23', '12', '编辑', '', '1', '2', '2018-10-30 18:05:03', '2018-10-30 18:43:57', '0', '0', '', 'user:edit', null);
INSERT INTO `t_sys_permission` VALUES ('24', '12', '删除', '', '1', '2', '2018-10-30 18:05:18', '2018-10-30 18:44:05', '0', '0', '', 'user:delete', null);
INSERT INTO `t_sys_permission` VALUES ('25', '13', '新建', '', '1', '2', '2018-10-30 18:05:37', '2018-10-30 18:44:15', '0', '0', '', 'role:add', null);
INSERT INTO `t_sys_permission` VALUES ('26', '13', '编辑', '', '1', '2', '2018-10-30 18:05:55', '2018-10-30 18:44:28', '0', '0', '', 'role:edit', null);
INSERT INTO `t_sys_permission` VALUES ('27', '13', '删除', '', '1', '2', '2018-10-30 18:06:10', '2018-10-30 18:44:49', '0', '0', '', 'role:delete', null);
INSERT INTO `t_sys_permission` VALUES ('28', '7', '中心管理', '', '1', '0', '2018-10-30 18:06:55', '2018-12-05 16:28:51', '0', '0', 'centerManagement', 'center:query', null);
INSERT INTO `t_sys_permission` VALUES ('29', '7', '窗口管理', '', '1', '1', '2018-10-30 18:07:13', '2018-12-05 16:29:58', '0', '0', 'windowManagement', 'window:query', null);
INSERT INTO `t_sys_permission` VALUES ('30', '7', '终端管理', '', '1', '1', '2018-10-30 18:07:27', '2018-12-05 16:30:06', '0', '0', 'terminalMachineManagement', 'terminal:query', null);
INSERT INTO `t_sys_permission` VALUES ('31', '7', '设备管理', '', '1', '1', '2018-10-30 18:07:42', '2018-12-05 16:30:22', '0', '0', 'otherMachineManagement', 'device:query', null);
INSERT INTO `t_sys_permission` VALUES ('36', '28', '新建', '', '1', '2', '2018-10-30 18:10:15', '2018-10-30 18:45:19', '0', '0', '', 'center:add', null);
INSERT INTO `t_sys_permission` VALUES ('38', '29', '新建', '', '1', '2', '2018-10-30 18:12:00', '2018-10-30 18:45:47', '0', '0', '', 'window:add', null);
INSERT INTO `t_sys_permission` VALUES ('39', '29', '启用/禁用', '', '1', '2', '2018-10-30 18:12:20', '2018-10-30 18:46:02', '0', '0', '', 'window:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('40', '29', '编辑', '', '1', '2', '2018-10-30 18:12:36', '2018-10-30 18:46:10', '0', '0', '', 'window:edit', null);
INSERT INTO `t_sys_permission` VALUES ('41', '29', '删除', '', '1', '2', '2018-10-30 18:12:51', '2018-10-30 18:46:18', '0', '0', '', 'window:delete', null);
INSERT INTO `t_sys_permission` VALUES ('42', '29', '详情', '', '1', '2', '2018-10-30 18:13:13', '2018-10-30 18:46:33', '0', '0', '', 'window:info', null);
INSERT INTO `t_sys_permission` VALUES ('44', '30', '同步终端', '', '1', '2', '2018-10-30 18:14:02', '2018-10-30 18:47:34', '0', '0', '', 'terminal:sync', null);
INSERT INTO `t_sys_permission` VALUES ('45', '30', '绑定/解绑', '', '1', '2', '2018-10-30 18:14:31', '2018-10-30 18:47:50', '0', '0', '', 'terminal:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('46', '30', '详情', '', '1', '2', '2018-10-30 18:14:44', '2018-10-30 18:47:58', '0', '0', '', 'terminal:info', null);
INSERT INTO `t_sys_permission` VALUES ('48', '31', '新建', '', '1', '2', '2018-10-30 18:15:19', '2018-10-30 18:48:35', '0', '0', '', 'device:add', null);
INSERT INTO `t_sys_permission` VALUES ('49', '31', '编辑', '', '1', '2', '2018-10-30 18:15:43', '2018-10-30 18:48:41', '0', '0', '', 'device:edit', null);
INSERT INTO `t_sys_permission` VALUES ('50', '31', '删除', '', '1', '2', '2018-10-30 18:15:54', '2018-10-30 18:48:49', '0', '0', '', 'device:delete', null);
INSERT INTO `t_sys_permission` VALUES ('51', '31', '详情', '', '1', '2', '2018-10-30 18:16:04', '2018-10-30 18:48:56', '0', '0', '', 'device:info', null);
INSERT INTO `t_sys_permission` VALUES ('52', '8', '业务列表', '', '1', '1', '2018-10-30 18:16:20', '2018-12-05 16:30:46', '0', '0', 'businessDetail', 'business:info:query', null);
INSERT INTO `t_sys_permission` VALUES ('53', '8', '业务类别', '', '1', '1', '2018-10-30 18:17:09', '2018-12-05 16:30:56', '0', '0', 'businessCategory', 'business:type:query', null);
INSERT INTO `t_sys_permission` VALUES ('54', '8', '业务材料', '', '1', '1', '2018-10-30 18:17:29', '2018-12-05 16:31:03', '0', '0', 'businessMaterial', 'business:material:query', null);
INSERT INTO `t_sys_permission` VALUES ('56', '52', ' 新建', '', '1', '2', '2018-10-30 18:18:04', '2018-10-30 18:49:54', '0', '0', '', 'business:info:add', null);
INSERT INTO `t_sys_permission` VALUES ('57', '52', '启用/禁用', '', '1', '2', '2018-10-30 18:18:27', '2018-10-30 18:50:04', '0', '0', '', 'business:info:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('58', '52', '编辑', '', '1', '2', '2018-10-30 18:18:50', '2018-10-30 18:50:10', '0', '0', '', 'business:info:edit', null);
INSERT INTO `t_sys_permission` VALUES ('59', '52', '删除', '', '1', '2', '2018-10-30 18:19:02', '2018-10-30 18:50:15', '0', '0', '', 'business:info:delete', null);
INSERT INTO `t_sys_permission` VALUES ('60', '52', '详情', '', '1', '2', '2018-10-30 18:19:15', '2018-10-30 18:50:23', '0', '0', '', 'business:info:info', null);
INSERT INTO `t_sys_permission` VALUES ('62', '53', '新建', '', '1', '2', '2018-10-30 18:20:39', '2018-10-30 18:50:53', '0', '0', '', 'business:type:add', null);
INSERT INTO `t_sys_permission` VALUES ('63', '53', '启用/禁用', '', '1', '2', '2018-10-30 18:20:53', '2018-10-30 18:51:02', '0', '0', '', 'business:type:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('64', '53', '编辑', '', '1', '2', '2018-10-30 18:21:05', '2018-10-30 18:51:08', '0', '0', '', 'business:type:edit', null);
INSERT INTO `t_sys_permission` VALUES ('65', '53', '删除', '', '1', '2', '2018-10-30 18:21:19', '2018-10-30 18:51:14', '0', '0', '', 'business:type:delete', null);
INSERT INTO `t_sys_permission` VALUES ('66', '53', '详情', '', '1', '2', '2018-10-30 18:21:30', '2018-10-30 18:51:18', '0', '0', '', 'business:type:info', null);
INSERT INTO `t_sys_permission` VALUES ('68', '54', '新建', '', '1', '2', '2018-10-30 18:22:03', '2018-10-30 18:52:02', '0', '0', '', 'business:material:add', null);
INSERT INTO `t_sys_permission` VALUES ('69', '54', '编辑', '', '1', '2', '2018-10-30 18:22:18', '2018-10-30 18:52:11', '0', '0', '', 'business:material:edit', null);
INSERT INTO `t_sys_permission` VALUES ('70', '54', '删除', '', '1', '2', '2018-10-30 18:22:30', '2018-10-30 18:52:19', '0', '0', '', 'business:material:delete', null);
INSERT INTO `t_sys_permission` VALUES ('71', '54', '详情', '', '1', '2', '2018-10-30 18:22:41', '2018-10-30 18:52:24', '0', '0', '', 'business:material:info', null);
INSERT INTO `t_sys_permission` VALUES ('73', '28', '统筹中心', '', '1', '1', '2018-11-07 14:00:23', '2018-12-10 16:07:49', '0', '0', 'centerManagement', 'center:first', null);
INSERT INTO `t_sys_permission` VALUES ('74', '28', '审批中心', '', '1', '1', '2018-11-07 14:00:33', '2018-12-10 16:07:50', '0', '0', 'centerManagement', 'center:second', null);
INSERT INTO `t_sys_permission` VALUES ('75', '28', '服务中心', '', '1', '1', '2018-11-07 14:00:47', '2018-12-10 16:07:53', '0', '0', 'centerManagement', 'second:third', null);
INSERT INTO `t_sys_permission` VALUES ('76', '73', '编辑', '', '1', '2', '2018-11-07 14:01:32', '2018-12-03 17:55:31', '0', '0', 'centerManagement', 'center:first:edit', null);
INSERT INTO `t_sys_permission` VALUES ('77', '73', '删除', '', '1', '2', '2018-11-07 14:01:39', '2018-12-03 17:55:44', '0', '0', 'centerManagement', 'center:first:delete', null);
INSERT INTO `t_sys_permission` VALUES ('78', '74', '编辑', '', '1', '2', '2018-11-07 14:01:51', '2018-12-03 17:55:57', '0', '0', 'centerManagement', 'center:second:edit', null);
INSERT INTO `t_sys_permission` VALUES ('79', '74', '删除', '', '1', '2', '2018-11-07 14:01:59', '2018-12-03 17:56:02', '0', '0', 'centerManagement', 'center:second:delete', null);
INSERT INTO `t_sys_permission` VALUES ('80', '75', '编辑', '', '1', '2', '2018-11-07 14:02:08', '2018-12-03 17:56:14', '0', '0', 'centerManagement', 'center:third:edit', null);
INSERT INTO `t_sys_permission` VALUES ('81', '75', '删除', '', '1', '2', '2018-11-07 14:02:14', '2018-12-03 17:56:20', '0', '0', 'centerManagement', 'center:third:delete', null);
INSERT INTO `t_sys_permission` VALUES ('82', '73', '详情', '', '1', '2', '2018-11-07 14:07:02', '2018-12-03 17:56:45', '0', '0', '', 'center:first:info', null);
INSERT INTO `t_sys_permission` VALUES ('83', '74', '详情', '', '1', '2', '2018-11-07 14:07:18', '2018-12-03 17:56:53', '0', '0', '', 'center:second:info', null);
INSERT INTO `t_sys_permission` VALUES ('84', '75', '详情', '', '1', '2', '2018-11-07 14:07:33', '2018-12-03 17:57:03', '0', '0', '', 'center:third:info', null);
INSERT INTO `t_sys_permission` VALUES ('85', '12', ' 详情', '', '1', '2', '2018-11-30 16:24:06', null, '0', '0', '', 'user:info', null);
INSERT INTO `t_sys_permission` VALUES ('86', '12', ' 重置密码', '', '1', '2', '2018-11-30 16:21:30', null, '0', '0', '', 'user:reset', null);
INSERT INTO `t_sys_permission` VALUES ('87', '12', ' 启用/禁用', '', '1', '2', '2018-11-30 16:32:09', null, '0', '0', '', 'user:changeStatus', null);
INSERT INTO `t_sys_permission` VALUES ('89', '13', '详情', '', '1', '2', '2018-11-30 16:37:02', null, '0', '0', '', 'role:info', null);
INSERT INTO `t_sys_permission` VALUES ('90', '9', '标题设置', '', '1', '2', '2018-11-30 16:48:50', '2018-12-03 11:37:35', '0', '0', '', 'sys:title', null);
INSERT INTO `t_sys_permission` VALUES ('91', '9', '服务设置', '', '1', '2', '2018-11-30 16:48:50', '2018-12-03 11:37:40', '0', '0', '', 'sys:service', null);
INSERT INTO `t_sys_permission` VALUES ('92', '9', '系统设置', '', '1', '2', '2018-11-30 16:48:50', '2018-12-03 11:37:47', '0', '0', '', 'sys:system', null);

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role` (
  `roleId` int(11) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '角色名称',
  `description` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '角色描述',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` int(2) DEFAULT '1' COMMENT '角色状态，1有效，0无效',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `type` int(2) DEFAULT NULL COMMENT '0：管理员；1：业务员',
  `roleSign` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '角色标识',
  `creater` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_role_permission_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_permission_rel`;
CREATE TABLE `t_sys_role_permission_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `roleId` int(11) DEFAULT NULL COMMENT '角色id',
  `permissionId` int(11) DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_role_permission_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `loginName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '用户名或登录名',
  `userName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '真实姓名',
  `password` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '用户密码',
  `status` varchar(2) COLLATE utf8_bin DEFAULT '0' COMMENT '用户状态0离线，1在线 ,-1删除',
  `workStatus` int(2) DEFAULT '0' COMMENT '工作状态： 0：空闲中 1：等待中 2：处理中 3：可用 4：不可用',
  `mobilePhone` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '手机号码',
  `email` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '电子邮箱',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `lastLoginTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `affiliation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '归属',
  `serviceKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心唯一key',
  `deptId` int(11) DEFAULT NULL COMMENT '部门id  0代表所有部门',
  `salt` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '盐',
  `type` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '0超管，1统筹中西管理员，2审核中心管理员，3业务员',
  `userStatus` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '1可用 2不可用',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES ('1', 'admin', '超级管理员', 'dd643ec30e8be3f9361a8b7d1272a31e', '3', '0', '1333333333', '13@qq.com', '2018-03-26 14:25:03', '2018-11-29 10:22:43', '2018-11-29 10:22:43', '0', '测试归属请修改', '', '0', '3a0c6dd6020f408e8992184e40efedcf', '0', '1');

-- ----------------------------
-- Table structure for t_sys_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role_rel`;
CREATE TABLE `t_sys_user_role_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `roleId` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_user_role_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_sys_window
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_window`;
CREATE TABLE `t_sys_window` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `windowName` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '窗口名',
  `serviceKey` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '服务中心唯一key',
  `deptId` int(11) NOT NULL COMMENT '部门主键',
  `ip` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'ip地址',
  `status` int(11) NOT NULL COMMENT '状态 1可用 2不可用  -1删除',
  `isUse` int(11) DEFAULT '2' COMMENT '\r\n判断是否是使用中 1 使用中 2空闲中',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_sys_window
-- ----------------------------

-- ----------------------------
-- Table structure for t_terminal
-- ----------------------------
DROP TABLE IF EXISTS `t_terminal`;
CREATE TABLE `t_terminal` (
  `terminalId` int(12) NOT NULL AUTO_INCREMENT COMMENT '终端id',
  `terminalName` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '终端名称',
  `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '终端号',
  `ip` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '终端ip',
  `serviceKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心key',
  `type` int(2) DEFAULT NULL COMMENT '终端机的种类 1：启明 2：极光 3：手机 4.安卓盒子',
  `status` int(2) DEFAULT '0' COMMENT '状态 0：空闲 1：使用中 -1:不可用 -2：删除',
  `address` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '详细地址',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(12) DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`terminalId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_terminal
-- ----------------------------

-- ----------------------------
-- Table structure for t_title
-- ----------------------------
DROP TABLE IF EXISTS `t_title`;
CREATE TABLE `t_title` (
  `titleId` int(11) NOT NULL AUTO_INCREMENT COMMENT '标题id',
  `mainTitle` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '主标题',
  `publicSubhead` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '公安类副标题',
  `socialSubhead` varchar(255) COLLATE utf8_bin DEFAULT '' COMMENT '社保类副标题',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updatetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`titleId`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_title
-- ----------------------------
INSERT INTO `t_title` VALUES ('1', '浙江省电子政务视联网远程申办服务平台', '桐乡市公安局远程申办服务系统', '桐乡市社保局远程申办服务系统', '2018-04-08 15:15:55', '2018-04-12 11:54:09', '7');

-- ----------------------------
-- Table structure for t_type_materials_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_type_materials_rel`;
CREATE TABLE `t_type_materials_rel` (
  `type_id` int(11) NOT NULL COMMENT '业务类型id',
  `materials_id` int(11) NOT NULL COMMENT '材料id',
  PRIMARY KEY (`type_id`,`materials_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_type_materials_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_unattended_cause
-- ----------------------------
DROP TABLE IF EXISTS `t_unattended_cause`;
CREATE TABLE `t_unattended_cause` (
  `causeId` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '原因id',
  `causeName` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '原因名称',
  PRIMARY KEY (`causeId`,`causeName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_unattended_cause
-- ----------------------------
INSERT INTO `t_unattended_cause` VALUES ('1', '材料不全');
INSERT INTO `t_unattended_cause` VALUES ('2', '材料不符合规范');
INSERT INTO `t_unattended_cause` VALUES ('3', '材料不实');
INSERT INTO `t_unattended_cause` VALUES ('4', '不是本人');
INSERT INTO `t_unattended_cause` VALUES ('5', '其他');
INSERT INTO `t_unattended_cause` VALUES ('6', '异常操作');

-- ----------------------------
-- Table structure for t_upload
-- ----------------------------
DROP TABLE IF EXISTS `t_upload`;
CREATE TABLE `t_upload` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `unique_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '唯一名称',
  `file_description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件描述',
  `file_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '文件路径',
  `original_filename` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '原名称',
  `file_type` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '文件类型(后缀)',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `business_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '业务key',
  `state` int(11) DEFAULT '0' COMMENT '状态 0 ：正常 -1 删除 1 生成签名图片 2 生成签章图片 3 生成图片加签章图片',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `absolute_path` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '绝对路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_upload
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `loginName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '用户名或登录名',
  `userName` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '真实姓名',
  `password` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '用户密码',
  `status` int(2) DEFAULT '0' COMMENT '用户状态0离线，1在线 ,-1删除',
  `workStatus` int(2) DEFAULT '0' COMMENT '工作状态： 0：空闲中 1：等待中 2：处理中',
  `mobilePhone` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '手机号码',
  `email` varchar(50) COLLATE utf8_bin DEFAULT '' COMMENT '电子邮箱',
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `lastLoginTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后登录时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `affiliation` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '归属',
  `serviceKey` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '服务中心唯一key',
  `deptId` int(11) DEFAULT NULL COMMENT '部门id',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_role_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role_rel`;
CREATE TABLE `t_user_role_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `roleId` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user_role_rel
-- ----------------------------

-- ----------------------------
-- Table structure for t_user_terminal_rel
-- ----------------------------
DROP TABLE IF EXISTS `t_user_terminal_rel`;
CREATE TABLE `t_user_terminal_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `terminalId` int(11) DEFAULT NULL COMMENT '终端id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_user_terminal_rel
-- ----------------------------

-- ----------------------------
-- Table structure for vc_dev
-- ----------------------------
DROP TABLE IF EXISTS `vc_dev`;
CREATE TABLE `vc_dev` (
  `id` int(11) NOT NULL,
  `name` varchar(600) DEFAULT NULL COMMENT '设备名称',
  `mac` char(30) DEFAULT NULL COMMENT '设备mac地址',
  `ip` char(30) DEFAULT NULL COMMENT '设备ip地址',
  `type` int(11) DEFAULT '1' COMMENT '设备类型。1-启明；2-极光;3-手机;4-安卓盒子',
  `description` char(255) DEFAULT NULL COMMENT '备注',
  `alias` varchar(600) DEFAULT NULL COMMENT '设备别名',
  `role` int(11) DEFAULT '0' COMMENT '设备角色',
  `p_x` varchar(20) DEFAULT NULL COMMENT 'gis x坐标值',
  `p_y` varchar(20) DEFAULT NULL COMMENT 'gis y坐标值',
  `p_z` varchar(20) DEFAULT NULL COMMENT 'gis z坐标值',
  `p_layer` int(11) DEFAULT NULL COMMENT 'gis 层级',
  `svrid` int(11) DEFAULT '-1' COMMENT '设备所属服务器ID',
  `updatetime` datetime DEFAULT NULL,
  `svr_region_id` varchar(32) DEFAULT NULL COMMENT '数据来源-子系统行政区域ID',
  `address` varchar(300) DEFAULT NULL COMMENT '设备详细地址',
  `state` int(2) DEFAULT '0' COMMENT '状态 0：空闲 1：使用中 -1:不可用 -2：删除',
  `serviceKey` varchar(255) DEFAULT NULL COMMENT '服务中心key',
  `deptId` int(11) DEFAULT NULL COMMENT '部门id',
  `windowId` int(11) DEFAULT NULL COMMENT '窗口id',
  `userId` int(11) DEFAULT NULL COMMENT '用户id',
  `form` int(11) DEFAULT '0' COMMENT '形态 0：未分配 1：服务中心终端 2：审批中心终端 3：默认终端 4：高拍仪终端 5：统筹中心终端',
  `associated` varchar(255) DEFAULT NULL COMMENT '关联字段。用于扩展关联，当form为4时此字段为高拍仪扩展字段',
  PRIMARY KEY (`id`),
  KEY `fk_dev_devtype_type` (`type`) USING BTREE,
  KEY `fk_dev_devrole_role` (`role`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='需要补充 -----需要继续';

-- ----------------------------
-- Records of vc_dev
-- ----------------------------
