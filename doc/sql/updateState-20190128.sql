 ALTER TABLE `t_business_details_classification`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `type`;
-- 状态 0正常，-1为弃用
update t_business_details_classification set state =1 where state =0 ;
update t_business_details_classification set state =0 where state =-1 ;

ALTER TABLE `t_business_info`
CHANGE COLUMN `statu` `state` tinyint(2) NULL DEFAULT 1 COMMENT '业务状态:1 未处理 2 处理中 3 已处理 4  过号' AFTER `end_time`;
ALTER TABLE .`t_business_info`
CHANGE COLUMN `final_statu` `final_state` tinyint(2) NULL DEFAULT 0 COMMENT '结束时的状态  0：未结束 1：已经处理  2：未处理' AFTER `terminal_number`;
update t_business_info SET state = 1 where state = 0;
update t_business_info SET state = 2 where state = 1;
update t_business_info SET state = 3 where state = 2;
update t_business_info SET state = 4 where state = 3;




 ALTER TABLE `t_business_type`
 CHANGE COLUMN `status` `state` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `version`;
 update t_business_type SET state = 1 where state = 0;
update t_business_type SET state = 0 where state = -2;
-- 状态 -2：删除 -1禁用

ALTER TABLE `t_common_config`
MODIFY COLUMN `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `commonValue`;
UPDATE t_common_config set state =1 where state =0;
-- 状态，默认0,
ALTER TABLE `t_device_info`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `type`;
-- 状态  0：可用 -1:删除  2：不可用
UPDATE t_device_info SET state =1 where state=0;
UPDATE t_device_info SET state =-1 where state=2;
UPDATE t_device_info SET state =0 where state=-1;

ALTER TABLE `t_forms`
MODIFY COLUMN `state` int(11) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `formAddress`;
-- 状态 0：正常  -1：删除
UPDATE t_forms set state =1 where state = 0;
UPDATE t_forms set state =0 where state = -1;


ALTER TABLE `t_materials`
CHANGE COLUMN `materials_status` `state` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `materials_version`;
ALTER TABLE `t_meeting_operation`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态,1:会议中,2:已停会' AFTER `ip`;
-- 状态 -1删除 0:会议中,1:已停会
update t_materials set state = 0 where state =-1;

ALTER TABLE `t_photo`
CHANGE COLUMN `statu` `state` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `icon_path`;
ALTER TABLE `t_role`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `updateTime`;
-- 状态 -1删除
update t_photo set state = 0 where state =-1;

ALTER TABLE `t_service_center`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `address`;
-- 状态 0：可用 -1:删除  2：不可用
update t_service_center set state= 0 where state =-1;
update t_service_center set state= 1 where state =0;
update t_service_center set state= -1 where state =2;

ALTER TABLE `t_signedphotoes`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `version`;
-- 状态 0：可用 -1:删除  2：不可用
update t_signedphotoes set state= -1 where state =2;

ALTER TABLE `t_sys_dept`
CHANGE COLUMN `dept_status` `state` int(2) NOT NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `dept_contacts_phone`;
-- 部门状态  -2 删除  -1：禁用  0：启用
update t_sys_dept set state =1 where state =0 ;
update t_sys_dept set state =0 where state =-2 ;

ALTER TABLE `t_sys_permission`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `description`;
-- 权限状态，1有效，0无效


ALTER TABLE `t_sys_role`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效 -1:禁用' AFTER `updateTime`;
-- 角色状态，1有效，0无效

-- 用户状态0离线，1在线 ,-1删除
ALTER TABLE `t_sys_user`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 1:在线 0:离线' AFTER `password`;
ALTER TABLE `t_sys_user`
CHANGE COLUMN `workStatus` `workState` int(2) NULL DEFAULT 0 COMMENT '工作状态： 0：空闲中 1：等待中 2：处理中' AFTER `state`,
CHANGE COLUMN `userStatus` `userState` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT 1 COMMENT '1可用 0 无效 -1：禁用' AFTER `type`;
UPDATE t_sys_user set userState  = 0 where state = 2;


ALTER TABLE `t_sys_window`
CHANGE COLUMN `status` `state` int(11) NOT NULL DEFAULT 1 COMMENT '状态 1有效 0无效 -1禁用' AFTER `ip`;
-- 状态 1可用 2不可用  -1删除
update t_sys_window set state =0  where state=-1;
update t_sys_window set state =-1  where state=2;


ALTER TABLE `t_terminal`
CHANGE COLUMN `status` `state` int(2) NULL DEFAULT 1 COMMENT '状态 2：使用中 1：有效空闲  0：无效 -1:禁用 ' AFTER `type`;
-- 状态 0：空闲 1：使用中 -1:不可用 -2：删除
update t_terminal set state =2  where state=1;
update t_sys_window set state =1  where state=0;
update t_sys_window set state =0  where state=-2;



ALTER TABLE `t_upload`
MODIFY COLUMN `state` int(11) NULL DEFAULT 1 COMMENT '状态 1:有效 0:无效  2:生成签名图片 3:生成签章图片 4:生成图片加签章图片' AFTER `business_id`;
-- 状态 0 ：正常 -1 删除 1 生成签名图片 2 生成签章图片 3 生成图片加签章图片
update t_upload set state =4  where state =3;
update t_upload set state =3  where state =2;
update t_upload set state =2  where state =1;
update t_upload set state =1  where state =0;
update t_upload set state =0  where state =-1;


ALTER TABLE `vc_dev`
MODIFY COLUMN  `state` int(2) NULL DEFAULT 1 COMMENT '状态 2:使用中 1:空闲，有效  0:删除 -1:禁用 ' AFTER `address`;
-- 状态 0：空闲 1：使用中 -1:不可用 -2：删除
update vc_dev set state=2 where state =1 ;
update vc_dev set state=1 where state =0 ;
update vc_dev set state=0 where state =-2 ;
