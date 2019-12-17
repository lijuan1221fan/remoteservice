ALTER TABLE t_business_info MODIFY terminal_number VARCHAR(255) COMMENT '终端号码';
ALTER TABLE vc_dev MODIFY id VARCHAR(255) COMMENT '终端号';
alter table t_business_type add isCustom INT(2) NULL COMMENT '是否自定义签名盖章  1.后台配置  2.自由拖拽';
