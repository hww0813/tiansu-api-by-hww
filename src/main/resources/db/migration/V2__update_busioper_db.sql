
-- 描述：更新操作行为相关表结构
-- 相关功能：关联播放停止时间和相关操作行为，匹配探针机器码，进行平台过滤
-- 更新时间：2020.7.9
-- 作者：徐灿

ALTER TABLE `itms`.`busi_oper`
ADD COLUMN `OPER_START` datetime(0) NULL COMMENT '播放开始时间' AFTER `BIT_RATE`,
ADD COLUMN `OPER_END` datetime(0) NULL COMMENT '播放结束时间' AFTER `OPER_START`,
ADD COLUMN `PROBE_HD_INFO` varchar(255) NULL COMMENT '探针机器码' AFTER `OPER_END`,
ADD COLUMN `RELATION_OPER_UUID` varchar(255) NULL COMMENT '相关操作行为uuid' AFTER `PROBE_HD_INFO`;

ALTER TABLE `itms`.`busi_oper_t`
ADD COLUMN `OPER_START` datetime(0) NULL COMMENT '播放开始时间' AFTER `BIT_RATE`,
ADD COLUMN `OPER_END` datetime(0) NULL COMMENT '播放结束时间' AFTER `OPER_START`,
ADD COLUMN `PROBE_HD_INFO` varchar(255) NULL COMMENT '探针机器码' AFTER `OPER_END`,
ADD COLUMN `RELATION_OPER_UUID` varchar(255) NULL COMMENT '相关操作行为uuid' AFTER `PROBE_HD_INFO`;

