
/**
* M-远程申办业务模式拓展及业务数据监控
 *
 */
 /**
 原始需求：1	实现除公安特殊业务外的其他业务可以由综合窗口统一受理，公安特殊业务由特定窗口受理的统一和定制并存的业务受理模式
  */
ALTER TABLE t_sys_dept`
ADD COLUMN `type` integer(1) NULL DEFAULT 0 COMMENT '一窗综办：0，是、1，否' AFTER `stampUrl`;
