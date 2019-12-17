DROP TABLE IF EXISTS `t_embedded_server`;
CREATE TABLE `t_embedded_server` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `vc_dev_id` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '终端id',
  `session_id` varchar(255) COLLATE utf8_bin NOT NULL COMMENT 'x86连接sessionId',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `vc_dev_id` (`vc_dev_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3979 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
