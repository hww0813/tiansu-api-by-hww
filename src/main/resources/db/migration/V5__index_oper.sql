
-- 描述：oper操作行为表添加索引
-- 相关功能：oper表 动作开始结束时间加索引
-- 更新时间：2020.7.11
-- 作者：徐灿

ALTER TABLE `itms`.`busi_oper`
ADD INDEX `idx_oper_start`(`OPER_START`) USING BTREE,
ADD INDEX `idx_oper_end`(`OPER_END`) USING BTREE;
