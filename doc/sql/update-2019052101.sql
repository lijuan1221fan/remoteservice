ALTER TABLE `t_business_type`
ADD COLUMN `supReceipt` int(1) NULL COMMENT '回执单模式：1:查看本地回执单，2单独上传电子签名模式，3无需提供业务回执' AFTER `afternoon_limit_number`;
ALTER TABLE `t_type_materials_rel`
ADD COLUMN `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键' FIRST,
ADD COLUMN `update_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建更新时间' AFTER `materials_id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;

ALTER TABLE `t_business_type`
MODIFY COLUMN `police_sign` int(2) NULL COMMENT '是否需要公安电子签名加盖打印 0：不需要 1：需要' AFTER `remote_print`,
MODIFY COLUMN `police_notarize` int(2) NULL COMMENT '是否需要公安信息确认单 0：不需要 1：需要' AFTER `police_sign`;
