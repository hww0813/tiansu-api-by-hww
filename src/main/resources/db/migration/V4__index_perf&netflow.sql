
-- 描述：http接口表/netFlow流量表添加索引
-- 相关功能：http接口表/netFlow流量表添加索引
-- 更新时间：2020.7.11
-- 作者：徐灿

ALTER TABLE `itms`.`busi_http_perf`
ADD INDEX `fk_src_port`(`src_port`) USING BTREE,
ADD INDEX `fk_dst_ip`(`dst_ip`) USING BTREE,
ADD INDEX `fk_dst_port`(`dst_port`) USING BTREE,
ADD INDEX `fk_stamp`(`stamp`) USING BTREE;

ALTER TABLE `itms`.`busi_raw_net_flow`
ADD INDEX `fk_src_port`(`src_port`) USING BTREE,
ADD INDEX `fk_dst_ip`(`dst_ip`) USING BTREE,
ADD INDEX `fk_dst_port`(`dst_port`) USING BTREE,
ADD INDEX `fk_stamp`(`stamp`) USING BTREE;
