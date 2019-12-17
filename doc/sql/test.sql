<!-- 表字段修改-->
ALTER TABLE `rs_dev`.`t_business_type`
CHANGE COLUMN `supReceipt` `sup_receipt` int(1) NULL DEFAULT NULL COMMENT '回执单模式：1:查看本地回执单，2单独上传电子签名模式，3无需提供业务回执' AFTER `afternoon_limit_number`,
CHANGE COLUMN `isCustom` `is_custom` int(2) NULL DEFAULT NULL COMMENT '是否自定义签名盖章  1.自定义  2.配置' AFTER `sup_receipt`,
CHANGE COLUMN `isComprehensive` `is_comprehensive` int(1) NULL DEFAULT NULL COMMENT '是否仅支持统筹受理:1是，0否' AFTER `is_custom`;
