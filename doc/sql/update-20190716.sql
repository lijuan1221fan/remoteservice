/**
socket关联关系管理表修改
 */
ALTER TABLE `t_embedded_server`
MODIFY COLUMN `user_id` Int(11) NULL DEFAULT NULL COMMENT '用户id' AFTER `session_id`;
 ALTER TABLE `t_embedded_server`
ADD UNIQUE INDEX (`vc_dev_id`) USING BTREE ;
