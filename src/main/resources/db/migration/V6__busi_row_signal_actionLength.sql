
-- 更新时间：2020.8.17
-- 作者：易鹏飞

-- 修改action的字段长度为255
ALTER TABLE busi_raw_signal MODIFY COLUMN action VARCHAR(255);


--  备份busi_raw_siagnal_t表
DROP TABLE IF EXISTS `busi_raw_signal_tmp`;
alter table busi_raw_signal_t rename busi_raw_signal_t_bak0817;


-- 重建busi_raw_siagnal_temp和busi_raw_siagnal_t表;

create table busi_raw_signal_tmp like busi_raw_signal;
create table busi_raw_signal_t like busi_raw_signal;


--删除busi_raw_siagnal_temp表的分区;

alter table busi_raw_signal_tmp remove partitioning;

