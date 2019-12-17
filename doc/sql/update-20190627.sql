DROP TABLE IF EXISTS `t_x86_config_information`;
CREATE TABLE `t_x86_config_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `column_name_en` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '字段英文名',
  `column_name_cn` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '字段中文名',
  `column_value` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '字段值',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `state` int(1) DEFAULT NULL COMMENT '状态 1:有效 0:无效 ',
  `describes` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '描述',
  `data_type` int(2) DEFAULT NULL COMMENT '1.字符串，2.整数，3.浮点数，4.布尔类型',
  `config_type` int(2) DEFAULT NULL COMMENT '1：x86配置文件  2：x86升级包',
  `regex` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '字段值校验规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of t_x86_config_information
-- ----------------------------
BEGIN;
INSERT INTO `t_x86_config_information` VALUES (2, 'timeOut', '超时时间', '49999', '2019-07-26 16:45:13', '2019-07-02 17:51:07', 1, '大于等于5000毫秒,小于等于50000毫秒\r\n', 2, 1, '^([5-9][0-9]{3}|[1-4][0-9]{4}|[5][0]{4})$');
INSERT INTO `t_x86_config_information` VALUES (3, 'pictureMaxSize', '图片大小最大值', '5', '2019-07-26 16:41:43', '2019-07-02 17:51:09', 1, '大于等于1MB ,小于等于5MB \r\n', 2, 1, '^([1-5])$');
INSERT INTO `t_x86_config_information` VALUES (4, 'signatureTimeOut', '签名板超时时间', '99999', '2019-07-26 16:45:13', '2019-07-02 17:51:11', 1, '大于等于10000毫秒,小于等于100000毫秒\r\n', 2, 1, '^([1-9][0-9]{4}|[1][0]{5})$');
INSERT INTO `t_x86_config_information` VALUES (5, 'heartBeat', '心跳超时时间', '30000', '2019-07-12 21:39:15', '2019-07-02 17:51:11', 1, '大于等于30000毫秒,小于等于300000毫秒\r\n', 2, 1, '^([3-9][0-9]{4}|[1-2][0-9]{5}|[3][0]{5})$');
INSERT INTO `t_x86_config_information` VALUES (6, 'threshold', '比对阈值', '0.7', '2019-07-23 18:57:57', '2019-07-02 17:51:11', 1, '大于等于0.6,小于等于1（小数点后最多保留2位）\r\n', 3, 1, '^([0][.][6-9]{1,2}|[1])$');
INSERT INTO `t_x86_config_information` VALUES (7, 'appId', '人证对比appId', 'E1nPsJo5rRSWvpwk2whePu2hWqZnZFLDyuuCcSNfZjJX', '2019-07-11 15:30:42', '2019-07-02 17:51:11', 1, '字符长度：\r\n>=1 且 <= 255\r\n', 1, 1, '^[a-zA-Z0-9]{1,255}$');
INSERT INTO `t_x86_config_information` VALUES (8, 'sdkKey', '人证对比key', 'HnTUbZhb4VJJmnicZ785nS6HC4zEH7YReVdSBcAhb7mM', '2019-07-11 15:30:47', '2019-07-02 17:51:11', 1, '字符长度：\r\n>=1 且 <= 255\r\n', 1, 1, '^[a-zA-Z0-9]{1,255}$');
INSERT INTO `t_x86_config_information` VALUES (10, 'zipUrl', '包路径', 'E:\\ideaworkspace\\remoteservice-new\\X86\\data\\v2v_GovServiceTerminal-3.1.1.0.zip', '2019-07-11 18:01:42', '2019-07-03 10:09:51', 0, NULL, 1, 2, NULL);
INSERT INTO `t_x86_config_information` VALUES (11, 'version', '版本号', '3.1.1.0', '2019-07-11 18:01:42', '2019-07-03 10:09:55', 1, '升级包版本号', 1, 1, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;




INSERT INTO `t_sys_permission` (`permissionId`, `parentId`, `permissionName`, `description`, `state`, `type`, `createTime`, `updateTime`, `defaultPermission`, `version`, `url`, `perms`, `icon`, `seq`) VALUES ('91', '9', '服务设置', '', '1', '2', '2018-11-30 16:48:50', '2018-12-13 18:29:03', '0', '0', '', 'sys:service', NULL, NULL);
