/**
* 2.9.1vesion数据监控
*
*/
ALTER TABLE `t_business_info`
ADD COLUMN `handle_service_key`  varchar(255) COMMENT '受理中心servicekey';
ALTER TABLE `t_service_center`
ADD COLUMN `province`  varchar(255) COMMENT 'province';
ALTER TABLE `t_service_center`
ADD COLUMN `city`  varchar(255) COMMENT 'city';
ALTER TABLE `t_service_center`
ADD COLUMN `area`  varchar(255) COMMENT 'area';
ALTER TABLE `t_service_center`
ADD COLUMN `street`  varchar(255) COMMENT 'street';
ALTER TABLE `t_service_center`
ADD COLUMN `community`  varchar(255) COMMENT 'community';
ALTER TABLE `t_service_center`
ADD COLUMN `lal`  varchar(255) COMMENT 'lal';

