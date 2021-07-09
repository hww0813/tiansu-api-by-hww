
-- 描述：http接口表新增字段
-- 相关功能：http接口新增请求参数/相应参数
-- 更新时间：2020.7.9
-- 作者：徐灿

ALTER TABLE `itms`.`busi_http_perf`
ADD COLUMN `request_parameter` varchar(1000) NULL COMMENT '请求参数' AFTER `http_status`,
ADD COLUMN `response_parameter` varchar(1000)  NULL COMMENT '响应参数' AFTER `request_parameter`,


